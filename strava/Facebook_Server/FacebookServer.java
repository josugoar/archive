package Facebook_Server;

import java.net.ServerSocket;
import java.io.IOException;


public class FacebookServer {


    private static int clients = 0;
    public static void main(String[] args) {

        int port = 3200;

        try (ServerSocket tcpServerSocket = new ServerSocket(port)){
            System.out.println(" Facebook Server: Awaiting connection '" + tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getLocalPort());
			while (true) {
                new FacebookService(tcpServerSocket.accept());
                System.out.println("Facebook Server: New client, number of clients connected: " + clients);  
            }
        } catch (IOException e) {
            // TODO: handle exception
            System.err.println("Facebook Server: IO Error: " + e.getMessage());
        }
        
    }
    
}
