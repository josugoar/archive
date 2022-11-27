package es.deusto.ingenieria.sd.strava.facebook.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFacebook extends Remote {

    public boolean checkEmail(String email) throws RemoteException;

    public boolean checkEmailAndPassword(String email, String password) throws RemoteException;

}
