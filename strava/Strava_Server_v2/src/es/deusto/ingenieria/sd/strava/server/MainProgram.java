package es.deusto.ingenieria.sd.strava.server;

import java.rmi.Naming;

import es.deusto.ingenieria.sd.strava.server.remote.IRemoteFacade;
import es.deusto.ingenieria.sd.strava.server.remote.RemoteFacade;

public class MainProgram {

    public static void main(final String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // args[0] = RMIRegistry IP
        // args[1] = RMIRegistry Port
        // args[2] = Service Name
        final String name = "//" + args[0] + ":" + args[1] + "/" + args[2];

        try {
            final IRemoteFacade remoteFacade = new RemoteFacade();
            Naming.rebind(name, remoteFacade);
            System.err.println(" * Strava Server v1 '" + name + "' started!!");
        } catch (final Exception e) {
            System.err.println(" # Strava Server Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
