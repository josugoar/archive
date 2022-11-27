package es.deusto.ingenieria.sd.auctions.facebook.server;

import java.rmi.Naming;

import es.deusto.ingenieria.sd.auctions.facebook.remote.FacebookService;
import es.deusto.ingenieria.sd.auctions.facebook.remote.IFacebook;

public class FacebookServer {

    public static void main(final String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        final String name = "//" + args[0] + ":" + args[1] + "/" + args[2];

        try {
            final IFacebook remoteObject = FacebookService.getInstance();
            Naming.rebind(name, remoteObject);
            System.out.println(" * Facebook Server '" + name + "' started!!");
        } catch (final Exception e) {
            System.out.println(" # Facebook Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
