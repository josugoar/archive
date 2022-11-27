package es.deusto.ingenieria.sd.strava.facebook.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class FacebookService extends Thread {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    private static final String DELIMITER = "\0";

    // TODO
    private final Map<String, String> serverState = new HashMap<>() {
        {
            put("a@gmail.com", "12345");
            put("b@gmail.com", "67890");
            put("c@gmail.com", "password");
            put("tyfaghjsxb@gmail.com", "ksjchk");
        }
    };

    public FacebookService(final Socket socket) {
        try {
            tcpSocket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            start();
        } catch (final IOException e) {
            System.err.println("# FacebookService - TCPConnection IO error:" + e.getMessage());
        }
    }

    public void run() {
        try {
            final String inData = in.readUTF();

            System.err.println("   - FacebookService - Received data from '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + inData + "'");

            // TODO
            final String[] strings = inData.split(DELIMITER, 2);
            final boolean outData;
            if (strings.length == 1) {
                outData = checkEmail(strings[0]);
            } else {
                outData = checkEmailAndPassword(strings[0], strings[1]);
            }

            out.writeBoolean(outData);

            System.err.println("   - FacebookService - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + outData + "'");
        } catch (final IOException e) {
            System.err.println("   # FacebookService - TCPConnection error" + e.getMessage());
        } finally {
            try {
                tcpSocket.close();
            } catch (final IOException e) {
                System.err.println("   # FacebookService - TCPConnection IO error:" + e.getMessage());
            }
        }
    }

    public boolean checkEmail(final String email) {
        return serverState.containsKey(email);
    }

    public boolean checkEmailAndPassword(final String email, final String password) {
        return serverState.containsKey(email) && serverState.get(email).equals(password);
    }

}
