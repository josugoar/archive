#include <errno.h>
#include <limits.h>
#include <mqueue.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

void mq_ev(void)
{
    mqd_t mqdes = mq_open("/mq", O_WRONLY);

    while (true)
    {
        char msg[SSIZE_MAX] = "";

        if (!fgets(msg, sizeof(msg), stdin))
        {
            if (feof(stdin))
            {
                break;
            }

            fprintf(stderr, "E mq: fgets: %s\n", strerror(errno));

            goto mq_cleanup;
        }

        if (mq_send(mqdes, msg, strlen(msg), 0) == -1)
        {
            fprintf(stderr, "E mq: mq_send: %s\n", strerror(errno));

            goto mq_cleanup;
        }
    }

    // TODO: does this work or a sentinel value is needed (not msg_len == 0)?
    if (mq_send(mqdes, "", 0, 0) == -1)
    {
        fprintf(stderr, "E mq: mq_send: %s\n", strerror(errno));

        goto mq_cleanup;
    }

mq_cleanup:
    mq_close(mqdes);
}

void mq_wkr(void)
{
    mqd_t mqdes = mq_open("/mq", O_RDONLY);

    while (true)
    {
        char msg[SSIZE_MAX] = {0};

        ssize_t msg_len = mq_receive(mqdes, msg, sizeof(msg) - 1, NULL);
        if (msg_len == -1)
        {
            fprintf(stderr, "E mq: mq_receive: %s\n", strerror(errno));

            goto mq_cleanup;
        }

        if (msg_len == 0)
        {
            break;
        }

        msg[msg_len] = '\0';

        // TODO: pthread information

        if (fputs(msg, stdout) == EOF)
        {
            fprintf(stderr, "E mq: fputs: %s\n", strerror(errno));

            goto mq_cleanup;
        }
    }

mq_cleanup:
    mq_close(mqdes);
}

int main(void)
{
    // TODO: mode
    mqd_t mqdes = mq_open("/mq", O_CREAT | O_EXCL, 0600, NULL);
    if (mqdes == -1)
    {
        fprintf(stderr, "E mq: mq_open: %s\n", strerror(errno));

        goto mq_cleanup;
    }

    pthread_t ev;
    pthread_t wkr;

    pthread_create(&ev, NULL, (void *(*)(void *))mq_ev, NULL);
    pthread_create(&wkr, NULL, (void *(*)(void *))mq_wkr, NULL);

    pthread_join(ev, NULL);
    pthread_join(wkr, NULL);

mq_cleanup:
    mq_close(mqdes);
    mq_unlink("/mq");
}
