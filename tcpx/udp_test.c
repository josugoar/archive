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

bool fd_set_isset(fd_set *fdset)
{
    for (int fd = 0; fd < FD_SETSIZE; ++fd)
    {
        if (FD_ISSET(fd, fdset) != 0)
        {
            return true;
        }
    }

    return false;
}

void app_main(void)
{
    int errnum = 0;

    struct addrinfo req = {
        .ai_family = AF_UNSPEC,
        .ai_socktype = SOCK_DGRAM,
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

        // TODO: select EINPROGRESS
        errno = 0;
        connect(fd, ai->ai_addr, ai->ai_addrlen);
        errnum = errno;
        if (errnum != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "connect: %s\n", strerror(errnum));

                goto fd_cleanup;
            }

            close(fd);

            continue;
        }

        break;
    }

    int readfd = STDIN_FILENO;

    errno = 0;
    fcntl(readfd, F_SETFL, fcntl(readfd, F_GETFL) | O_NONBLOCK);
    errnum = errno;
    if (errnum != 0)
    {
        fprintf(stderr, "fcntl: %s\n", strerror(errnum));

        goto fd_cleanup;
    }

    int writefd = fd;

    errno = 0;
    fcntl(writefd, F_SETFL, fcntl(writefd, F_GETFL) | O_NONBLOCK);
    errnum = errno;
    if (errnum != 0)
    {
        fprintf(stderr, "fcntl: %s\n", strerror(errnum));

        goto fd_cleanup;
    }

    int nfds = readfd > writefd ? readfd : writefd;
    errnum = nfds > FD_SETSIZE ? EINVAL : 0;
    if (errnum != 0)
    {
        fprintf(stderr, "select: %s\n", strerror(errnum));

        goto fd_cleanup;
    }

    fd_set next_readfds = {0};
    FD_ZERO(&next_readfds);
    FD_SET(readfd, &next_readfds);

    fd_set next_writefds = {0};
    FD_ZERO(&next_writefds);

    unsigned char buf[N] = {0};

    size_t nread = 0;
    size_t nwrite = 0;

    bool eof = false;

    while (fd_set_isset(&next_readfds) || fd_set_isset(&next_writefds))
    {
        fd_set curr_readfds = next_readfds;
        fd_set curr_writefds = next_writefds;

        errno = 0;
        int set_nfds = select(nfds + 1, &curr_readfds, &curr_writefds, NULL, NULL);
        errnum = errno;
        if (errnum != 0)
        {
            fprintf(stderr, "select: %s\n", strerror(errnum));

            goto fd_cleanup;
        }

        for (int fd = 0; fd < nfds && set_nfds > 0; ++fd)
        {
            if (FD_ISSET(fd, &curr_readfds) != 0)
            {
                --set_nfds;

                while (true)
                {
                    errno = 0;
                    ssize_t n = read(readfd, buf + nread, sizeof(buf) - nread);
                    errnum = errno;
                    if (errnum != 0)
                    {
                        if (errnum == EAGAIN || errnum == EWOULDBLOCK)
                        {
                            break;
                        }

                        fprintf(stderr, "read: %s\n", strerror(errnum));

                        goto fd_cleanup;
                    }

                    if (n == 0)
                    {
                        eof = true;

                        FD_CLR(readfd, &next_readfds);

                        break;
                    }

                    nread += n;

                    FD_SET(writefd, &next_writefds);

                    if (nread == sizeof(buf))
                    {
                        FD_CLR(readfd, &next_readfds);

                        break;
                    }
                }
            }

            if (FD_ISSET(fd, &curr_writefds) != 0)
            {
                --set_nfds;

                while (true)
                {
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

                        goto fd_cleanup;
                    }

                    nwrite += n;

                    if (nwrite == nread)
                    {
                        nread = 0;
                        nwrite = 0;

                        if (!eof)
                        {
                            FD_SET(readfd, &next_readfds);
                        }

                        FD_CLR(writefd, &next_writefds);

                        break;
                    }
                }
            }
        }
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
