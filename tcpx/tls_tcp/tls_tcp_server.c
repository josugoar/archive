#define USE_CERT_BUFFERS_1024

#include <errno.h>
#include <fcntl.h>
#include <netdb.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <sys/select.h>
#include <sys/socket.h>
#include <unistd.h>
#include <wolfssl/certs_test.h>
#include <wolfssl/ssl.h>

#ifdef ESP_PLATFORM
#include <esp_event.h>
#include <esp_netif.h>
#include <nvs_flash.h>
#include <protocol_examples_common.h>
#endif

#define NDEBUG_WOLFSSL
#define SERVER_CERT server_cert_der_1024
#define SIZEOF_SERVER_CERT sizeof_server_cert_der_1024
#define SERVER_KEY server_key_der_1024
#define SIZEOF_SERVER_KEY sizeof_server_key_der_1024
#define NAME "localhost"
#define SERVICE "1025"
#define NBUF 1200

void wolfSSL_log_printf(int log_level, const char *log_message)
{
    (void)log_level;

    fprintf(stderr, "V wolfSSL: %s\n", log_message);
}

#ifdef ESP_PLATFORM
void app_main(void)
#else
int main(void)
#endif
{
    int errnum = 0;

#ifdef ESP_PLATFORM
    ESP_ERROR_CHECK(nvs_flash_init());
    ESP_ERROR_CHECK(esp_netif_init());
    ESP_ERROR_CHECK(esp_event_loop_create_default());
    ESP_ERROR_CHECK(example_connect());
#endif

#if defined(DEBUG_WOLFSSL) && !defined(NDEBUG_WOLFSSL)
    errnum = wolfSSL_Debugging_ON();
    if (errnum != 0)
    {
        fprintf(stderr, "V tls_tls_tcp_server: wolfSSL_Debugging_ON: %s\n", wolfSSL_ERR_reason_error_string(errnum));
    }

    errnum = wolfSSL_SetLoggingCb(wolfSSL_log_printf);
    if (errnum != 0)
    {
        fprintf(stderr, "V tls_tls_tcp_server: wolfSSL_SetLoggingCb: %s\n", wolfSSL_ERR_reason_error_string(errnum));
    }
#endif

    WOLFSSL_CTX *ctx = wolfSSL_CTX_new(wolfTLS_server_method());
    if (!ctx)
    {
        fprintf(stderr, "E tls_tls_tcp_server: wolfSSL_CTX_new");

        goto ctx_cleanup;
    }

    errnum = wolfSSL_CTX_use_certificate_chain_buffer_format(ctx, SERVER_CERT, SIZEOF_SERVER_CERT, WOLFSSL_FILETYPE_ASN1);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "E tls_tls_tcp_server: wolfSSL_CTX_use_certificate_chain_buffer_format: %s\n", wolfSSL_ERR_reason_error_string(errnum));

        goto ctx_cleanup;
    }

    errnum = wolfSSL_CTX_use_PrivateKey_buffer(ctx, SERVER_KEY, SIZEOF_SERVER_KEY, WOLFSSL_FILETYPE_ASN1);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "E tls_tls_tcp_server: wolfSSL_CTX_use_PrivateKey_buffer: %s\n", wolfSSL_ERR_reason_error_string(errnum));

        goto ctx_cleanup;
    }

    errnum = wolfSSL_CTX_check_private_key(ctx);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "E tls_tls_tcp_server: wolfSSL_CTX_check_private_key: %s\n", wolfSSL_ERR_reason_error_string(errnum));

        goto ctx_cleanup;
    }

    struct addrinfo req = {
        .ai_flags = AI_PASSIVE,
        .ai_family = AF_UNSPEC,
        .ai_socktype = SOCK_STREAM,
        .ai_protocol = 0,
    };

    struct addrinfo *ais = NULL;

    errnum = getaddrinfo(NAME, SERVICE, &req, &ais);
    if (errnum != 0)
    {
        fprintf(stderr, "E tls_tcp_server: getaddrinfo name=%s service=%s domain=%d type=%d protocol=%d: %d\n", NAME, SERVICE, req.ai_family, req.ai_socktype, req.ai_protocol, errnum);

        goto ais_cleanup;
    }

    int local_fd = -1;

    int remote_fds[SOMAXCONN] = {0};
    size_t remote_fds_len = sizeof(remote_fds) / sizeof(*remote_fds);

    for (size_t i = 0; i < remote_fds_len; ++i)
    {
        remote_fds[i] = -1;
    }

    struct sockaddr_storage local_addr = {0};
    socklen_t local_addr_len = sizeof(local_addr);

    struct sockaddr_storage remote_addr = {0};
    socklen_t remote_addr_len = sizeof(remote_addr);

    for (struct addrinfo *ai = ais; ai; ai = ai->ai_next)
    {
        local_fd = socket(ai->ai_family, ai->ai_socktype, ai->ai_protocol);
        if (local_fd == -1)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "E tls_tcp_server: socket domain=%d type=%d protocol=%d: %s\n", ai->ai_family, ai->ai_socktype, ai->ai_protocol, strerror(errno));

                goto fd_cleanup;
            }

            fprintf(stderr, "W tls_tcp_server: socket domain=%d type=%d protocol=%d: %s\n", ai->ai_family, ai->ai_socktype, ai->ai_protocol, strerror(errno));

            continue;
        }

        if (bind(local_fd, ai->ai_addr, ai->ai_addrlen) != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "E tls_tcp_server: bind fd=%d: %s\n", local_fd, strerror(errno));

                goto fd_cleanup;
            }

            fprintf(stderr, "W tls_tcp_server: bind fd=%d: %s\n", local_fd, strerror(errno));

            if (close(local_fd) != 0)
            {
                fprintf(stderr, "W tls_tcp_server: close fd=%d: %s\n", local_fd, strerror(errno));
            }

            continue;
        }

        fprintf(stderr, "I tls_tcp_server: local_fd=%d\n", local_fd);

        memcpy(&local_addr, ai->ai_addr, ai->ai_addrlen);
        local_addr_len = ai->ai_addrlen;

        break;
    }

    if (listen(local_fd, remote_fds_len) != 0)
    {
        fprintf(stderr, "E tls_tcp_server: listen fd=%d: %s\n", local_fd, strerror(errno));

        goto fd_cleanup;
    }

    char local_host[NI_MAXHOST] = "";
    char local_serv[NI_MAXSERV] = "";

    if (getnameinfo((struct sockaddr *)&local_addr, local_addr_len, local_host, sizeof(local_host), local_serv, sizeof(local_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
    {
        fprintf(stderr, "W tls_tcp_server: getnameinfo local_addr: %s\n", strerror(errno));
    }

    if (*local_host != '\0' && *local_serv != '\0')
    {
        fprintf(stderr, "I tls_tcp_server: local_host=%s local_serv=%s\n", local_host, local_serv);
    }

    WOLFSSL *ssls[SOMAXCONN] = {0};
    size_t ssls_len = sizeof(ssls) / sizeof(*ssls);

    int acceptfd = local_fd;

    int *readfds = remote_fds;
    size_t readfds_len = remote_fds_len;

    int writefd = STDOUT_FILENO;

    int fds[] = {acceptfd, writefd};
    size_t fds_len = sizeof(fds) / sizeof(*fds);

    int next_nfds = -1;

    for (size_t i = 0; i < fds_len; ++i)
    {
        int fd = fds[i];

        if (fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK) != 0)
        {
            fprintf(stderr, "E tls_tcp_server: fcntl fd=%d: %s\n", fd, strerror(errno));

            goto ssl_cleanup;
        }

        next_nfds = next_nfds > fd ? next_nfds : fd + 1;
        if (next_nfds > FD_SETSIZE)
        {
            fprintf(stderr, "E tls_tcp_server: nfds=%d > FD_SETSIZE=%d\n", next_nfds, FD_SETSIZE);

            goto ssl_cleanup;
        }
    }

    fd_set next_readfds = {0};
    FD_ZERO(&next_readfds);

    fd_set next_writefds = {0};
    FD_ZERO(&next_writefds);

    unsigned char buf[NBUF] = {0};

    size_t nread = 0;
    size_t nwrite = 0;

    FD_SET(acceptfd, &next_readfds);

    while (true)
    {
        fd_set curr_readfds = next_readfds;
        fd_set curr_writefds = next_writefds;

        int curr_nfds = select(next_nfds, &curr_readfds, &curr_writefds, NULL, NULL);
        if (curr_nfds == -1)
        {
            fprintf(stderr, "E tls_tcp_server: select: %s\n", strerror(errno));

            goto ssl_cleanup;
        }

        if (FD_ISSET(acceptfd, &curr_readfds) != 0)
        {
            while (true)
            {
                size_t i = 0;

                for (; i < remote_fds_len; ++i)
                {
                    int remote_fd = remote_fds[i];

                    if (remote_fd == -1)
                    {
                        break;
                    }
                }

                if (i >= remote_fds_len)
                {
                    FD_CLR(acceptfd, &next_readfds);

                    break;
                }

                int remote_fd = accept(acceptfd, (struct sockaddr *)&remote_addr, &remote_addr_len);
                if (remote_fd == -1)
                {
                    if (errno == EAGAIN || errno == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "E tls_tcp_server: accept fd=%d: %s\n", acceptfd, strerror(errno));

                    goto ssl_cleanup;
                }

                fprintf(stderr, "I tls_tcp_server: remote_fd=%d\n", remote_fd);

                char remote_host[NI_MAXHOST] = "";
                char remote_serv[NI_MAXSERV] = "";

                if (getnameinfo((struct sockaddr *)&remote_addr, remote_addr_len, remote_host, sizeof(remote_host), remote_serv, sizeof(remote_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
                {
                    fprintf(stderr, "W tls_tcp_server: getnameinfo: %s\n", strerror(errno));
                }

                if (*remote_host != '\0' && *remote_serv != '\0')
                {
                    fprintf(stderr, "I tls_tcp_server: remote_host=%s remote_serv=%s\n", remote_host, remote_serv);
                }

                WOLFSSL *ssl = wolfSSL_new(ctx);
                if (!ssl)
                {
                    fprintf(stderr, "E tls_tcp_server: wolfSSL_new\n");

                    goto ssl_cleanup;
                }

                errnum = wolfSSL_set_fd(ssl, remote_fd);
                if (errnum != WOLFSSL_SUCCESS)
                {
                    fprintf(stderr, "E tls_tcp_server: wolfSSL_set_fd fd=%d: %s\n", local_fd, wolfSSL_ERR_reason_error_string(errnum));

                    goto ssl_cleanup;
                }

                errnum = wolfSSL_accept(ssl);
                if (errnum != WOLFSSL_SUCCESS)
                {
                    fprintf(stderr, "E tls_tcp_server: wolfSSL_connect: %s\n", wolfSSL_ERR_reason_error_string(wolfSSL_get_error(ssl, errnum)));

                    goto ssl_cleanup;
                }

                int readfd = remote_fd;

                if (fcntl(readfd, F_SETFL, fcntl(readfd, F_GETFL) | O_NONBLOCK) != 0)
                {
                    fprintf(stderr, "E tls_tcp_server: fcntl fd=%d: %s\n", readfd, strerror(errno));

                    goto ssl_cleanup;
                }

                next_nfds = next_nfds > readfd ? next_nfds : readfd + 1;
                if (next_nfds > FD_SETSIZE)
                {
                    fprintf(stderr, "E tls_tcp_server: nfds=%d > FD_SETSIZE=%d\n", next_nfds, FD_SETSIZE);

                    goto ssl_cleanup;
                }

                readfds[i] = readfd;

                ssls[i] = ssl;

                FD_SET(readfd, &next_readfds);
            }
        }

        for (size_t i = 0; i < readfds_len; ++i)
        {
            int readfd = readfds[i];

            WOLFSSL *ssl = ssls[i];

            if (FD_ISSET(readfd, &curr_readfds) != 0)
            {
                while (true)
                {
                    if (nread >= sizeof(buf))
                    {
                        FD_CLR(readfd, &next_readfds);

                        break;
                    }

                    ssize_t n = wolfSSL_read(ssl, buf + nread, sizeof(buf) - nread);
                    if (n == -1)
                    {
                        errnum = wolfSSL_get_error(ssl, n);

                        if (errnum == WOLFSSL_ERROR_WANT_READ || errnum == WOLFSSL_ERROR_WANT_WRITE)
                        {
                            break;
                        }

                        fprintf(stderr, "E tls_tcp_server: wolfSSL_read: %s\n", wolfSSL_ERR_reason_error_string(errnum));

                        goto ssl_cleanup;
                    }

                    if (n == 0)
                    {
                        wolfSSL_free(ssl);

                        if (close(readfd) != 0)
                        {
                            fprintf(stderr, "W tls_tcp_server: close fd=%d: %s\n", readfd, strerror(errno));
                        }

                        readfds[i] = -1;

                        ssls[i] = NULL;

                        FD_SET(acceptfd, &next_readfds);

                        FD_CLR(readfd, &next_readfds);

                        break;
                    }

                    nread += n;

                    FD_SET(writefd, &next_writefds);
                }

                if (FD_ISSET(writefd, &next_writefds) != 0)
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

                        FD_CLR(readfd_other, &next_readfds);
                    }

                    break;
                }
            }
        }

        if (FD_ISSET(writefd, &curr_writefds) != 0)
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

                        FD_SET(readfd, &next_readfds);
                    }

                    FD_CLR(writefd, &next_writefds);

                    break;
                }

                ssize_t n = write(writefd, buf + nwrite, nread - nwrite);
                if (n == -1)
                {
                    if (errno == EAGAIN || errno == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "E tls_tcp_server: write fd=%d: %s\n", writefd, strerror(errno));

                    goto ssl_cleanup;
                }

                nwrite += n;
            }
        }
    }

ssl_cleanup:
    for (size_t i = 0; i < ssls_len; ++i)
    {
        WOLFSSL *ssl = ssls[i];

        if (ssl)
        {
            wolfSSL_free(ssl);
        }
    }

fd_cleanup:
    for (size_t i = 0; i < remote_fds_len; ++i)
    {
        int remote_fd = remote_fds[i];

        if (remote_fd == -1)
        {
            continue;
        }

        if (close(remote_fd) != 0)
        {
            fprintf(stderr, "W tls_tcp_server: close fd=%d: %s\n", remote_fd, strerror(errno));
        }
    }

    if (local_fd != -1)
    {
        if (close(local_fd) != 0)
        {
            fprintf(stderr, "W tls_tcp_server: close fd=%d: %s\n", local_fd, strerror(errno));
        }
    }

ais_cleanup:
    if (ais)
    {
        freeaddrinfo(ais);
    }

ctx_cleanup:
    if (ctx)
    {
        wolfSSL_CTX_free(ctx);
    }
}
