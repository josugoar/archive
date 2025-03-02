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
#include <wolfssl/wolfcrypt/settings.h>
#include <wolfssl/certs_test.h>
#include <wolfssl/ssl.h>

#undef DEBUG_WOLFSSL
#define NAME "localhost"
#define SERVICE "1025"
#define CTX_CA_CERT ca_cert_der_1024
#define BUF_LEN 1200

static const char *TAG = "tls_client";

void wolfSSL_log(int log_level, const char *log_message)
{
    (void)log_level;

    fprintf(stderr, "V %s: %s\n", "wolfssl", log_message);
}

int main(void)
{
    struct addrinfo *ais = NULL;
    int client_fd = -1;
    WOLFSSL_CTX *ctx = NULL;
    WOLFSSL *ssl = NULL;

    int errnum = 0;

#ifdef DEBUG_WOLFSSL
    errnum = wolfSSL_Debugging_ON();
    if (errnum != 0)
    {
        fprintf(stderr, "V %s: wolfSSL_Debugging_ON: %s\n", TAG, wolfSSL_ERR_reason_error_string(errnum));
    }

    errnum = wolfSSL_SetLoggingCb(wolfSSL_log);
    if (errnum != 0)
    {
        fprintf(stderr, "V %s: wolfSSL_SetLoggingCb: %s\n", TAG, wolfSSL_ERR_reason_error_string(errnum));
    }
#endif

    struct addrinfo req = {
        .ai_family = AF_UNSPEC,
        .ai_socktype = SOCK_STREAM,
        .ai_protocol = 0,
    };

    errnum = getaddrinfo(NAME, SERVICE, &req, &ais);
    if (errnum != 0)
    {
        fprintf(stderr, "E %s: getaddrinfo: %d\n", TAG, errnum);
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
                fprintf(stderr, "E %s: socket: %s\n", TAG, strerror(errno));
                goto cleanup;
            }

            fprintf(stderr, "W %s: socket: %s\n", TAG, strerror(errno));

            continue;
        }

        if (connect(fd, ai->ai_addr, ai->ai_addrlen) != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "E %s: connect: %s\n", TAG, strerror(errno));
                goto cleanup;
            }

            fprintf(stderr, "W %s: connect: %s\n", TAG, strerror(errno));

            if (close(fd) != 0)
            {
                fprintf(stderr, "W %s: close: %s\n", TAG, strerror(errno));
            }
            fd = -1;

            continue;
        }

        client_fd = fd;

        memcpy(&remote_addr, ai->ai_addr, ai->ai_addrlen);
        remote_addr_len = ai->ai_addrlen;

        break;
    }

    if (getsockname(client_fd, (struct sockaddr *)&local_addr, &local_addr_len) != 0)
    {
        fprintf(stderr, "E %s: getsockname: %s\n", TAG, strerror(errno));
        goto cleanup;
    }

    char local_host[NI_MAXHOST] = "";
    char local_serv[NI_MAXSERV] = "";

    if (getnameinfo((struct sockaddr *)&local_addr, local_addr_len, local_host, sizeof(local_host), local_serv, sizeof(local_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
    {
        fprintf(stderr, "W %s: getnameinfo: %s\n", TAG, strerror(errno));
    }

    fprintf(stderr, "I %s: local_host=%s local_serv=%s\n", TAG, local_host, local_serv);

    char remote_host[NI_MAXHOST] = "";
    char remote_serv[NI_MAXSERV] = "";

    if (getnameinfo((struct sockaddr *)&remote_addr, remote_addr_len, remote_host, sizeof(remote_host), remote_serv, sizeof(remote_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
    {
        fprintf(stderr, "W %s: getnameinfo: %s\n", TAG, strerror(errno));
    }

    fprintf(stderr, "I %s: remote_host=%s remote_serv=%s\n", TAG, remote_host, remote_serv);

    ctx = wolfSSL_CTX_new(wolfTLSv1_3_client_method());
    if (!ctx)
    {
        fprintf(stderr, "E %s: wolfSSL_CTX_new\n", TAG);
        goto cleanup;
    }

    errnum = wolfSSL_CTX_load_verify_buffer(ctx, CTX_CA_CERT, sizeof(CTX_CA_CERT), WOLFSSL_FILETYPE_ASN1);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "E %s: wolfSSL_CTX_load_verify_buffer: %s\n", TAG, wolfSSL_ERR_reason_error_string(errnum));
        goto cleanup;
    }

    ssl = wolfSSL_new(ctx);
    if (!ssl)
    {
        fprintf(stderr, "E %s: wolfSSL_new\n", TAG);
        goto cleanup;
    }

    errnum = wolfSSL_set_fd(ssl, client_fd);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "E %s: wolfSSL_set_fd: %s\n", TAG, wolfSSL_ERR_reason_error_string(errnum));
        goto cleanup;
    }

    errnum = wolfSSL_connect(ssl);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "E %s: wolfSSL_connect: %s\n", TAG, wolfSSL_ERR_reason_error_string(wolfSSL_get_error(ssl, errnum)));
        goto cleanup;
    }

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
            fprintf(stderr, "E %s: fd=%d >= FD_SETSIZE\n", TAG, fd);
            goto cleanup;
        }

        if (fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK) != 0)
        {
            fprintf(stderr, "E %s: fcntl: %s\n", TAG, strerror(errno));
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

            fprintf(stderr, "E %s: select: %s\n", TAG, strerror(errno));
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

                    fprintf(stderr, "E %s: read: %s\n", TAG, strerror(errno));
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

                ssize_t n = wolfSSL_write(ssl, buf + nwrite, nread - nwrite);
                if (n == -1)
                {
                    if (wolfSSL_get_error(ssl, n) == EAGAIN || wolfSSL_get_error(ssl, n) == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "E %s: wolfSSL_write: %s\n", TAG, wolfSSL_ERR_reason_error_string(wolfSSL_get_error(ssl, n)));
                    goto cleanup;
                }

                nwrite += n;
            }
        }
    }

    if (wolfSSL_shutdown(ssl) != 0)
    {
        fprintf(stderr, "W %s: wolfSSL_shutdown: %s\n", TAG, client_fd, wolfSSL_ERR_reason_error_string(wolfSSL_get_error(ssl, WOLFSSL_FATAL_ERROR)));
    }

cleanup:
    if (ssl)
    {
        wolfSSL_free(ssl);
        ssl = NULL;
    }

    if (ctx)
    {
        wolfSSL_CTX_free(ctx);
        ctx = NULL;
    }

    if (client_fd != -1)
    {
        if (close(client_fd) != 0)
        {
            fprintf(stderr, "W %s: close fd=%d: %s\n", TAG, client_fd, strerror(errno));
        }
        client_fd = -1;
    }

    if (ais)
    {
        freeaddrinfo(ais);
        ais = NULL;
    }
}
