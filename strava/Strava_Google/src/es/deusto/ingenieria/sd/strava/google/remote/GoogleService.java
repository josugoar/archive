package es.deusto.ingenieria.sd.strava.google.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class GoogleService extends UnicastRemoteObject implements IGoogle {

    private static GoogleService instance;

    // TODO
    private final Map<String, String> serverState = new HashMap<>() {
        {
            put("a@gmail.com", "12345");
            put("b@gmail.com", "67890");
            put("c@gmail.com", "password");
            put("tyfaghjsxb@gmail.com", "ksjchk");
        }
    };

    private GoogleService() throws RemoteException {
        super();
    }

    public static GoogleService getInstance() {
        if (instance == null) {
            try {
                instance = new GoogleService();
            } catch (final RemoteException e) {
                System.err.println("  # Error initializing service(): " + e.getMessage());
            }
        }

        return instance;
    }

    @Override
    public boolean checkEmail(final String email) throws RemoteException {
        return serverState.containsKey(email);
    }

    @Override
    public boolean checkEmailAndPassword(final String email, final String password) throws RemoteException {
        return serverState.containsKey(email) && serverState.get(email).equals(password);
    }

}
