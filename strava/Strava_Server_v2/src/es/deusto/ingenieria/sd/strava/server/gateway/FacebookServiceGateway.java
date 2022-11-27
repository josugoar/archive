package es.deusto.ingenieria.sd.strava.server.gateway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FacebookServiceGateway {

    private static FacebookServiceGateway instance;

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8001;

    private static final String DELIMITER = "#";

    private FacebookServiceGateway() {
    }

    public static FacebookServiceGateway getInstance() {
        if (instance == null) {
            instance = new FacebookServiceGateway();
        }

        return instance;
    }

    public boolean checkEmail(final String email) {
        System.err.println("   - Check email from Facebook Service Gateway");

        try (final Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                final DataInputStream in = new DataInputStream(socket.getInputStream());
                final DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            out.writeUTF(email);

            System.err.println(" - Sending data to '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + email + "'");

            final boolean inData = in.readBoolean();

            System.err.println(" - Getting check from '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + inData + "'");

            return inData;
        } catch (final IOException e) {
            System.err.println("# FacebookServiceGateway error: " + e.getMessage());

            return false;
        }
    }

    public boolean checkEmailAndPassword(final String email, final String password) {
        System.err.println("   - Check email and password from Facebook Service Gateway");

        try (final Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                final DataInputStream in = new DataInputStream(socket.getInputStream());
                final DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            out.writeUTF(email + DELIMITER + password);

            System.err.println(" - Sending data to '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + email + "'");

            final boolean inData = in.readBoolean();

            System.err.println(" - Getting translation from '" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "' -> '" + inData + "'");

            return inData;
        } catch (final IOException e) {
            System.err.println("# FacebookServiceGateway error: " + e.getMessage());

            return false;
        }
    }

}
