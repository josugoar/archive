package es.deusto.ingenieria.sd.strava.server.external;

import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conection {
    private final int PORT = 1234;
    private final String HOST = "localhost";
    protected String message;
    protected ServerSocket ss;
    protected Socket cs;
    protected DataOutputStream output;

    public Conection(String type) throws IOException {
        if (type.equalsIgnoreCase("server")) {

            ss = new ServerSocket(PORT);
            cs = new Socket(); 

        } else {
            cs = new Socket(HOST, PORT);
        }
    }
     
}
