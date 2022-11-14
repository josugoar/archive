package es.deusto.ingenieria.sd.strava.client.controller;

import java.rmi.RemoteException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

public class ChallengeController {

    ServiceLocator serviceLocator;

    public ChallengeController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ChallengeDTO createChallenge(long token, String name, Date startDate,
            Date endDate,
            Float distance,
            Duration time,
            boolean isCycling,
            boolean isRunning) {
        try {
            return this.serviceLocator.getService().createChallenge(token, name, startDate, endDate, distance, time, isCycling, isRunning);
        } catch (RemoteException e) {
            System.out.println("# Error creating an challenge: " + e);
            return null;
        }
    }

    public List<ChallengeDTO> getActiveChallenges(long token) {
        try {
            return this.serviceLocator.getService().getActiveChallenges(token);
        } catch (RemoteException e) {
            System.out.println("# Error creating an activity: " + e);
            return new ArrayList<>();
        }
    }

    public boolean acceptChallenge(long token, int challengeId) {
        try {
            this.serviceLocator.getService().acceptChallenge(token, challengeId);;
            return true;
        } catch (RemoteException e) {
            System.out.println("# Error creating an activity: " + e);
            return false;
        }
    }

    public float getChallengeState(long token, int challengeId) {
        try {
            return this.serviceLocator.getService().getChallengeState(token, challengeId);
        } catch (RemoteException e) {
            System.out.println("# Error creating an activity: " + e);
            return -1;
        }
    }

}
