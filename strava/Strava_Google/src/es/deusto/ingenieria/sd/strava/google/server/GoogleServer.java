package es.deusto.ingenieria.sd.strava.google.server;

import java.rmi.Naming;

import es.deusto.ingenieria.sd.strava.google.remote.GoogleService;
import es.deusto.ingenieria.sd.strava.google.remote.IGoogle;

public class GoogleServer {

    public static void main(final String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        final String name = "//" + args[0] + ":" + args[1] + "/" + args[2];

        try {
            final IGoogle remoteObject = GoogleService.getInstance();
            Naming.rebind(name, remoteObject);
            System.err.println(" * Google Exchange Server '" + name + "' started!!");
        } catch (final Exception ex) {
            System.err.println(" # Google Exchange Server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
