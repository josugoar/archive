package es.deusto.ingenieria.sd.strava.server.gateway;

import java.rmi.RemoteException;

public class GoogleServiceGateway implements IExternalLogin {
    
    private static final GoogleServiceGateway INSTANCE = new GoogleServiceGateway();

    private GoogleServiceGateway() {}

    public static GoogleServiceGateway getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean login(String email, String password) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }

}
