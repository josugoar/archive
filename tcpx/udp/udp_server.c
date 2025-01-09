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

static const char *TAG = "udp_server";

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
        .ai_socktype = SOCK_DGRAM,
        .ai_protocol = 0,
    };

    struct addrinfo *ais = NULL;

    errnum = getaddrinfo(NAME, SERVICE, &req, &ais);
    if (errnum != 0)
    {
        fprintf(stderr, "E %s: getaddrinfo name=%s service=%s domain=%d type=%d protocol=%d: %d\n", TAG, NAME, SERVICE, req.ai_family, req.ai_socktype, req.ai_protocol, errnum);

        goto ais_cleanup;
    }

    int local_fd = -1;

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
                fprintf(stderr, "E %s: socket domain=%d type=%d protocol=%d: %s\n", TAG, ai->ai_family, ai->ai_socktype, ai->ai_protocol, strerror(errno));

                goto fd_cleanup;
            }

            fprintf(stderr, "W %s: socket domain=%d type=%d protocol=%d: %s\n", TAG, ai->ai_family, ai->ai_socktype, ai->ai_protocol, strerror(errno));

            continue;
        }

        if (bind(local_fd, ai->ai_addr, ai->ai_addrlen) != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "E %s: bind fd=%d: %s\n", TAG, local_fd, strerror(errno));

                goto fd_cleanup;
            }

            fprintf(stderr, "W %s: bind fd=%d: %s\n", TAG, local_fd, strerror(errno));

            if (close(local_fd) != 0)
            {
                fprintf(stderr, "W %s: close fd=%d: %s\n", TAG, local_fd, strerror(errno));
            }

            continue;
        }

        fprintf(stderr, "I %s: local_fd=%d\n", TAG, local_fd);

        memcpy(&local_addr, ai->ai_addr, ai->ai_addrlen);
        local_addr_len = ai->ai_addrlen;

        break;
    }

    char local_host[NI_MAXHOST] = "";
    char local_serv[NI_MAXSERV] = "";

    if (getnameinfo((struct sockaddr *)&local_addr, local_addr_len, local_host, sizeof(local_host), local_serv, sizeof(local_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
    {
        fprintf(stderr, "W %s: getnameinfo local_addr: %s\n", TAG, strerror(errno));
    }

    if (*local_host != '\0' && *local_serv != '\0')
    {
        fprintf(stderr, "I %s: local_host=%s local_serv=%s\n", TAG, local_host, local_serv);
    }

    int readfd = local_fd;

    int writefd = STDOUT_FILENO;

    int fds[] = {readfd, writefd};
    size_t fds_len = sizeof(fds) / sizeof(*fds);

    int next_nfds = -1;

    for (size_t i = 0; i < fds_len; ++i)
    {
        int fd = fds[i];

        if (fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK) != 0)
        {
            fprintf(stderr, "E %s: fcntl fd=%d: %s\n", TAG, fd, strerror(errno));

            goto fd_cleanup;
        }

        next_nfds = next_nfds > fd ? next_nfds : fd + 1;
        if (next_nfds > FD_SETSIZE)
        {
            fprintf(stderr, "E %s: nfds=%d > FD_SETSIZE=%d\n", TAG, next_nfds, FD_SETSIZE);

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

    FD_SET(readfd, &next_readfds);

    while (true)
    {
        fd_set curr_readfds = next_readfds;
        fd_set curr_writefds = next_writefds;

        int curr_nfds = select(next_nfds, &curr_readfds, &curr_writefds, NULL, NULL);
        if (curr_nfds == -1)
        {
            fprintf(stderr, "E %s: select: %s\n", TAG, strerror(errno));

            goto fd_cleanup;
        }

        if (FD_ISSET(readfd, &curr_readfds) != 0)
        {
            while (true)
            {
                if (nread >= sizeof(buf))
                {
                    FD_CLR(readfd, &next_readfds);

                    break;
                }

                ssize_t n = recvfrom(readfd, buf + nread, sizeof(buf) - nread, 0, (struct sockaddr *)&remote_addr, &remote_addr_len);
                if (n == -1)
                {
                    if (errno == EAGAIN || errno == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "E %s: recvfrom fd=%d: %s\n", TAG, readfd, strerror(errno));

                    goto fd_cleanup;
                }

                char remote_host[NI_MAXHOST] = "";
                char remote_serv[NI_MAXSERV] = "";

                if (getnameinfo((struct sockaddr *)&remote_addr, remote_addr_len, remote_host, sizeof(remote_host), remote_serv, sizeof(remote_serv), NI_NUMERICHOST | NI_NUMERICSERV) != 0)
                {
                    fprintf(stderr, "W %s: getnameinfo remote_addr: %s\n", TAG, strerror(errno));
                }

                if (*remote_host != '\0' && *remote_serv != '\0')
                {
                    fprintf(stderr, "I %s: remote_host=%s remote_serv=%s\n", TAG, remote_host, remote_serv);
                }

                nread += n;

                FD_SET(writefd, &next_writefds);
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

                    FD_SET(readfd, &next_readfds);

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

                    fprintf(stderr, "E %s: write fd=%d: %s\n", TAG, writefd, strerror(errno));

                    goto fd_cleanup;
                }

                nwrite += n;
            }
        }
    }

fd_cleanup:
    if (local_fd != -1)
    {
        if (close(local_fd) != 0)
        {
            fprintf(stderr, "W %s: close fd=%d: %s\n", TAG, local_fd, strerror(errno));
        }
    }

ais_cleanup:
    if (ais)
    {
        freeaddrinfo(ais);
    }
}
