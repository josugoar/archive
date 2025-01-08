#include <errno.h>
#include <fcntl.h>
#include <netdb.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <sys/select.h>
#include <sys/socket.h>
#include <unistd.h>

#ifdef ESP_PLATFORM
#include <esp_event.h>
#include <esp_netif.h>
#include <nvs_flash.h>
#include <protocol_examples_common.h>
#endif

#define NAME "localhost"
#define SERVICE "1025"
#define NBUF 1200

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
        fprintf(stderr, "E tcp_server: getaddrinfo name=%s service=%s domain=%d type=%d protocol=%d: %d\n", NAME, SERVICE, req.ai_family, req.ai_socktype, req.ai_protocol, errnum);

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
                fprintf(stderr, "E tcp_server: socket domain=%d type=%d protocol=%d: %s\n", ai->ai_family, ai->ai_socktype, ai->ai_protocol, strerror(errno));

                goto fd_cleanup;
            }

            fprintf(stderr, "W tcp_server: socket domain=%d type=%d protocol=%d: %s\n", ai->ai_family, ai->ai_socktype, ai->ai_protocol, strerror(errno));

            continue;
        }

        if (bind(local_fd, ai->ai_addr, ai->ai_addrlen) != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "E tcp_server: bind fd=%d: %s\n", local_fd, strerror(errno));

                goto fd_cleanup;
            }

            fprintf(stderr, "W tcp_server: bind fd=%d: %s\n", local_fd, strerror(errno));

            if (close(local_fd) != 0)
            {
                fprintf(stderr, "W tcp_server: close fd=%d: %s\n", local_fd, strerror(errno));
            }

            continue;
        }

        fprintf(stderr, "I tcp_server: local_fd=%d\n", local_fd);

        memcpy(&local_addr, ai->ai_addr, ai->ai_addrlen);
        local_addr_len = ai->ai_addrlen;

        break;
    }

    if (listen(local_fd, remote_fds_len) != 0)
    {
        fprintf(stderr, "E tcp_server: listen fd=%d: %s\n", local_fd, strerror(errno));

        goto fd_cleanup;
    }

    char local_host[NI_MAXHOST] = "";
    char local_serv[NI_MAXSERV] = "";

    if (getnameinfo((struct sockaddr *)&local_addr, local_addr_len, local_host, sizeof(local_host), local_serv, sizeof(local_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
    {
        fprintf(stderr, "W tcp_server: getnameinfo local_addr: %s\n", strerror(errno));
    }

    if (*local_host != '\0' && *local_serv != '\0')
    {
        fprintf(stderr, "I tcp_server: local_host=%s local_serv=%s\n", local_host, local_serv);
    }

    int acceptfd = local_fd;

    int *readfds = remote_fds;
    size_t readfds_len = remote_fds_len;

    int writefd = STDOUT_FILENO;

    int fds[] = {acceptfd, writefd};
    size_t fds_len = sizeof(fds) / sizeof(*fds);

    int nfds = -1;

    for (size_t i = 0; i < fds_len; ++i)
    {
        int fd = fds[i];

        if (fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK) != 0)
        {
            fprintf(stderr, "E tcp_server: fcntl fd=%d: %s\n", fd, strerror(errno));

            goto fd_cleanup;
        }

        nfds = nfds > fd ? nfds : fd + 1;
        if (nfds > FD_SETSIZE)
        {
            fprintf(stderr, "E tcp_server: nfds=%d > FD_SETSIZE=%d\n", nfds, FD_SETSIZE);

            goto fd_cleanup;
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

        if (select(nfds, &curr_readfds, &curr_writefds, NULL, NULL) == -1)
        {
            fprintf(stderr, "E tcp_server: select: %s\n", strerror(errno));

            goto fd_cleanup;
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

                    fprintf(stderr, "E tcp_server: accept fd=%d: %s\n", acceptfd, strerror(errno));

                    goto fd_cleanup;
                }

                fprintf(stderr, "I tcp_server: remote_fd=%d\n", remote_fd);

                char remote_host[NI_MAXHOST] = "";
                char remote_serv[NI_MAXSERV] = "";

                if (getnameinfo((struct sockaddr *)&remote_addr, remote_addr_len, remote_host, sizeof(remote_host), remote_serv, sizeof(remote_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
                {
                    fprintf(stderr, "W tcp_server: getnameinfo: %s\n", strerror(errno));
                }

                if (*remote_host != '\0' && *remote_serv != '\0')
                {
                    fprintf(stderr, "I tcp_server: remote_host=%s remote_serv=%s\n", remote_host, remote_serv);
                }

                int readfd = remote_fd;

                if (fcntl(readfd, F_SETFL, fcntl(readfd, F_GETFL) | O_NONBLOCK) != 0)
                {
                    fprintf(stderr, "E tcp_server: fcntl fd=%d: %s\n", readfd, strerror(errno));

                    goto fd_cleanup;
                }

                nfds = nfds > readfd ? nfds : readfd + 1;
                if (nfds > FD_SETSIZE)
                {
                    fprintf(stderr, "E tcp_server: nfds=%d > FD_SETSIZE=%d\n", nfds, FD_SETSIZE);

                    goto fd_cleanup;
                }

                readfds[i] = readfd;

                FD_SET(readfd, &next_readfds);
            }
        }

        for (size_t i = 0; i < readfds_len; ++i)
        {
            int readfd = readfds[i];

            if (FD_ISSET(readfd, &curr_readfds) != 0)
            {
                while (true)
                {
                    if (nread >= sizeof(buf))
                    {
                        FD_CLR(readfd, &next_readfds);

                        break;
                    }

                    ssize_t n = read(readfd, buf + nread, sizeof(buf) - nread);
                    if (n == -1)
                    {
                        if (errno == EAGAIN || errno == EWOULDBLOCK)
                        {
                            break;
                        }

                        fprintf(stderr, "E tcp_server: read fd=%d: %s\n", readfd, strerror(errno));

                        goto fd_cleanup;
                    }

                    if (n == 0)
                    {
                        if (close(readfd) != 0)
                        {
                            fprintf(stderr, "W tcp_server: close fd=%d: %s\n", readfd, strerror(errno));
                        }

                        readfds[i] = -1;

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

                    fprintf(stderr, "E tcp_server: write fd=%d: %s\n", writefd, strerror(errno));

                    goto fd_cleanup;
                }

                nwrite += n;
            }
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
            fprintf(stderr, "W tcp_server: close fd=%d: %s\n", remote_fd, strerror(errno));
        }
    }

    if (local_fd != -1)
    {
        if (close(local_fd) != 0)
        {
            fprintf(stderr, "W tcp_server: close fd=%d: %s\n", local_fd, strerror(errno));
        }
    }

ais_cleanup:
    if (ais)
    {
        freeaddrinfo(ais);
    }
}
