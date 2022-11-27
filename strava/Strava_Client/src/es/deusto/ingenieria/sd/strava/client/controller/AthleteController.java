package es.deusto.ingenieria.sd.strava.client.controller;

import java.rmi.RemoteException;

import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;

public class AthleteController {

    ServiceLocator serviceLocator;

    private Long token;

    public AthleteController(final ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public boolean register(final String password, final AthleteDTO athleteDTO) {
        try {
            serviceLocator.getService().register(password, athleteDTO);

            return true;
        } catch (final RemoteException e) {
            System.err.println("# Error during register: " + e);

            return false;
        }
    }

    public boolean registerGoogle(final AthleteDTO athleteDTO) {
        try {
            serviceLocator.getService().registerGoogle(athleteDTO);

            return true;
        } catch (final RemoteException e) {
            System.err.println("# Error during register: " + e);

            return false;
        }
    }

    public boolean registerFacebook(final AthleteDTO athleteDTO) {
        try {
            serviceLocator.getService().registerFacebook(athleteDTO);

            return true;
        } catch (final RemoteException e) {
            System.err.println("# Error during register: " + e);

            return false;
        }
    }

    public boolean login(final String email, final String password) {
        try {
            token = serviceLocator.getService().login(email, password);

            return true;
        } catch (final RemoteException e) {
            System.err.println("# Error during login: " + e);

            return false;
        }
    }

    public void logout() {
        try {
            serviceLocator.getService().logout(token);

            token = null;
        } catch (final RemoteException e) {
            System.err.println("# Error during logout: " + e);
        }
    }

    public AthleteDTO getAthlete() {
        try {
            return serviceLocator.getService().getAthlete(token);
        } catch (final RemoteException e) {
            System.err.println("# Error getting athlete: " + e);
            return null;
        }
    }

    public long getToken() {
        return token;
    }

}
