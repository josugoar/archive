#define WITH_WOLFSSL
#define NAME "localhost"
#define SERVICE "1025"
#define CTX_SERVER_CERT server_cert_der_1024
#define CTX_SERVER_KEY server_key_der_1024
#define CLIENTS_LEN 4096
#define BUF_LEN 1200

#undef DEBUG_WOLFSSL
#define USE_CERT_BUFFERS_1024
#define WC_NO_HARDEN
#define WOLFSSL_TLS13

#include <errno.h>
#include <fcntl.h>
#include <netdb.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <sys/select.h>
#include <sys/socket.h>
#include <unistd.h>
#ifdef WITH_WOLFSSL
#include <wolfssl/wolfcrypt/settings.h>
#include <wolfssl/certs_test.h>
#include <wolfssl/ssl.h>
#endif
#ifdef ESP_PLATFORM
#include <esp_event.h>
#include <esp_netif.h>
#include <nvs_flash.h>
#include <protocol_examples_common.h>
#endif

static const char *TAG = "tcp_server";

#ifdef WITH_WOLFSSL
void wolfSSL_log(int log_level, const char *log_message)
{
    (void)log_level;

    fprintf(stderr, "%s %s: %s\n", "V", "wolfssl", log_message);
}
#endif

#ifdef ESP_PLATFORM
void app_main(void)
#else
int main(void)
#endif
{
    int errnum = 0;

    struct addrinfo *ais = NULL;
    int server_fd = -1;
    int client_fds[CLIENTS_LEN] = {0};
    size_t client_fds_len = sizeof(client_fds) / sizeof(*client_fds);
    for (size_t i = 0; i < client_fds_len; ++i)
    {
        client_fds[i] = -1;
    }
#ifdef WITH_WOLFSSL
    WOLFSSL_CTX *ctx = NULL;
    WOLFSSL *ssls[CLIENTS_LEN] = {0};
    size_t ssls_len = sizeof(ssls) / sizeof(*ssls);
#endif

#ifdef ESP_PLATFORM
    ESP_ERROR_CHECK(nvs_flash_init());
    ESP_ERROR_CHECK(esp_netif_init());
    ESP_ERROR_CHECK(esp_event_loop_create_default());
    ESP_ERROR_CHECK(example_connect());
#endif

#ifdef WITH_WOLFSSL
#ifdef DEBUG_WOLFSSL
    errnum = wolfSSL_Debugging_ON();
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: wolfSSL_Debugging_ON: %s\n", "V", TAG, wolfSSL_ERR_reason_error_string(errnum));
    }

    errnum = wolfSSL_SetLoggingCb(wolfSSL_log);
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: wolfSSL_SetLoggingCb: %s\n", "V", TAG, wolfSSL_ERR_reason_error_string(errnum));
    }
