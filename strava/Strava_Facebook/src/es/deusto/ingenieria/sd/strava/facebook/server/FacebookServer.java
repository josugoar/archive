package es.deusto.ingenieria.sd.strava.facebook.server;

import java.io.IOException;
import java.net.ServerSocket;

import es.deusto.ingenieria.sd.strava.facebook.socket.FacebookService;

public class FacebookServer {

    private static int numClients = 0;

    public static void main(final String[] args) {
        if (args.length < 1) {
            System.err.println(" # Usage: FacebookServer [PORT]");
            System.exit(1);
        }

        // args[1] = Server socket port
        final int serverPort = Integer.parseInt(args[0]);

        try (ServerSocket tcpEchoServerSocket = new ServerSocket(serverPort);) {
            System.out.println(
                    " - FacebookServer: Waiting for connections '" + tcpEchoServerSocket.getInetAddress().getHostAddress()
                            + ":" + tcpEchoServerSocket.getLocalPort() + "' ...");

            while (true) {
                new FacebookService(tcpEchoServerSocket.accept());
                System.out.println(" - FacebookServer: New client connection accepted. Client Number: " + numClients++);
            }
        } catch (final IOException e) {
            System.out.println("# FacebookServer: IO error:" + e.getMessage());
        }
    }

}
