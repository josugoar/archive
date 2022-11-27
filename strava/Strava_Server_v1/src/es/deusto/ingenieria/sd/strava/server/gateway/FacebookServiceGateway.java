package es.deusto.ingenieria.sd.strava.server.gateway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class FacebookServiceGateway implements IExternalLogin{

    private static final FacebookServiceGateway INSTANCE = new FacebookServiceGateway();
    private String ip;
    private int port;
    private static String separator = "$";

    private FacebookServiceGateway() {
        ip = "127.0.0.1";
        port  = 3200;
    }

    public static FacebookServiceGateway getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean login(String email, String password) throws RemoteException {
        // TODO Auto-generated method stub
        String message = email + separator + password;
        boolean exists = false;


        try (Socket s = new Socket(ip, port)){

            DataInputStream inStream = new DataInputStream(s.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());

            outputStream.writeUTF(message);
            System.err.println("Facebook Service Gateway: Sending to " + s.getInetAddress().getHostAddress() + ":" + s.getPort() + " the following data: " + "\"" + message + "\"");

            exists = inStream.readBoolean();
            System.err.println("Facebook Service Gateway: Receiving from " + s.getInetAddress().getHostAddress() + ":" + s.getPort() + " if the user exists: " + "\"" + exists + "\"");

        } catch (UnknownHostException e) {
			System.err.println("Facebook Service Gateway: Socket error: " + e.getMessage());
		} catch (EOFException e) {
			System.err.println("Facebook Service Gateway: EOF error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Facebook Service Gateway: IO error: " + e.getMessage());
		}

        return exists;

    }



}
