package es.deusto.ingenieria.sd.strava.facebook.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class FacebookService extends Thread implements IFacebook {

    private static FacebookService instance;

    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    // TODO
    private final Map<String, String> serverState = new HashMap<>() {
        {
            put("a@gmail.com", "12345");
            put("b@gmail.com", "67890");
            put("c@gmail.com", "password");
            put("tyfaghjsxb@gmail.com", "ksjchk");
        }
    };

    private FacebookService(final Socket socket) {
        try {
            tcpSocket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            start();
        } catch (final IOException e) {
            System.err.println("# FacebookService - TCPConnection IO error:" + e.getMessage());
        }
    }

    public static FacebookService getInstance() {
        if (instance == null) {
            try {
                instance = new FacebookService();
            } catch (final RemoteException e) {
                System.err.println("  # Error initializing service(): " + e.getMessage());
            }
        }

        return instance;
    }

    public void run() {
        try {
            String data = in.readUTF();
            System.out.println("   - FacebookService - Received data from '"
                    + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");
            data = translate(data);
            out.writeBoolean(isAlive());
            System.out.println("   - FacebookService - Sent data to '" + tcpSocket.getInetAddress().getHostAddress()
                    + ":" + tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
        } catch (final IOException e) {
            System.out.println("   # FacebookService - TCPConnection error" + e.getMessage());
        } finally {
            try {
                tcpSocket.close();
            } catch (final IOException e) {
                System.out.println("   # FacebookService - TCPConnection IO error:" + e.getMessage());
            }
        }
    }

    @Override
    public boolean checkEmail(final String email) throws RemoteException {
        return serverState.containsKey(email);
    }

    @Override
    public boolean checkEmailAndPassword(final String email, final String password) throws RemoteException {
        return serverState.containsKey(email) && serverState.get(email).equals(password);
    }

}
