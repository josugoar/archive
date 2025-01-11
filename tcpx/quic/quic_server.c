#define USE_CERT_BUFFERS_1024

#include "quic.h"

#include <errno.h>
#include <netdb.h>
#include <ngtcp2/ngtcp2_crypto_wolfssl.h>
#include <ngtcp2/ngtcp2_crypto.h>
#include <ngtcp2/ngtcp2.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <time.h>
#include <unistd.h>
#include <wolfssl/certs_test.h>
#include <wolfssl/ssl.h>

#undef DEBUG_WOLFSSL
#define DEBUG_NGTCP2
#define CTX_SERVER_CERT server_cert_der_1024
#define CTX_SERVER_KEY server_key_der_1024
#define NAME "localhost"
#define SERVICE "1025"
#define PROTOCOL_NAME_LIST "quic"

static const char *TAG = "quic_server";

#ifdef DEBUG_WOLFSSL
void wolfSSL_log(int log_level, const char *log_message)
{
    (void)log_level;

    fprintf(stderr, "%s %s: %s\n", "V", "wolfSSL", log_message);
}
#endif

#ifdef DEBUG_NGTCP2
void ngtcp2_log(void *user_data, const char *format, ...)
{
    (void)user_data;

    va_list arg;
    va_start(arg, format);

    fprintf(stderr, "%s %s: ", "V", "ngtcp2");
    vfprintf(stderr, format, arg);
    fprintf(stderr, "\n");

    va_end(arg);
}
#endif

ngtcp2_tstamp timestamp(void)
{
    struct timespec tp = {0};

    if (clock_gettime(CLOCK_MONOTONIC, &tp) != 0)
    {
        fprintf(stderr, "%s %s: clock_gettime: %s\n", "W", TAG, strerror(errno));

        return UINT64_MAX;
    }

    return tp.tv_sec * NGTCP2_SECONDS + tp.tv_nsec * NGTCP2_NANOSECONDS;
}

ngtcp2_conn *get_conn(ngtcp2_crypto_conn_ref *conn_ref)
{
    return (ngtcp2_conn *)conn_ref->user_data;
}

#ifdef ESP_PLATFORM
void app_main(void)
#else
int main(void)
#endif
{
    int errnum = 0;

    struct addrinfo *ais = NULL;
    int fd = -1;
    WOLFSSL_CTX *ctx = NULL;
    WOLFSSL *ssl = NULL;
    ngtcp2_conn *conn = NULL;

#ifdef DEBUG_WOLFSSL
    errnum = wolfSSL_Debugging_ON();
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: wolfSSL_Debugging_ON: %s\n", "V", TAG, wolfSSL_ERR_reason_error_string(errnum));
    }

    errnum = wolfSSL_SetLoggingCb(wolfSSL_log);
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: wolfSSL_SetLoggingCb: %s\n", "V", TAG, wolfSSL_ERR_reason_error_string(errnum));
    }
