# QUIC

## PROTOCOL IDEAS

* DISCARD
* ECHO
* CHAT (client -> server -> other clients) (one stream per client?)
* SIDUCK
* PERF
* MD5

## NOTES

* when connection timeout is updated, SET TIMEOUT VALUE OF SELECT TO NEW TIMEOUT IF NEW TIMEOUT IS SMALLER AND UPDATE TIMEOUTS EACH TIME SELECT RETURNS timeouts recommended

* if connection is closed (the same as read number == 0 for example) **ngtcp2_conn_get_ccerr** gets the actual error code

* when **NGTCP2_ERR_CRYPTO** is returned call **ngtcp2_conn_get_tls_alert** to get the alert code to use when writing connection close frame

* CLIENT when wants to disconnect (e. g. EOF) send **ngtcp2_conn_write_connection_close**

* When force disconnect by an APP ERROR call **ngtcp2_conn_shutdown_stream**

* SERVER when client (or server on error) closes connection server MUST hold draining or closing period for **ngtcp2_conn_get_pto** / NGTCP2_SECONDS * 3

* when sending packets call **ngtcp2_conn_writev_stream** or **ngtcp2_conn_writev_datagram** and AFTER call ONLY FOR STREAMS **ngtcp2_conn_update_pkt_tx_time**

# MULTIPROCESSING

* 1 READ CIRC BUFFER PER CLIENT AND 1 WRITE BUFFER FOR MAIN THREAD (or 1 per client?)
