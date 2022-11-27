package es.deusto.ingenieria.sd.strava.server.gateway;

import java.rmi.Naming;
import java.rmi.RemoteException;

import es.deusto.ingenieria.sd.strava.facebook.remote.IFacebook;

public class FacebookServiceGateway {

    private static FacebookServiceGateway instance;

    private IFacebook facebookService;

    private FacebookServiceGateway() {
        try {
            facebookService = (IFacebook) Naming.lookup("//127.0.0.1:1099/Facebook");
        } catch (final Exception e) {
            System.err.println("# Error locating remote service: " + e);
        }
    }

    public static FacebookServiceGateway getInstance() {
        if (instance == null) {
            instance = new FacebookServiceGateway();
        }

        return instance;
    }

    public boolean checkEmail(final String email) {
        System.out.println("   - Check email from Facebook Service Gateway");

        try {
            return facebookService.checkEmail(email);
        } catch (final RemoteException e) {
            System.out.println("   $ Error checking email: " + e.getMessage());
            return false;
        }
    }

    public boolean checkEmailAndPassword(final String email, final String password) {
        System.out.println("   - Check email and password from Facebook Service Gateway");

        try {
            return facebookService.checkEmailAndPassword(email, password);
        } catch (final RemoteException e) {
            System.out.println("   $ Error checking email and password: " + e.getMessage());
            return false;
        }
    }

}
