#ifndef QUIC_H
#define QUIC_H

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

int client_initial_cb(ngtcp2_conn *conn, void *user_data)
{
    fprintf(stderr, "D quic: client_initial_cb\n");

    int errnum = ngtcp2_crypto_client_initial_cb(conn, user_data);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_client_initial_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int recv_client_initial_cb(ngtcp2_conn *conn, const ngtcp2_cid *dcid, void *user_data)
{
    fprintf(stderr, "D quic: recv_client_initial_cb\n");

    int errnum = ngtcp2_crypto_recv_client_initial_cb(conn, dcid, user_data);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_recv_client_initial_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int recv_crypto_data_cb(ngtcp2_conn *conn, ngtcp2_encryption_level encryption_level, uint64_t offset, const uint8_t *data, size_t datalen, void *user_data)
{
    fprintf(stderr, "D quic: recv_crypto_data_cb\n");

    int errnum = ngtcp2_crypto_recv_crypto_data_cb(conn, encryption_level, offset, data, datalen, user_data);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_recv_crypto_data_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int handshake_completed_cb(ngtcp2_conn *conn, void *user_data)
{
    (void)conn;
    (void)user_data;

    fprintf(stderr, "D quic: handshake_completed_cb\n");

    return 0;
}

int recv_version_negotiation_cb(ngtcp2_conn *conn, const ngtcp2_pkt_hd *hd, const uint32_t *sv, size_t nsv, void *user_data)
{
    (void)conn;
    (void)hd;
    (void)sv;
    (void)nsv;
    (void)user_data;

    fprintf(stderr, "D quic: recv_version_negotiation_cb\n");

    return 0;
}

int encrypt_cb(uint8_t *dest, const ngtcp2_crypto_aead *aead, const ngtcp2_crypto_aead_ctx *aead_ctx, const uint8_t *plaintext, size_t plaintextlen, const uint8_t *nonce, size_t noncelen, const uint8_t *aad, size_t aadlen)
{
    fprintf(stderr, "D quic: encrypt_cb\n");

    int errnum = ngtcp2_crypto_encrypt_cb(dest, aead, aead_ctx, plaintext, plaintextlen, nonce, noncelen, aad, aadlen);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_encrypt_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int decrypt_cb(uint8_t *dest, const ngtcp2_crypto_aead *aead, const ngtcp2_crypto_aead_ctx *aead_ctx, const uint8_t *ciphertext, size_t ciphertextlen, const uint8_t *nonce, size_t noncelen, const uint8_t *aad, size_t aadlen)
{
    fprintf(stderr, "D quic: decrypt_cb\n");

    int errnum = ngtcp2_crypto_decrypt_cb(dest, aead, aead_ctx, ciphertext, ciphertextlen, nonce, noncelen, aad, aadlen);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_decrypt_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int hp_mask_cb(uint8_t *dest, const ngtcp2_crypto_cipher *hp, const ngtcp2_crypto_cipher_ctx *hp_ctx, const uint8_t *sample)
{
    fprintf(stderr, "D quic: hp_mask_cb\n");

    int errnum = ngtcp2_crypto_hp_mask_cb(dest, hp, hp_ctx, sample);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_hp_mask_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int recv_stream_data_cb(ngtcp2_conn *conn, uint32_t flags, int64_t stream_id, uint64_t offset, const uint8_t *data, size_t datalen, void *user_data, void *stream_user_data)
{
    (void)conn;
    (void)flags;
    (void)stream_id;
    (void)offset;
    (void)user_data;
    (void)stream_user_data;

    fprintf(stderr, "D quic: recv_stream_data_cb\n");

    if (write(STDOUT_FILENO, data, datalen) == -1)
    {
        fprintf(stderr, "W quic: write: %s\n", strerror(errno));

        return NGTCP2_ERR_CALLBACK_FAILURE;
    }

    return 0;
}

int acked_stream_data_offset_cb(ngtcp2_conn *conn, int64_t stream_id, uint64_t offset, uint64_t datalen, void *user_data, void *stream_user_data)
{
    (void)conn;
    (void)stream_id;
    (void)offset;
    (void)datalen;
    (void)user_data;
    (void)stream_user_data;

    fprintf(stderr, "D quic: acked_stream_data_offset_cb\n");

    // TODO: sender frees data passed to write stream data

    return 0;
}

int stream_open_cb(ngtcp2_conn *conn, int64_t stream_id, void *user_data)
{
    (void)conn;
    (void)stream_id;
    (void)user_data;

    fprintf(stderr, "D quic: stream_open_cb\n");

    return 0;
}

int stream_close_cb(ngtcp2_conn *conn, uint32_t flags, int64_t stream_id, uint64_t app_error_code, void *user_data, void *stream_user_data)
{
    (void)conn;
    (void)flags;
    (void)stream_id;
    (void)app_error_code;
    (void)user_data;
    (void)stream_user_data;

    fprintf(stderr, "D quic: stream_close_cb\n");

    return 0;
}

int recv_stateless_reset_cb(ngtcp2_conn *conn, const ngtcp2_pkt_stateless_reset *sr, void *user_data)
{
    (void)conn;
    (void)sr;
    (void)user_data;

    fprintf(stderr, "D quic: recv_stateless_reset_cb\n");

    return 0;
}

int recv_retry_cb(ngtcp2_conn *conn, const ngtcp2_pkt_hd *hd, void *user_data)
{
    fprintf(stderr, "D quic: recv_retry_cb\n");

    int errnum = ngtcp2_crypto_recv_retry_cb(conn, hd, user_data);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_recv_retry_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int extend_max_local_streams_bidi_cb(ngtcp2_conn *conn, uint64_t max_streams, void *user_data)
{
    (void)conn;
    (void)max_streams;
    (void)user_data;

    fprintf(stderr, "D quic: extend_max_local_streams_bidi_cb\n");

    return 0;
}

int extend_max_local_streams_uni_cb(ngtcp2_conn *conn, uint64_t max_streams, void *user_data)
{
    (void)max_streams;
    (void)user_data;

    fprintf(stderr, "D quic: extend_max_local_streams_uni_cb\n");

    int64_t stream_id = -1;

    int errnum = ngtcp2_conn_open_uni_stream(conn, &stream_id, NULL);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_conn_open_uni_stream: %s\n", ngtcp2_strerror(errnum));

        return NGTCP2_ERR_CALLBACK_FAILURE;
    }

    return 0;
}

void rand_cb(uint8_t *dest, size_t destlen, const ngtcp2_rand_ctx *rand_ctx)
{
    (void)rand_ctx;

    fprintf(stderr, "D quic: rand_cb\n");

    for (size_t i = 0; i < destlen; ++i)
    {
        *dest = (uint8_t)rand();
    }
}

int get_new_connection_id_cb(ngtcp2_conn *conn, ngtcp2_cid *cid, uint8_t *token, size_t cidlen, void *user_data)
{
    (void)conn;
    (void)user_data;

    fprintf(stderr, "D quic: get_new_connection_id_cb\n");

    int errnum = 0;

    cid->datalen = cidlen;
    errnum = wolfSSL_RAND_bytes(cid->data, cid->datalen);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "W quic: wolfSSL_RAND_bytes: %s\n", wolfSSL_ERR_reason_error_string(errnum));

        return NGTCP2_ERR_CALLBACK_FAILURE;
    }

    errnum = wolfSSL_RAND_bytes(token, NGTCP2_STATELESS_RESET_TOKENLEN);
    if (errnum != WOLFSSL_SUCCESS)
    {
        fprintf(stderr, "W quic: wolfSSL_RAND_bytes: %s\n", wolfSSL_ERR_reason_error_string(errnum));

        return NGTCP2_ERR_CALLBACK_FAILURE;
    }

    // TODO: server associates cid

    return 0;
}

int remove_connection_id_cb(ngtcp2_conn *conn, const ngtcp2_cid *cid, void *user_data)
{
    (void)conn;
    (void)cid;
    (void)user_data;

    fprintf(stderr, "D quic: remove_connection_id_cb\n");

    // TODO: server dissociates cid

    return 0;
}

int update_key_cb(ngtcp2_conn *conn, uint8_t *rx_secret, uint8_t *tx_secret, ngtcp2_crypto_aead_ctx *rx_aead_ctx, uint8_t *rx_iv, ngtcp2_crypto_aead_ctx *tx_aead_ctx, uint8_t *tx_iv, const uint8_t *current_rx_secret, const uint8_t *current_tx_secret, size_t secretlen, void *user_data)
{
    fprintf(stderr, "D quic: update_key_cb\n");

    int errnum = ngtcp2_crypto_update_key_cb(conn, rx_secret, tx_secret, rx_aead_ctx, rx_iv, tx_aead_ctx, tx_iv, current_rx_secret, current_tx_secret, secretlen, user_data);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_update_key_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int path_validation_cb(ngtcp2_conn *conn, uint32_t flags, const ngtcp2_path *path, const ngtcp2_path *old_path, ngtcp2_path_validation_result res, void *user_data)
{
    (void)conn;
    (void)flags;
    (void)path;
    (void)old_path;
    (void)res;
    (void)user_data;

    fprintf(stderr, "D quic: path_validation_cb\n");

    return 0;
}

int select_preferred_addr_cb(ngtcp2_conn *conn, ngtcp2_path *dest, const ngtcp2_preferred_addr *paddr, void *user_data)
{
    (void)conn;
    (void)dest;
    (void)paddr;
    (void)user_data;

    fprintf(stderr, "D quic: select_preferred_addr_cb\n");

    return 0;
}

int stream_reset_cb(ngtcp2_conn *conn, int64_t stream_id, uint64_t final_size, uint64_t app_error_code, void *user_data, void *stream_user_data)
{
    (void)conn;
    (void)stream_id;
    (void)final_size;
    (void)app_error_code;
    (void)user_data;
    (void)stream_user_data;

    fprintf(stderr, "D quic: stream_reset_cb\n");

    return 0;
}

int extend_max_remote_streams_bidi_cb(ngtcp2_conn *conn, uint64_t max_streams, void *user_data)
{
    (void)conn;
    (void)max_streams;
    (void)user_data;

    fprintf(stderr, "D quic: extend_max_remote_streams_bidi_cb\n");

    return 0;
}

int extend_max_remote_streams_uni_cb(ngtcp2_conn *conn, uint64_t max_streams, void *user_data)
{
    (void)conn;
    (void)max_streams;
    (void)user_data;

    fprintf(stderr, "D quic: extend_max_remote_streams_uni_cb\n");

    return 0;
}

int extend_max_stream_data_cb(ngtcp2_conn *conn, int64_t stream_id, uint64_t max_data, void *user_data, void *stream_user_data)
{
    (void)conn;
    (void)stream_id;
    (void)max_data;
    (void)user_data;
    (void)stream_user_data;

    fprintf(stderr, "D quic: extend_max_stream_data_cb\n");

    // TODO: client sends stream data (TO SEND 0-RTT CALL MANUALLY) and wants to write, assign stream_id

    return 0;
}

int dcid_status_cb(ngtcp2_conn *conn, ngtcp2_connection_id_status_type type, uint64_t seq, const ngtcp2_cid *cid, const uint8_t *token, void *user_data)
{
    (void)conn;
    (void)type;
    (void)seq;
    (void)cid;
    (void)token;
    (void)user_data;

    fprintf(stderr, "D quic: dcid_status_cb\n");

    return 0;
}

int handshake_confirmed_cb(ngtcp2_conn *conn, void *user_data)
{
    (void)conn;
    (void)user_data;

    fprintf(stderr, "D quic: handshake_confirmed_cb\n");

    return 0;
}

int recv_new_token_cb(ngtcp2_conn *conn, const uint8_t *token, size_t tokenlen, void *user_data)
{
    (void)conn;
    (void)token;
    (void)tokenlen;
    (void)user_data;

    fprintf(stderr, "D quic: recv_new_token_cb\n");

    return 0;
}

void delete_crypto_aead_ctx_cb(ngtcp2_conn *conn, ngtcp2_crypto_aead_ctx *aead_ctx, void *user_data)
{
    fprintf(stderr, "D quic: delete_crypto_aead_ctx_cb\n");

    ngtcp2_crypto_delete_crypto_aead_ctx_cb(conn, aead_ctx, user_data);
}

void delete_crypto_cipher_ctx_cb(ngtcp2_conn *conn, ngtcp2_crypto_cipher_ctx *cipher_ctx, void *user_data)
{
    fprintf(stderr, "D quic: delete_crypto_cipher_ctx_cb\n");

    ngtcp2_crypto_delete_crypto_cipher_ctx_cb(conn, cipher_ctx, user_data);
}

int recv_datagram_cb(ngtcp2_conn *conn, uint32_t flags, const uint8_t *data, size_t datalen, void *user_data)
{
    (void)conn;
    (void)flags;
    (void)user_data;

    fprintf(stderr, "D quic: recv_datagram_cb\n");

    if (write(STDOUT_FILENO, data, datalen) == -1)
    {
        fprintf(stderr, "W quic: write: %s\n", strerror(errno));

        return NGTCP2_ERR_CALLBACK_FAILURE;
    }

    return 0;
}

int ack_datagram_cb(ngtcp2_conn *conn, uint64_t dgram_id, void *user_data)
{
    (void)conn;
    (void)dgram_id;
    (void)user_data;

    fprintf(stderr, "D quic: ack_datagram_cb\n");

    return 0;
}

int lost_datagram_cb(ngtcp2_conn *conn, uint64_t dgram_id, void *user_data)
{
    (void)conn;
    (void)dgram_id;
    (void)user_data;

    fprintf(stderr, "D quic: lost_datagram_cb\n");

    return 0;
}

int get_path_challenge_data_cb(ngtcp2_conn *conn, uint8_t *data, void *user_data)
{
    fprintf(stderr, "D quic: get_path_challenge_data_cb\n");

    int errnum = ngtcp2_crypto_get_path_challenge_data_cb(conn, data, user_data);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_get_path_challenge_data_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int stream_stop_sending_cb(ngtcp2_conn *conn, int64_t stream_id, uint64_t app_error_code, void *user_data, void *stream_user_data)
{
    (void)conn;
    (void)stream_id;
    (void)app_error_code;
    (void)user_data;
    (void)stream_user_data;

    fprintf(stderr, "D quic: stream_stop_sending_cb\n");

    return 0;
}

int version_negotiation_cb(ngtcp2_conn *conn, uint32_t version, const ngtcp2_cid *client_dcid, void *user_data)
{
    fprintf(stderr, "D quic: version_negotiation_cb\n");

    int errnum = ngtcp2_crypto_version_negotiation_cb(conn, version, client_dcid, user_data);
    if (errnum != 0)
    {
        fprintf(stderr, "W quic: ngtcp2_crypto_version_negotiation_cb: %s\n", ngtcp2_strerror(errnum));
    }

    return errnum;
}

int recv_rx_key_cb(ngtcp2_conn *conn, ngtcp2_encryption_level level, void *user_data)
{
    (void)conn;
    (void)level;
    (void)user_data;

    fprintf(stderr, "D quic: recv_rx_key_cb\n");

    return 0;
}

int recv_tx_key_cb(ngtcp2_conn *conn, ngtcp2_encryption_level level, void *user_data)
{
    (void)conn;
    (void)level;
    (void)user_data;

    fprintf(stderr, "D quic: recv_tx_key_cb\n");

    return 0;
}

int tls_early_data_rejected_cb(ngtcp2_conn *conn, void *user_data)
{
    (void)conn;
    (void)user_data;

    fprintf(stderr, "D quic: tls_early_data_rejected_cb\n");

    return 0;
}

#endif
