#include <errno.h>
#include <fcntl.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <sys/select.h>
#include <sys/uio.h>
#include <unistd.h>

#define CIRC_SIZE 65536
#define CIRC_CNT_MIN 1
#define CIC_SPACE_MIN 1200

// INFO: if size == exp2(n) then (idx & (size - 1)) == (idx % size)
// EXPLANATION: https://csresources.github.io/SystemProgrammingWiki/SystemProgramming/Synchronization,-Part-8:-Ring-Buffer-Example/

#define CIRC_CNT(head, tail, size) (((head) - (tail)) & ((size) - 1))

#define CIRC_SPACE(head, tail, size) CIRC_CNT((tail), ((head) + 1), (size))

#define CIRC_CNT_TO_END(head, tail, size) \
    ({int end = (size) - (tail); \
	  int n = ((head) + end) & ((size)-1); \
	  n < end ? n : end; })

#define CIRC_SPACE_TO_END(head, tail, size) \
    ({int end = (size) - 1 - (head); \
	  int n = (end + (tail)) & ((size)-1); \
	  n <= end ? n : end+1; })

int main(void)
{
    int read_fd = STDIN_FILENO;
    int write_fd = STDOUT_FILENO;

    int fds[] = {read_fd, write_fd};
    int fds_size = sizeof(fds) / sizeof(*fds);

    int next_nfds = -1;

    for (int i = 0; i < fds_size; ++i)
    {
        int fd = fds[i];

        if (fcntl(fd, F_SETFL, fcntl(fd, F_GETFL) | O_NONBLOCK) != 0)
        {
            fprintf(stderr, "E fcntl: %s\n", strerror(errno));

            goto cleanup;
        }

        next_nfds = next_nfds > fd ? next_nfds : fd + 1;
        if (next_nfds > FD_SETSIZE)
        {
            fprintf(stderr, "E nfds > FD_SETSIZE\n");

            goto cleanup;
        }
    }

    fd_set next_read_fds = {0};
    FD_ZERO(&next_read_fds);

    fd_set next_write_fds = {0};
    FD_ZERO(&next_write_fds);

    char circ_buf[CIRC_SIZE] = {0};
    int circ_buf_head = 0;
    int circ_buf_tail = 0;
    int circ_size = sizeof(circ_buf) / sizeof(*circ_buf);

    bool eof = false;

    FD_SET(read_fd, &next_read_fds);

    while (FD_ISSET(read_fd, &next_read_fds) != 0 || FD_ISSET(write_fd, &next_write_fds) != 0)
    {
        fd_set read_fds = next_read_fds;
        fd_set write_fds = next_write_fds;

        int nfds = select(next_nfds, &read_fds, &write_fds, NULL, NULL);
        if (nfds == -1)
        {
            fprintf(stderr, "E select: %s\n", strerror(errno));

            goto cleanup;
        }

        if (FD_ISSET(read_fd, &read_fds) != 0)
        {
            while (true)
            {
                int circ_buf_space = CIRC_SPACE(circ_buf_head, circ_buf_tail, circ_size);

                if (circ_buf_space < CIC_SPACE_MIN)
                {
                    FD_CLR(read_fd, &next_read_fds);

                    break;
                }

                int circ_buf_space_to_end = CIRC_SPACE_TO_END(circ_buf_head, circ_buf_tail, circ_size);

                struct iovec iovecs[] = {
                    {
                        .iov_base = circ_buf + circ_buf_head,
                        .iov_len = circ_buf_space_to_end,
                    },
                    {
                        .iov_base = circ_buf,
                        .iov_len = circ_buf_space - circ_buf_space_to_end,
                    },
                };

                int iovecs_size = sizeof(iovecs) / sizeof(*iovecs) - (circ_buf_space == circ_buf_space_to_end);

                ssize_t read_size = readv(read_fd, iovecs, iovecs_size);
                if (read_size == -1)
                {
                    if (errno == EAGAIN || errno == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "E readv: %s\n", strerror(errno));

                    goto cleanup;
                }

                if (read_size == 0)
                {
                    eof = true;

                    FD_CLR(read_fd, &next_read_fds);

                    break;
                }

                circ_buf_head = (circ_buf_head + read_size) & (circ_size - 1);

                FD_SET(write_fd, &next_write_fds);
            }
        }

        if (FD_ISSET(write_fd, &write_fds) != 0)
        {
            while (true)
            {
                int circ_buf_cnt = CIRC_CNT(circ_buf_head, circ_buf_tail, circ_size);

                if (circ_buf_cnt < CIRC_CNT_MIN)
                {
                    FD_CLR(write_fd, &next_write_fds);

                    break;
                }

                int circ_buf_cnt_to_end = CIRC_CNT_TO_END(circ_buf_head, circ_buf_tail, circ_size);

                struct iovec iovecs[] = {
                    {
                        .iov_base = circ_buf + circ_buf_tail,
                        .iov_len = circ_buf_cnt_to_end,
                    },
                    {
                        .iov_base = circ_buf,
                        .iov_len = circ_buf_cnt - circ_buf_cnt_to_end,
                    },
                };

                int iovecs_size = sizeof(iovecs) / sizeof(*iovecs) - (circ_buf_cnt == circ_buf_cnt_to_end);

                ssize_t circ_buf_write_size = writev(write_fd, iovecs, iovecs_size);
                if (circ_buf_write_size == -1)
                {
                    if (errno == EAGAIN || errno == EWOULDBLOCK)
                    {
                        break;
                    }

                    fprintf(stderr, "E writev: %s\n", strerror(errno));

                    goto cleanup;
                }

                circ_buf_tail = (circ_buf_tail + circ_buf_write_size) & (circ_size - 1);
                ;

                if (!eof)
                {
                    FD_SET(read_fd, &next_read_fds);
                }
            }
        }
    }

cleanup:;
}
