package es.deusto.ingenieria.sd.strava.client.remote;

import java.rmi.Naming;

import es.deusto.ingenieria.sd.strava.server.remote.IRemoteFacade;

public class ServiceLocator {

    private IRemoteFacade service;

    public void setService(final String ip, final String port, final String serviceName) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            service = (IRemoteFacade) Naming.lookup("//" + ip + ":" + port + "/" + serviceName);
        } catch (final Exception e) {
            System.err.println("# Error locating remote facade: " + e);
        }
    }

    public IRemoteFacade getService() {
        return service;
    }

}