#endif

    struct addrinfo req = {
        .ai_flags = AI_PASSIVE,
        .ai_family = AF_UNSPEC,
        .ai_socktype = SOCK_DGRAM,
        .ai_protocol = 0,
    };

    errnum = getaddrinfo(NAME, SERVICE, &req, &ais);
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: getaddrinfo: %d\n", "E", TAG, errnum);
        goto cleanup;
    }

    struct sockaddr_storage local_addr = {0};
    socklen_t local_addr_len = sizeof(local_addr);

    struct sockaddr_storage remote_addr = {0};
    socklen_t remote_addr_len = sizeof(remote_addr);

    for (struct addrinfo *ai = ais; ai; ai = ai->ai_next)
    {
        fd = socket(ai->ai_family, ai->ai_socktype, ai->ai_protocol);
        if (fd == -1)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "%s %s: socket: %s\n", "E", TAG, strerror(errno));
                goto cleanup;
            }

            fprintf(stderr, "%s %s: socket: %s\n", "W", TAG, strerror(errno));

            continue;
        }

        if (bind(fd, ai->ai_addr, ai->ai_addrlen) != 0)
        {
            if (!ai->ai_next)
            {
                fprintf(stderr, "%s %s: bind: %s\n", "E", TAG, strerror(errno));
                goto cleanup;
            }

            fprintf(stderr, "%s %s: bind: %s\n", "W", TAG, strerror(errno));

            if (close(fd) != 0)
            {
                fprintf(stderr, "%s %s: close: %s\n", "W", TAG, strerror(errno));
            }

            continue;
        }

        memcpy(&local_addr, ai->ai_addr, ai->ai_addrlen);
        local_addr_len = ai->ai_addrlen;

        break;
    }

    ctx = wolfSSL_CTX_new(wolfTLSv1_3_server_method());
    if (!ctx)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_new", "E", TAG);
        goto cleanup;
    }

    errnum = wolfSSL_CTX_use_certificate_chain_buffer_format(ctx, CTX_SERVER_CERT, sizeof(CTX_SERVER_CERT), WOLFSSL_FILETYPE_ASN1);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_use_certificate_chain_buffer_format: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));
        goto cleanup;
    }

    errnum = wolfSSL_CTX_use_PrivateKey_buffer(ctx, CTX_SERVER_KEY, sizeof(CTX_SERVER_KEY), WOLFSSL_FILETYPE_ASN1);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_use_PrivateKey_buffer: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));
        goto cleanup;
    }

    errnum = wolfSSL_CTX_check_private_key(ctx);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_CTX_check_private_key: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));
        goto cleanup;
    }

    if (ngtcp2_crypto_wolfssl_configure_server_context(ctx) != 0)
    {
        fprintf(stderr, "%s %s: ngtcp2_crypto_wolfssl_configure_server_context\n", "E", TAG);
        goto cleanup;
    }

    int readfd = fd;
    int writefd = STDOUT_FILENO;

    unsigned char readbuf[NGTCP2_DEFAULT_MAX_RECV_UDP_PAYLOAD_SIZE] = {0};
    ssize_t nread = 0;

    unsigned char writebuf[NGTCP2_MAX_UDP_PAYLOAD_SIZE] = {0};
    ssize_t nwrite = 0;

    nread = recvfrom(readfd, readbuf, sizeof(readbuf), 0, (struct sockaddr *)&remote_addr, &remote_addr_len);
    if (nread == -1)
    {
        fprintf(stderr, "%s %s: recvfrom: %s\n", "E", TAG, strerror(errno));
        goto cleanup;
    }

    if (connect(fd, (struct sockaddr *)&remote_addr, remote_addr_len) != 0)
    {
        fprintf(stderr, "%s %s: connect: %s\n", "E", TAG, strerror(errno));
        goto cleanup;
    }

    ngtcp2_version_cid vc = {0};

    errnum = ngtcp2_pkt_decode_version_cid(&vc, readbuf, nread, NGTCP2_MAX_CIDLEN);
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: ngtcp2_pkt_decode_version_cid: %s\n", "E", TAG, ngtcp2_strerror(errnum));
        goto cleanup;
    }

    ngtcp2_pkt_hd hd = {0};

    errnum = ngtcp2_accept(&hd, readbuf, nread);
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: ngtcp2_accept: %s\n", "E", TAG, ngtcp2_strerror(errnum));
        goto cleanup;
    }

    ssl = wolfSSL_new(ctx);
    if (!ssl)
    {
        fprintf(stderr, "%s %s: wolfSSL_new\n", "E", TAG);
        goto cleanup;
    }

    errnum = wolfSSL_UseALPN(ssl, PROTOCOL_NAME_LIST, sizeof(PROTOCOL_NAME_LIST) - 1, WOLFSSL_ALPN_FAILED_ON_MISMATCH);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_UseALPN: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));
        goto cleanup;
    }

    ngtcp2_cid dcid = hd.scid;

    ngtcp2_cid scid = {0};
    scid.datalen = NGTCP2_MAX_CIDLEN;
    errnum = wolfSSL_RAND_bytes(scid.data, scid.datalen);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_RAND_bytes: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));
        goto cleanup;
    }

    ngtcp2_path path = {
        .local = {
            .addr = (ngtcp2_sockaddr *)&local_addr,
            .addrlen = local_addr_len,
        },
        .remote = {
            .addr = (ngtcp2_sockaddr *)&remote_addr,
            .addrlen = remote_addr_len,
        },
    };

    uint32_t client_chosen_version = hd.version;

    ngtcp2_callbacks callbacks = {
        .client_initial = client_initial_cb,
        .recv_client_initial = recv_client_initial_cb,
        .recv_crypto_data = recv_crypto_data_cb,
        .handshake_completed = handshake_completed_cb,
        .recv_version_negotiation = recv_version_negotiation_cb,
        .encrypt = encrypt_cb,
        .decrypt = decrypt_cb,
        .hp_mask = hp_mask_cb,
        .recv_stream_data = recv_stream_data_cb,
        .acked_stream_data_offset = acked_stream_data_offset_cb,
        .stream_open = stream_open_cb,
        .stream_close = stream_close_cb,
        .recv_stateless_reset = recv_stateless_reset_cb,
        .recv_retry = recv_retry_cb,
        .extend_max_local_streams_bidi = extend_max_local_streams_bidi_cb,
        .extend_max_local_streams_uni = extend_max_local_streams_uni_cb,
        .rand = rand_cb,
        .get_new_connection_id = get_new_connection_id_cb,
        .remove_connection_id = remove_connection_id_cb,
        .update_key = update_key_cb,
        .path_validation = path_validation_cb,
        .select_preferred_addr = select_preferred_addr_cb,
        .stream_reset = stream_reset_cb,
        .extend_max_remote_streams_bidi = extend_max_remote_streams_bidi_cb,
        .extend_max_remote_streams_uni = extend_max_remote_streams_uni_cb,
        .extend_max_stream_data = extend_max_stream_data_cb,
        .dcid_status = dcid_status_cb,
        .handshake_confirmed = handshake_confirmed_cb,
        .recv_new_token = recv_new_token_cb,
        .delete_crypto_aead_ctx = delete_crypto_aead_ctx_cb,
        .delete_crypto_cipher_ctx = delete_crypto_cipher_ctx_cb,
        .recv_datagram = recv_datagram_cb,
        .ack_datagram = ack_datagram_cb,
        .lost_datagram = lost_datagram_cb,
        .get_path_challenge_data = get_path_challenge_data_cb,
        .stream_stop_sending = stream_stop_sending_cb,
        .version_negotiation = version_negotiation_cb,
        .recv_rx_key = recv_rx_key_cb,
        .recv_tx_key = recv_tx_key_cb,
        .tls_early_data_rejected = tls_early_data_rejected_cb,
    };

    ngtcp2_settings settings = {0};
    ngtcp2_settings_default(&settings);
    settings.initial_ts = timestamp();