#endif
#endif

    struct addrinfo req = {
        .ai_flags = AI_PASSIVE,
        .ai_family = AF_UNSPEC,
        .ai_socktype = SOCK_STREAM,
    };

    errnum = getaddrinfo(NAME, SERVICE, &req, &ais);
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: getaddrinfo name=%s service=%s: %d\n", "E", TAG, NAME, SERVICE, errnum);

        goto cleanup;
    }

    struct sockaddr_storage local_addr = {0};
    socklen_t local_addr_len = sizeof(local_addr);

    struct sockaddr_storage remote_addr = {0};
    socklen_t remote_addr_len = sizeof(remote_addr);

    for (struct addrinfo *ai = ais; ai; ai = ai->ai_next)
    {
        int fd = socket(ai->ai_family, ai->ai_socktype, ai->ai_protocol);
        if (fd == -1)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "%s %s: socket domain=%d type=%d protocol=%d: %s\n", "E", TAG, ai->ai_family, ai->ai_socktype, ai->ai_protocol, strerror(errno));

                goto cleanup;
            }

            fprintf(stderr, "%s %s: socket domain=%d type=%d protocol=%d: %s\n", "W", TAG, ai->ai_family, ai->ai_socktype, ai->ai_protocol, strerror(errno));

            continue;
        }

        if (bind(fd, ai->ai_addr, ai->ai_addrlen) != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "%s %s: bind fd=%d: %s\n", "E", TAG, fd, strerror(errno));

                goto cleanup;
            }

            fprintf(stderr, "%s %s: bind fd=%d: %s\n", "W", TAG, fd, strerror(errno));

            if (close(fd) != 0)
            {
                fprintf(stderr, "%s %s: close fd=%d: %s\n", "W", TAG, fd, strerror(errno));
            }

            continue;
        }

        server_fd = fd;

        memcpy(&local_addr, ai->ai_addr, ai->ai_addrlen);
        local_addr_len = ai->ai_addrlen;

        break;
    }

    fprintf(stderr, "%s %s: server_fd=%d\n", "I", TAG, server_fd);

    if (listen(server_fd, SOMAXCONN) != 0)
    {
        fprintf(stderr, "%s %s: listen fd=%d: %s\n", "E", TAG, server_fd, strerror(errno));

        goto cleanup;
    }

    char local_host[NI_MAXHOST] = "";
    char local_serv[NI_MAXSERV] = "";

    if (getnameinfo((struct sockaddr *)&local_addr, local_addr_len, local_host, sizeof(local_host), local_serv, sizeof(local_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
    {
        fprintf(stderr, "%s %s: getnameinfo: %s\n", "W", TAG, strerror(errno));
    }

    fprintf(stderr, "%s %s: local_host=%s local_serv=%s\n", "I", TAG, local_host, local_serv);

#ifdef WITH_WOLFSSL
    ctx = wolfSSL_CTX_new(wolfTLSv1_3_server_method());
    if (!ctx)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_new\n", "E", TAG);

        goto cleanup;
    }

    errnum = wolfSSL_CTX_use_certificate_chain_buffer_format(ctx, CTX_SERVER_CERT, sizeof(CTX_SERVER_CERT), WOLFSSL_FILETYPE_ASN1);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_use_certificate_chain_buffer_format: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));

        goto cleanup;
    }

    errnum = wolfSSL_CTX_use_PrivateKey_buffer(ctx, CTX_SERVER_KEY, sizeof(CTX_SERVER_KEY), WOLFSSL_FILETYPE_ASN1);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_use_PrivateKey_buffer: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));

        goto cleanup;
    }

    errnum = wolfSSL_CTX_check_private_key(ctx);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_check_private_key: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));

        goto cleanup;
    }
