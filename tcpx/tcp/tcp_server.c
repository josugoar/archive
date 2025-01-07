#include <assert.h>
#include <errno.h>
#include <fcntl.h>
#include <netdb.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <sys/select.h>
#include <sys/socket.h>
#include <unistd.h>

#define NAME "localhost"
#define SERVICE "1025"
#define N 1200

void app_main(void)
{
    int errnum = 0;

    struct addrinfo req = {
        .ai_flags = AI_PASSIVE,
        .ai_family = AF_UNSPEC,
        .ai_socktype = SOCK_STREAM,
    };

    struct addrinfo *ais = NULL;

    errnum = getaddrinfo(NAME, SERVICE, &req, &ais);
    if (errnum != 0)
    {
        fprintf(stderr, "getaddrinfo: %d\n", errnum);

        goto addrinfo_cleanup;
    }

    int fd = -1;

    for (struct addrinfo *ai = ais; ai; ai = ai->ai_next)
    {
        errno = 0;
        fd = socket(ai->ai_family, ai->ai_socktype, ai->ai_protocol);
        errnum = errno;
        if (errnum != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "socket: %s\n", strerror(errnum));

                goto fd_cleanup;
            }

            continue;
        }

        errno = 0;
        bind(fd, ai->ai_addr, ai->ai_addrlen);
        errnum = errno;
        if (errnum != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "bind: %s\n", strerror(errnum));

                goto fd_cleanup;
            }

            close(fd);

            continue;
        }

        char host[NI_MAXHOST] = "";
        char serv[NI_MAXSERV] = "";

        errno = 0;
        getnameinfo(ai->ai_addr, ai->ai_addrlen, host, sizeof(host), serv, sizeof(serv), NI_NUMERICHOST | NI_NUMERICSERV);
        errnum = errno;
        if (errnum != 0)
        {
            fprintf(stderr, "getnameinfo: %s\n", strerror(errnum));
        }
        else
        {
            fprintf(stderr, "getnameinfo: host=%s, serv=%s\n", host, serv);
        }

        break;
    }

    errno = 0;
    listen(fd, SOMAXCONN);
    errnum = errno;
    if (errnum != 0)
    {
        fprintf(stderr, "listen: %s\n", strerror(errnum));

        goto fd_cleanup;
    }

    int local_fd = fd;

    errno = 0;
    fcntl(local_fd, F_SETFL, fcntl(local_fd, F_GETFL) | O_NONBLOCK);
    errnum = errno;
    if (errnum != 0)
    {
        fprintf(stderr, "fcntl: %s\n", strerror(errnum));

        goto conn_cleanup;
    }

    int remote_fds[SOMAXCONN] = {0};

    for (size_t i = 0; i < SOMAXCONN; ++i)
    {
        remote_fds[i] = -1;
    }

    int writefd = STDOUT_FILENO;

    errno = 0;
    fcntl(writefd, F_SETFL, fcntl(writefd, F_GETFL) | O_NONBLOCK);
    errnum = errno;
    if (errnum != 0)
    {
        fprintf(stderr, "fcntl: %s\n", strerror(errnum));

        goto conn_cleanup;
    }

    int nfds = local_fd > writefd ? local_fd + 1 : writefd + 1;
    errnum = nfds > FD_SETSIZE ? EINVAL : 0;
    if (errnum != 0)
    {
        fprintf(stderr, "select: %s\n", strerror(errnum));

        goto conn_cleanup;
    }

    fd_set next_readfds = {0};
    FD_ZERO(&next_readfds);
    FD_SET(local_fd, &next_readfds);

    fd_set next_writefds = {0};
    FD_ZERO(&next_writefds);

    unsigned char buf[N] = {0};

    size_t nread = 0;
    size_t nwrite = 0;

    while (true)
    {
        fd_set curr_readfds = next_readfds;
        fd_set curr_writefds = next_writefds;

        errno = 0;
        select(nfds, &curr_readfds, &curr_writefds, NULL, NULL);
        errnum = errno;
        if (errnum != 0)
        {
            fprintf(stderr, "select: %s\n", strerror(errnum));

            goto conn_cleanup;
        }

        if (FD_ISSET(local_fd, &curr_readfds) != 0)
        {
            while (true)
            {
                size_t i = 0;

                for (; i < SOMAXCONN; ++i)
                {
                    int remote_fd = remote_fds[i];

                    if (remote_fd == -1)
                    {
                        break;
                    }
                }

                assert(i < SOMAXCONN);

                errno = 0;
                int remote_fd = accept(local_fd, NULL, NULL);
                errnum = errno;
                if (errnum != 0)
                {
                    if (errnum == EAGAIN || errnum == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "accept: %s\n", strerror(errnum));

                    goto conn_cleanup;
                }

                errno = 0;
                fcntl(remote_fd, F_SETFL, fcntl(remote_fd, F_GETFL) | O_NONBLOCK);
                errnum = errno;
                if (errnum != 0)
                {
                    fprintf(stderr, "fcntl: %s\n", strerror(errnum));

                    goto conn_cleanup;
                }

                nfds = nfds > remote_fd ? nfds : remote_fd + 1;
                errnum = nfds > FD_SETSIZE ? EINVAL : 0;
                if (errnum != 0)
                {
                    fprintf(stderr, "select: %s\n", strerror(errnum));

                    goto conn_cleanup;
                }

                remote_fds[i] = remote_fd;

                FD_SET(remote_fd, &next_readfds);

                if (i >= SOMAXCONN - 1)
                {
                    FD_CLR(local_fd, &next_readfds);

                    break;
                }
            }
        }

        for (size_t i = 0; i < SOMAXCONN; ++i)
        {
            int remote_fd = remote_fds[i];

            if (remote_fd == -1)
            {
                continue;
            }

            if (FD_ISSET(remote_fd, &curr_readfds) != 0)
            {
                while (true)
                {
                    assert(sizeof(buf) - nread > 0);

                    errno = 0;
                    ssize_t n = read(remote_fd, buf + nread, sizeof(buf) - nread);
                    errnum = errno;
                    if (errnum != 0)
                    {
                        if (errnum == EAGAIN || errnum == EWOULDBLOCK)
                        {
                            break;
                        }

                        fprintf(stderr, "read: %s\n", strerror(errnum));

                        goto conn_cleanup;
                    }

                    if (n == 0)
                    {
                        shutdown(remote_fd, SHUT_RDWR);

                        close(remote_fd);

                        remote_fds[i] = -1;

                        FD_SET(local_fd, &next_readfds);

                        FD_CLR(remote_fd, &next_readfds);

                        break;
                    }

                    nread += n;

                    FD_SET(writefd, &next_writefds);

                    if (nread >= sizeof(buf))
                    {
                        FD_CLR(remote_fd, &next_readfds);

                        break;
                    }
                }

                if (FD_ISSET(writefd, &next_writefds) != 0)
                {
                    for (size_t j = 0; j < SOMAXCONN; ++j)
                    {
                        int remote_fd_this = remote_fds[i];
                        int remote_fd_other = remote_fds[j];

                        if (remote_fd_other == -1)
                        {
                            continue;
                        }

                        if (remote_fd_other == remote_fd_this)
                        {
                            continue;
                        }

                        FD_CLR(remote_fd_other, &next_readfds);
                    }

                    break;
                }
            }
        }

        if (FD_ISSET(writefd, &curr_writefds) != 0)
        {
            while (true)
            {
                assert(nread - nwrite > 0);

                errno = 0;
                ssize_t n = write(writefd, buf + nwrite, nread - nwrite);
                errnum = errno;
                if (errnum != 0)
                {
                    if (errnum == EAGAIN || errnum == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "write: %s\n", strerror(errnum));

                    goto conn_cleanup;
                }

                nwrite += n;

                if (nwrite >= nread)
                {
                    nread = 0;
                    nwrite = 0;

                    for (size_t i = 0; i < SOMAXCONN; ++i)
                    {
                        int remote_fd = remote_fds[i];

                        if (remote_fd == -1)
                        {
                            continue;
                        }

                        FD_SET(remote_fd, &next_readfds);
                    }

                    FD_CLR(writefd, &next_writefds);

                    break;
                }
            }
        }
    }

conn_cleanup:
    for (size_t i = 0; i < SOMAXCONN; ++i)
    {
        int remote_fd = remote_fds[i];
        close(remote_fd);
    }

fd_cleanup:
    close(fd);

addrinfo_cleanup:
    freeaddrinfo(ais);
}

int main(void)
{
    app_main();
}
