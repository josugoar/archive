#include <esp_err.h>
#include <esp_event.h>
#include <esp_log.h>

ESP_EVENT_DECLARE_BASE(READ_EVENT);
ESP_EVENT_DECLARE_BASE(WRITE_EVENT);

ESP_EVENT_DEFINE_BASE(READ_EVENT);
ESP_EVENT_DEFINE_BASE(WRITE_EVENT);

void read_event_handler(void *event_handler_arg, esp_event_base_t event_base, int32_t event_id, void *event_data)
{
}

void write_event_handler(void *event_handler_arg, esp_event_base_t event_base, int32_t event_id, void *event_data)
{
}

void app_main(void)
{
    int errnum = 0;

    esp_event_loop_args_t event_loop_args = {
        .queue_size = 0,
    };

    esp_event_loop_handle_t event_loop = NULL;

    errnum = esp_event_loop_create(&event_loop_args, &event_loop);
    if (errnum != ESP_OK)
    {
        ESP_LOGE(TAG, "esp_event_loop_create: %s", esp_err_to_name(errnum));

        goto event_loop_cleanup;
    }

    esp_event_handler_instance_t instance = NULL;

    // TODO
    errnum = esp_event_handler_instance_register_with(event_loop, ESP_EVENT_ANY_BASE, ESP_EVENT_ANY_ID, event_handler, NULL, &instance);
    if (errnum != ESP_OK)
    {
        ESP_LOGE(TAG, "esp_event_handler_instance_register_with: %s", esp_err_to_name(errnum));

        goto event_loop_cleanup;
    }

    struct addrinfo req = {
        .ai_family = AF_UNSPEC,
        .ai_socktype = SOCK_DGRAM,
    };

    struct addrinfo *ais = NULL;

    errnum = getaddrinfo(NAME, SERVICE, &req, &ais);
    if (errnum != 0)
    {
        ESP_LOGE(TAG, "getaddrinfo: %d", errnum);

        goto ais_cleanup;
    }

    int sockd = -1;

    for (struct addrinfo *ai = ais; ai; ai = ai->ai_next)
    {
        sockd = socket(ai->ai_family, ai->ai_socktype, ai->ai_protocol);
        if (sockd == -1)
        {
            if (!ai->ai_next)
            {
                ESP_LOGE(TAG, "socket: %s", strerror(errno));

                goto sockd_cleanup;
            }

            ESP_LOGW(TAG, "socket: %s", strerror(errno));

            continue;
        }

        if (connect(sockd, ai->ai_addr, ai->ai_addrlen) != 0)
        {
            if (!ai->ai_next)
            {
                ESP_LOGE(TAG, "connect: %s", strerror(errno));

                goto sockd_cleanup;
            }

            ESP_LOGW(TAG, "connect: %s", strerror(errno));

            if (close(sockd) != 0)
            {
                ESP_LOGW(TAG, "close: %s", strerror(errno));
            }

            continue;
        }

        break;
    }

    int readfd = STDIN_FILENO;

    if (fcntl(readfd, F_SETFL, fcntl(readfd, F_GETFL) | O_NONBLOCK) != 0)
    {
        ESP_LOGE(TAG, "fcntl: %s", strerror(errno));

        goto sockd_cleanup;
    }

    int writefd = sockd;

    if (fcntl(writefd, F_SETFL, fcntl(writefd, F_GETFL) | O_NONBLOCK) != 0)
    {
        ESP_LOGE(TAG, "fcntl: %s", strerror(errno));

        goto sockd_cleanup;
    }

    int nfds = readfd > writefd ? readfd + 1 : writefd + 1;
    if (nfds >= FD_SETSIZE)
    {
        fprintf(stderr, "select: %s\n", strerror(EINVAL));

        goto sockd_cleanup;
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

    while (FD_ISSET(readfd, &next_readfds) != 0 || FD_ISSET(writefd, &next_writefds) != 0)
    {
        fd_set curr_readfds = next_readfds;
        fd_set curr_writefds = next_writefds;

        if (select(nfds, &curr_readfds, &curr_writefds, NULL, NULL) == -1)
        {
            ESP_LOGE(TAG, "select: %s", strerror(errno));

            goto sockd_cleanup;
        }

        for (int fd = 0; fd < nfds; ++fd)
        {
            esp_event_base_t event_base = ESP_EVENT_ANY_BASE;

            if (FD_ISSET(fd, &curr_readfds) != 0)
            {
                event_base = READ_EVENT;
            }

            if (FD_ISSET(fd, &curr_writefds) != 0)
            {
                event_base = WRITE_EVENT;
            }

            if (event_base != ESP_EVENT_ANY_BASE)
            {
                errnum = esp_event_post_to(event_loop, event_base, fd, NULL, 0, 0);
                if (errnum != ESP_OK)
                {
                    ESP_LOGE(TAG, "esp_event_post_to: %s", esp_err_to_name(errnum));

                    goto sockd_cleanup;
                }
            }
        }

        // TODO
        continue;

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

                    ESP_LOGE(TAG, "read: %s", strerror(errno));

                    goto sockd_cleanup;
                }

                if (n == 0)
                {
                    eof = true;

                    FD_CLR(readfd, &next_readfds);

                    break;
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

                    if (!eof)
                    {
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

                    ESP_LOGE(TAG, "write: %s", strerror(errno));

                    goto sockd_cleanup;
                }

                nwrite += n;
            }
        }

        errnum = esp_event_loop_run(event_loop, 0);
        if (errnum != ESP_OK)
        {
            ESP_LOGE(TAG, "esp_event_loop_run: %s", esp_err_to_name(errnum));

            goto sockd_cleanup;
        }
    }

    // TODO
    errnum = esp_event_handler_instance_unregister_with(event_loop, ESP_EVENT_ANY_BASE, ESP_EVENT_ANY_ID, instance);
    if (errnum != ESP_OK)
    {
        ESP_LOGW(TAG, "esp_event_handler_instance_unregister_with: %s", esp_err_to_name(errnum));
    }

sockd_cleanup:
    if (sockd != -1)
    {
        if (close(sockd) != 0)
        {
            ESP_LOGW(TAG, "close: %s", strerror(errno));
        }
    }

ais_cleanup:
    if (ais)
    {
        freeaddrinfo(ais);
    }

event_loop_cleanup:
    if (event_loop)
    {
        errnum = esp_event_loop_delete(event_loop);
        if (errnum != ESP_OK)
        {
            ESP_LOGW(TAG, "esp_event_loop_delete: %s", esp_err_to_name(errnum));
        }
    }
}
