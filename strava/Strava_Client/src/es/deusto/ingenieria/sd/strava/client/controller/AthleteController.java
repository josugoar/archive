package es.deusto.ingenieria.sd.strava.client.controller;

import java.rmi.RemoteException;
import java.util.Date;

import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;

public class AthleteController {

    ServiceLocator serviceLocator;
    private Long token;

    public AthleteController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public boolean register(String email, String password, String name, Date birthDate, Float weight, Integer height,
            Integer restingHeartrate, Integer maxHeartrate) {
        try {
            this.token = this.serviceLocator.getService().register(email, password, name, birthDate, weight, height, restingHeartrate, maxHeartrate);
            return true;
        } catch (RemoteException e) {
            System.out.println("# Error during register: " + e);
            return false;
        }
    }

    public boolean registerGoogle(String email, String password, String name, Date birthDate, Float weight, Integer height,
            Integer restingHeartrate, Integer maxHeartrate) {
        try {
            this.token = this.serviceLocator.getService().registerGoogle(email, password, name, birthDate, weight, height, restingHeartrate, maxHeartrate);
            return true;
        } catch (RemoteException e) {
            System.out.println("# Error during register: " + e);
            return false;
        }
    }

    public boolean registerFacebook(String email, String password, String name, Date birthDate, Float weight, Integer height,
            Integer restingHeartrate, Integer maxHeartrate) {
        try {
            this.token = this.serviceLocator.getService().registerFacebook(email, password, name, birthDate, weight, height, restingHeartrate, maxHeartrate);
            return true;
        } catch (RemoteException e) {
            System.out.println("# Error during register: " + e);
            return false;
        }
    }

    public boolean login(String email, String password) {
        try {
            this.token = this.serviceLocator.getService().login(email, password);
            return true;
        } catch (RemoteException e) {
            System.out.println("# Error during login: " + e);
            return false;
        }
    }

    public void logout() {
        try {
            this.serviceLocator.getService().logout(this.token);
            this.token = null;
        } catch (RemoteException e) {
            System.out.println("# Error during logout: " + e);
        }
    }

    public AthleteDTO getAthlete() {
        try {
            return this.serviceLocator.getService().getAthlete(this.token);
        } catch (RemoteException e) {
            System.out.println("# Error getting athlete: " + e);
            return null;
        }
    }

    public long getToken() {
		return token;
	}

}
