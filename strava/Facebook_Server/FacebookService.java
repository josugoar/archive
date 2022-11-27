import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class FacebookService  extends Thread{

    private Socket tcpServerSocket;
    private DataInputStream inStream;
    private DataOutputStream outStream;

    private static String separator = "$";

    public FacebookService(Socket socket) {

        try {

            tcpServerSocket = socket;
            inStream = new DataInputStream(tcpServerSocket.getInputStream());
            outStream = new DataOutputStream(tcpServerSocket.getOutputStream());
            start();

        } catch (IOException e) {
            // TODO: handle exception
            System.err.println("Facebook Service: Connection IO error: " + e.getMessage());
        }
    }

    public void run() {
        try {
            String inData = inStream.readUTF();
            System.out.println("Facebook Service: From " + tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getPort() + " received following data: " + "\"" +inData + "\"");
            boolean userExists = login(inData);
            outStream.writeBoolean(userExists);
            System.out.println("Facebook Service: To " + tcpServerSocket.getInetAddress().getHostAddress() + ":" + tcpServerSocket.getPort() + " sent following data: " + "\"" +inData + "\"");
        } catch (EOFException e) {
            // TODO: handle exception
            System.out.println("Facebook Service: EOF Error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Facebook Service: IO Error " + e.getMessage());
        } finally {

            try {
                tcpServerSocket.close();                
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Facebook Service: Couldn't close connection, IO Error: " + e.getMessage());
            }
        }
    }


    public boolean login(String user) {
        boolean exists = false;

        if (user != null && !user.trim().isEmpty()) {

            try {
                StringTokenizer tokenizer = new StringTokenizer(user, separator);
                String email = tokenizer.nextToken();
                String password = tokenizer.nextToken();
    
                if (email != null && password != null) {
                    FacebookLogin facebookLogin = new FacebookLogin();
                    exists = facebookLogin(email, password);
                    if (exists) {
                        System.out.println("User " + email + " exists");
                    } else {
                        System.out.println("User " + email + "does NOT exist");
                    }
    
                }
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Facebook Login: Error " + e.getMessage());
                exists = false;
            }
            return exists;
        }

    }

    
}