#endif

    int acceptfd = server_fd;

    int *readfds = client_fds;
    size_t readfds_len = client_fds_len;

    int writefd = STDOUT_FILENO;

    int fds[] = {acceptfd, writefd};
    size_t fds_len = sizeof(fds) / sizeof(*fds);

    int next_nfds = -1;

    for (size_t i = 0; i < fds_len; ++i)
    {
        int fd = fds[i];

        if (fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK) != 0)
        {
            fprintf(stderr, "%s %s: fcntl fd=%d: %s\n", "E", TAG, fd, strerror(errno));

            goto cleanup;
        }

        if (fd >= FD_SETSIZE)
        {
            fprintf(stderr, "%s %s: fd=%d >= FD_SETSIZE\n", "E", TAG, fd);

            goto cleanup;
        }

        next_nfds = next_nfds > fd ? next_nfds : fd + 1;
    }

    fd_set next_readfd_set = {0};
    FD_ZERO(&next_readfd_set);

    fd_set next_writefd_set = {0};
    FD_ZERO(&next_writefd_set);

    unsigned char buf[BUF_LEN] = {0};
    size_t nread = 0;
    size_t nwrite = 0;

    // TODO: dont quit if socket error
    // goto cleanup scoed to loop (maybe if cond goto cleanup)

    FD_SET(acceptfd, &next_readfd_set);

    while (true)
    {
        fd_set readfd_set = next_readfd_set;
        fd_set writefd_set = next_writefd_set;

        int nfds = select(next_nfds, &readfd_set, &writefd_set, NULL, NULL);
        if (nfds == -1)
        {
            if (errno == EINTR)
            {
                continue;
            }

            fprintf(stderr, "%s %s: select: %s\n", "E", TAG, strerror(errno));

            goto cleanup;
        }

        if (FD_ISSET(acceptfd, &readfd_set) != 0)
        {
            while (true)
            {
                size_t client_fd_idx = client_fds_len;

                for (size_t i = 0; i < client_fds_len; ++i)
                {
                    int client_fd = client_fds[i];

                    if (client_fd == -1)
                    {
                        client_fd_idx = i;

                        break;
                    }
                }

                if (client_fd_idx >= client_fds_len)
                {
                    FD_CLR(acceptfd, &next_readfd_set);

                    break;
                }

                size_t i = client_fd_idx;

                int client_fd = accept(acceptfd, (struct sockaddr *)&remote_addr, &remote_addr_len);
                if (client_fd == -1)
                {
                    if (errno == EAGAIN || errno == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "%s %s: accept fd=%d: %s\n", "E", TAG, acceptfd, strerror(errno));

                    goto cleanup;
                }

                fprintf(stderr, "%s %s: client_fd=%d\n", "I", TAG, client_fd);

                char remote_host[NI_MAXHOST] = "";
                char remote_serv[NI_MAXSERV] = "";

                if (getnameinfo((struct sockaddr *)&remote_addr, remote_addr_len, remote_host, sizeof(remote_host), remote_serv, sizeof(remote_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
                {
                    fprintf(stderr, "%s %s: getnameinfo: %s\n", "W", TAG, strerror(errno));
                }

                fprintf(stderr, "%s %s: remote_host=%s remote_serv=%s\n", "I", TAG, remote_host, remote_serv);

#ifdef WITH_WOLFSSL
                WOLFSSL *ssl = wolfSSL_new(ctx);
                if (!ssl)
                {
                    fprintf(stderr, "%s %s: wolfSSL_new\n", "E", TAG);

                    goto cleanup;
                }

                errnum = wolfSSL_set_fd(ssl, client_fd);
                if (errnum != WOLFSSL_SUCCESS)
                {
                    fprintf(stderr, "%s %s: wolfSSL_set_fd fd=%d: %s\n", "E", TAG, client_fd, wolfSSL_ERR_reason_error_string(errnum));

                    goto cleanup;
                }

                errnum = wolfSSL_accept(ssl);
                if (errnum != WOLFSSL_SUCCESS)
                {
                    fprintf(stderr, "%s %s: wolfSSL_accept fd=%d: %s\n", "E", TAG, client_fd, wolfSSL_ERR_reason_error_string(wolfSSL_get_error(ssl, errnum)));

                    goto cleanup;
                }
#endif

                int readfd = client_fd;

                if (readfd >= FD_SETSIZE)
                {
                    fprintf(stderr, "%s %s: fd=%d >= FD_SETSIZE\n", "E", TAG, readfd);

                    goto cleanup;
                }

                if (fcntl(readfd, F_SETFL, fcntl(readfd, F_GETFL) | O_NONBLOCK) != 0)
                {
                    fprintf(stderr, "%s %s: fcntl fd=%d: %s\n", "E", TAG, readfd, strerror(errno));

                    goto cleanup;
                }

                next_nfds = next_nfds > readfd ? next_nfds : readfd + 1;

                readfds[i] = readfd;

#ifdef WITH_WOLFSSL
                ssls[i] = ssl;
#endif

                FD_SET(readfd, &next_readfd_set);
            }
        }

        for (size_t i = 0; i < readfds_len; ++i)
        {
            int readfd = readfds[i];

#ifdef WITH_WOLFSSL
            WOLFSSL *ssl = ssls[i];
#endif

            if (FD_ISSET(readfd, &readfd_set) != 0)
            {
                while (true)
                {
                    if (nread >= sizeof(buf))
                    {
                        FD_CLR(readfd, &next_readfd_set);

                        break;
                    }

#ifdef WITH_WOLFSSL
                    ssize_t n = wolfSSL_read(ssl, buf + nread, sizeof(buf) - nread);
                    if (n == -1)
                    {
                        errnum = wolfSSL_get_error(ssl, n);

                        if (errnum == WOLFSSL_ERROR_WANT_READ || errnum == WOLFSSL_ERROR_WANT_WRITE)
                        {
                            break;
                        }

                        fprintf(stderr, "%s %s: wolfSSL_read fd=%d: %s\n", "E", TAG, readfd, wolfSSL_ERR_reason_error_string(errnum));

                        goto cleanup;
                    }
#else
                    ssize_t n = read(readfd, buf + nread, sizeof(buf) - nread);
                    if (n == -1)
                    {
                        if (errno == EAGAIN || errno == EWOULDBLOCK)
                        {
                            break;
                        }

                        fprintf(stderr, "%s %s: read fd=%d: %s\n", "E", TAG, readfd, strerror(errno));

                        goto cleanup;
                    }
#endif

                    if (n == 0)
                    {
#ifdef WITH_WOLFSSL
                        wolfSSL_free(ssl);
#endif

                        if (close(readfd) != 0)
                        {
                            fprintf(stderr, "%s %s: close fd=%d: %s\n", "W", TAG, readfd, strerror(errno));
                        }

                        readfds[i] = -1;

#ifdef WITH_WOLFSSL
                        ssls[i] = NULL;
#endif

                        FD_SET(acceptfd, &next_readfd_set);

                        FD_CLR(readfd, &next_readfd_set);

                        break;
                    }

                    nread += n;

                    FD_SET(writefd, &writefd_set);
                }

                if (FD_ISSET(writefd, &writefd_set) != 0)
                {
                    for (size_t j = 0; j < readfds_len; ++j)
                    {
                        int readfd_this = readfds[i];
                        int readfd_other = readfds[j];

                        if (readfd_other == -1)
                        {
                            continue;
                        }

                        if (readfd_other == readfd_this)
                        {
                            continue;
                        }

                        FD_CLR(readfd_other, &next_readfd_set);
                    }

                    break;
                }
            }
        }

        if (FD_ISSET(writefd, &writefd_set) != 0)
        {
            while (true)
            {
                if (nwrite >= nread)
                {
                    nread = 0;
                    nwrite = 0;

                    for (size_t i = 0; i < readfds_len; ++i)
                    {
                        int readfd = readfds[i];

                        if (readfd == -1)
                        {
                            continue;
                        }

                        FD_SET(readfd, &next_readfd_set);
                    }

                    FD_CLR(writefd, &next_writefd_set);

                    break;
                }

                ssize_t n = write(writefd, buf + nwrite, nread - nwrite);
                if (n == -1)
                {
                    if (errno == EAGAIN || errno == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "%s %s: write fd=%d: %s\n", "E", TAG, writefd, strerror(errno));

                    goto cleanup;
                }

                nwrite += n;
            }
        }
    }

cleanup:
#ifdef WITH_WOLFSSL
    for (size_t i = 0; i < ssls_len; ++i)
    {
        WOLFSSL *ssl = ssls[i];

        if (ssl)
        {
            wolfSSL_free(ssl);
        }
    }
#endif

#ifdef WITH_WOLFSSL
    if (ctx)
    {
        wolfSSL_CTX_free(ctx);
    }
#endif

    for (size_t i = 0; i < client_fds_len; ++i)
    {
        int client_fd = client_fds[i];

        if (client_fd == -1)
        {
            continue;
        }

        if (close(client_fd) != 0)
        {
            fprintf(stderr, "%s %s: close fd=%d: %s\n", "W", TAG, client_fd, strerror(errno));
        }
    }

    if (server_fd != -1)
    {
        if (close(server_fd) != 0)
        {
            fprintf(stderr, "%s %s: close fd=%d: %s\n", "W", TAG, server_fd, strerror(errno));
        }
    }

    if (ais)
    {
        freeaddrinfo(ais);
    }
}
