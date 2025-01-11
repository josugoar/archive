#define WITH_WOLFSSL
#define NAME "localhost"
#define SERVICE "1025"
#define CA_CERT ca_cert_der_1024
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

static const char *TAG = "tls_tcp_client";

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
    int client_fd = -1;
#ifdef WITH_WOLFSSL
    WOLFSSL_CTX *ctx = NULL;
    WOLFSSL *ssl = NULL;
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

        if (connect(fd, ai->ai_addr, ai->ai_addrlen) != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "%s %s: connect fd=%d: %s\n", "E", TAG, fd, strerror(errno));

                goto cleanup;
            }

            fprintf(stderr, "%s %s: connect fd=%d: %s\n", "W", TAG, fd, strerror(errno));

            if (close(fd) != 0)
            {
                fprintf(stderr, "%s %s: close fd=%d: %s\n", "W", TAG, fd, strerror(errno));
            }

            continue;
        }

        client_fd = fd;

        memcpy(&remote_addr, ai->ai_addr, ai->ai_addrlen);
        remote_addr_len = ai->ai_addrlen;

        break;
    }

    fprintf(stderr, "%s %s: client_fd=%d\n", "I", TAG, client_fd);

    if (getsockname(client_fd, (struct sockaddr *)&local_addr, &local_addr_len) != 0)
    {
        fprintf(stderr, "%s %s: getsockname fd=%d: %s\n", "E", TAG, client_fd, strerror(errno));

        goto cleanup;
    }

    char local_host[NI_MAXHOST] = "";
    char local_serv[NI_MAXSERV] = "";

    if (getnameinfo((struct sockaddr *)&local_addr, local_addr_len, local_host, sizeof(local_host), local_serv, sizeof(local_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
    {
        fprintf(stderr, "%s %s: getnameinfo: %s\n", "W", TAG, strerror(errno));
    }

    fprintf(stderr, "%s %s: local_host=%s local_serv=%s\n", "I", TAG, local_host, local_serv);

    char remote_host[NI_MAXHOST] = "";
    char remote_serv[NI_MAXSERV] = "";

    if (getnameinfo((struct sockaddr *)&remote_addr, remote_addr_len, remote_host, sizeof(remote_host), remote_serv, sizeof(remote_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
    {
        fprintf(stderr, "%s %s: getnameinfo: %s\n", "W", TAG, strerror(errno));
    }

    fprintf(stderr, "%s %s: remote_host=%s remote_serv=%s\n", "I", TAG, remote_host, remote_serv);

#ifdef WITH_WOLFSSL
    ctx = wolfSSL_CTX_new(wolfTLSv1_3_client_method());
    if (!ctx)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_new\n", "E", TAG);

        goto cleanup;
    }

    errnum = wolfSSL_CTX_load_verify_buffer(ctx, CA_CERT, sizeof(CA_CERT), WOLFSSL_FILETYPE_ASN1);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_load_verify_buffer: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));

        goto cleanup;
    }
#endif

#ifdef WITH_WOLFSSL
    ssl = wolfSSL_new(ctx);
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

    errnum = wolfSSL_connect(ssl);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_connect: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(wolfSSL_get_error(ssl, errnum)));

        goto cleanup;
    }
#endif

    int readfd = STDIN_FILENO;

    int writefd = client_fd;

    int fds[] = {readfd, writefd};
    size_t fds_len = sizeof(fds) / sizeof(*fds);

    int next_nfds = -1;

    for (size_t i = 0; i < fds_len; ++i)
    {
        int fd = fds[i];

        if (fd >= FD_SETSIZE)
        {
            fprintf(stderr, "%s %s: fd=%d >= FD_SETSIZE\n", "E", TAG, fd);

            goto cleanup;
        }

        if (fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK) != 0)
        {
            fprintf(stderr, "%s %s: fcntl fd=%d: %s\n", "E", TAG, fd, strerror(errno));

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

    bool eof = false;

    FD_SET(readfd, &next_readfd_set);

    while (FD_ISSET(readfd, &next_readfd_set) != 0 || FD_ISSET(writefd, &next_writefd_set) != 0)
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

        if (FD_ISSET(readfd, &readfd_set) != 0)
        {
            while (true)
            {
                if (nread >= sizeof(buf))
                {
                    FD_CLR(readfd, &next_readfd_set);

                    break;
                }

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

                if (n == 0)
                {
                    eof = true;

                    FD_CLR(readfd, &next_readfd_set);

                    break;
                }

                nread += n;

                FD_SET(writefd, &next_writefd_set);
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

                    if (!eof)
                    {
                        FD_SET(readfd, &next_readfd_set);
                    }

                    FD_CLR(writefd, &next_writefd_set);

                    break;
                }

#ifdef WITH_WOLFSSL
                ssize_t n = wolfSSL_write(ssl, buf + nwrite, nread - nwrite);
                if (n == -1)
                {
                    errnum = wolfSSL_get_error(ssl, n);

                    if (errnum == EAGAIN || errnum == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "%s %s: wolfSSL_write fd=%d: %s\n", "E", TAG, writefd, wolfSSL_ERR_reason_error_string(errnum));

                    goto cleanup;
                }
#else
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
#endif

                nwrite += n;
            }
        }
    }

#ifdef WITH_WOLFSSL
    if (wolfSSL_shutdown(ssl) != 0)
    {
        fprintf(stderr, "%s %s: wolfSSL_shutdown fd=%d: %s\n", "W", TAG, client_fd, strerror(errno));
    }
#else
    if (shutdown(client_fd, SHUT_RDWR) != 0)
    {
        fprintf(stderr, "%s %s: shutdown fd=%d: %s\n", "W", TAG, client_fd, strerror(errno));
    }
#endif

cleanup:
#ifdef WITH_WOLFSSL
    if (ssl)
    {
        wolfSSL_free(ssl);
    }
#endif

#ifdef WITH_WOLFSSL
    if (ctx)
    {
        wolfSSL_CTX_free(ctx);
    }
#endif

    if (client_fd != -1)
    {
        if (close(client_fd) != 0)
        {
            fprintf(stderr, "%s %s: close fd=%d: %s\n", "W", TAG, client_fd, strerror(errno));
        }
    }

    if (ais)
    {
        freeaddrinfo(ais);
    }
}