#if defined(DEBUG_NGTCP2)
    settings.log_printf = ngtcp2_log;
#endif

    ngtcp2_transport_params params = {0};
    ngtcp2_transport_params_default(&params);
    params.original_dcid = hd.dcid;
    params.initial_max_stream_data_bidi_remote = NGTCP2_MAX_VARINT;
    params.initial_max_stream_data_uni = NGTCP2_MAX_VARINT;
    params.initial_max_data = NGTCP2_MAX_VARINT;
    params.initial_max_streams_bidi = 1;
    params.initial_max_streams_uni = 1;
    params.max_datagram_frame_size = NGTCP2_DEFAULT_MAX_RECV_UDP_PAYLOAD_SIZE;
    params.original_dcid_present = 1;

    errnum = ngtcp2_conn_server_new(&conn, &dcid, &scid, &path, client_chosen_version, &callbacks, &settings, &params, NULL, NULL);
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: ngtcp2_conn_server_new: %s\n", "E", TAG, ngtcp2_strerror(errnum));
        goto cleanup;
    }

    ngtcp2_crypto_conn_ref conn_ref = {
        .get_conn = get_conn,
        .user_data = conn,
    };

    errnum = wolfSSL_set_ex_data(ssl, 0, &conn_ref);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "%s %s: wolfSSL_set_ex_data: %s\n", "E", TAG, wolfSSL_ERR_reason_error_string(errnum));
        goto cleanup;
    }

    ngtcp2_conn_set_tls_native_handle(conn, ssl);

    errnum = ngtcp2_conn_read_pkt(conn, &path, NULL, readbuf, nread, timestamp());
    if (errnum != 0)
    {
        fprintf(stderr, "%s %s: ngtcp2_conn_read_pkt: %s\n", "E", TAG, ngtcp2_strerror(errnum));
        goto cleanup;
    }

    while (false)
    {
        nread = read(readfd, readbuf, sizeof(readbuf));
        if (readbuf == -1)
        {
            fprintf(stderr, "%s %s: read: %s\n", "E", TAG, strerror(errno));
            goto cleanup;
        }

        errnum = ngtcp2_conn_read_pkt(conn, &path, NULL, readbuf, nread, timestamp());
        if (errnum != 0)
        {
            fprintf(stderr, "%s %s: ngtcp2_conn_read_pkt: %s\n", "E", TAG, ngtcp2_strerror(errnum));
            goto cleanup;
        }
    }

cleanup:
    if (conn)
    {
        ngtcp2_conn_del(conn);
    }

    if (ssl)
    {
        wolfSSL_free(ssl);
    }

    if (ctx)
    {
        wolfSSL_CTX_free(ctx);
    }

    if (fd != -1)
    {
        if (close(fd) != 0)
        {
            fprintf(stderr, "%s %s: close: %s\n", "W", TAG, strerror(errno));
        }
    }

    if (ais)
    {
        freeaddrinfo(ais);
    }
}
