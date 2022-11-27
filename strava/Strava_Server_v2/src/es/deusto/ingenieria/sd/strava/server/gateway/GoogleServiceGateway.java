package es.deusto.ingenieria.sd.strava.server.gateway;

import java.rmi.Naming;
import java.rmi.RemoteException;

import es.deusto.ingenieria.sd.strava.google.remote.IGoogle;

public class GoogleServiceGateway {

    private static GoogleServiceGateway instance;

    private static final String SERVER_IP = "127.0.0.1";
    private static final String SERVER_PORT = "8001";
    private static final String SERVER_NAME = "8001";

    private IGoogle googleService;

    private GoogleServiceGateway() {
        try {
            googleService = (IGoogle) Naming.lookup("//" + SERVER_IP + ":" + SERVER_PORT + "/" + SERVER_NAME);
        } catch (final Exception e) {
            System.err.println("# Error locating remote service: " + e);
        }
    }

    public static GoogleServiceGateway getInstance() {
        if (instance == null) {
            instance = new GoogleServiceGateway();
        }

        return instance;
    }

    public boolean checkEmail(final String email) {
        System.err.println("   - Check email from Google Service Gateway");

        try {
            return googleService.checkEmail(email);
        } catch (final RemoteException e) {
            System.err.println("   $ Error checking email: " + e.getMessage());
            return false;
        }
    }

    public boolean checkEmailAndPassword(final String email, final String password) {
        System.err.println("   - Check email and password from Google Service Gateway");

        try {
            return googleService.checkEmailAndPassword(email, password);
        } catch (final RemoteException e) {
            System.err.println("   $ Error checking email and password: " + e.getMessage());
            return false;
        }
    }

}
