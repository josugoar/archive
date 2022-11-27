package es.deusto.ingenieria.sd.strava.server.gateway;

import java.rmi.RemoteException;

public interface IExternalLogin {
    public boolean login(String email, String password) throws RemoteException;
}
