#include <errno.h>
#include <mqueue.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#define MQ_NAME "/mq"
#define MQ_MAXMSG 10
#define MQ_MSGSIZE 1024
#define MQ_CLIENT_THREADS_LEN 10

void *mq_server(void *arg)
{
    size_t i = (size_t)arg;

    pthread_t thread = pthread_self();

    fprintf(stderr, "I mq_server=%d thread=%lu\n", i, thread);

    mqd_t mqdes = mq_open(MQ_NAME, O_WRONLY);
    if (mqdes == -1)
    {
        fprintf(stderr, "E mq_server thread=%lu: mq_open: %s\n", thread, strerror(errno));

        goto mqdes_cleanup;
    }

    while (true)
    {
        char msg[MQ_MSGSIZE] = "";

        ssize_t msg_len = read(STDIN_FILENO, msg, sizeof(msg));
        if (msg_len == -1)
        {
            fprintf(stderr, "E mq_server thread=%lu: read: %s\n", thread, strerror(errno));

            goto mqdes_cleanup;
        }

        if (msg_len == 0)
        {
            fprintf(stderr, "I mq_server thread=%lu: EOF\n", thread);

            for (size_t i = 0; i < MQ_CLIENT_THREADS_LEN; ++i)
            {
                if (mq_send(mqdes, "", 0, 0) == -1)
                {
                    fprintf(stderr, "E mq_server thread=%lu: mq_send: %s\n", thread, strerror(errno));

                    goto mqdes_cleanup;
                }
            }

            break;
        }

        if (mq_send(mqdes, msg, msg_len, 0) == -1)
        {
            fprintf(stderr, "E mq_server thread=%lu: mq_send: %s\n", thread, strerror(errno));

            goto mqdes_cleanup;
        }
    }

mqdes_cleanup:
    if (mqdes != -1)
    {
        if (mq_close(mqdes) == -1)
        {
            fprintf(stderr, "W mq_server thread=%lu: mq_close mqdes=%d: %s\n", thread, mqdes, strerror(errno));
        }
    }

    return NULL;
}

void *mq_client(void *arg)
{
    size_t i = (size_t)arg;

    pthread_t thread = pthread_self();

    fprintf(stderr, "I mq_client=%d thread=%lu\n", (int)i, thread);

    mqd_t mqdes = mq_open(MQ_NAME, O_RDONLY);
    if (mqdes == -1)
    {
        fprintf(stderr, "E mq_client thread=%lu: mq_open: %s\n", thread, strerror(errno));

        goto mqdes_cleanup;
    }

    while (true)
    {
        char msg[MQ_MSGSIZE + 1] = {0};

        ssize_t msg_len = mq_receive(mqdes, msg, sizeof(msg), NULL);
        if (msg_len == -1)
        {
            fprintf(stderr, "E mq_client thread=%lu: mq_receive: %s\n", thread, strerror(errno));

            goto mqdes_cleanup;
        }

        if (msg_len == 0)
        {
            fprintf(stderr, "I mq_client thread=%lu: EOF\n", thread);

            break;
        }

        msg[msg_len] = '\0';

        if (printf("I mq_client thread=%lu: %.*s", thread, (int)msg_len, msg) < 0)
        {
            fprintf(stderr, "E mq_client thread=%lu: printf: %s\n", thread, strerror(errno));

            goto mqdes_cleanup;
        }
    }

mqdes_cleanup:
    if (mqdes != -1)
    {
        if (mq_close(mqdes) == -1)
        {
            fprintf(stderr, "W mq_client thread=%lu: mq_close mqdes=%d: %s\n", thread, mqdes, strerror(errno));
        }
    }

    return NULL;
}

int main(void)
{
    struct mq_attr mq_attr = {
        .mq_maxmsg = MQ_MAXMSG,
        .mq_msgsize = MQ_MSGSIZE,
    };

    mqd_t mqdes = mq_open(MQ_NAME, O_CREAT | O_EXCL, 0600, &mq_attr);
    if (mqdes == -1)
    {
        fprintf(stderr, "E mq: mq_open: %s\n", strerror(errno));

        goto mqdes_cleanup;
    }

    pthread_t mq_server_thread = 0;

    if (pthread_create(&mq_server_thread, NULL, mq_server, (void *)0) != 0)
    {
        fprintf(stderr, "E mq: pthread_create: mq_server %s\n", strerror(errno));

        goto mqdes_cleanup;
    }

    pthread_t mq_client_threads[MQ_CLIENT_THREADS_LEN] = {0};

    for (size_t i = 0; i < MQ_CLIENT_THREADS_LEN; ++i)
    {
        if (pthread_create(&mq_client_threads[i], NULL, mq_client, (void *)i) != 0)
        {
            fprintf(stderr, "E mq: pthread_create: mq_client=%d %s\n", (int)i, strerror(errno));

            goto mqdes_cleanup;
        }
    }

    if (pthread_join(mq_server_thread, NULL) != 0)
    {
        fprintf(stderr, "E mq: pthread_join thread=%lu: %s\n", mq_server_thread, strerror(errno));

        goto mqdes_cleanup;
    }

    for (size_t i = 0; i < MQ_CLIENT_THREADS_LEN; ++i)
    {
        if (pthread_join(mq_client_threads[i], NULL) != 0)
        {
            fprintf(stderr, "E mq: pthread_join thread=%lu: %s\n", mq_client_threads[i], strerror(errno));

            goto mqdes_cleanup;
        }
    }

mqdes_cleanup:
    if (mqdes != -1)
    {
        if (mq_close(mqdes) == -1)
        {
            fprintf(stderr, "W mq: mq_close mqdes=%d: %s\n", mqdes, strerror(errno));
        }

        if (mq_unlink("/mq") == -1)
        {
            fprintf(stderr, "W mq: mq_unlink mqdes=%d: %s\n", mqdes, strerror(errno));
        }
    }
}
