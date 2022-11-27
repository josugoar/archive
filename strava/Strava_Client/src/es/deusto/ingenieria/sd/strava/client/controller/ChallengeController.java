package es.deusto.ingenieria.sd.strava.client.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

public class ChallengeController {

    ServiceLocator serviceLocator;

    public ChallengeController(final ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public void createChallenge(final long token, final ChallengeDTO challengeDTO) {
        try {
            serviceLocator.getService().createChallenge(token, challengeDTO);
        } catch (final RemoteException e) {
            System.err.println("# Error creating an challenge: " + e);
        }
    }

    public List<ChallengeDTO> getChallenges(final long token) {
        try {
            return serviceLocator.getService().getChallenges(token);
        } catch (final RemoteException e) {
            System.err.println("# Error getting challenges: " + e);

            return new ArrayList<>();
        }
    }

    public boolean acceptChallenge(final long token, final ChallengeDTO challengeDTO) {
        try {
            serviceLocator.getService().acceptChallenge(token, challengeDTO);;

            return true;
        } catch (final RemoteException e) {
            System.err.println("# Error accepting challenge: " + e);

            return false;
        }
    }

    public double getChallengeProgress(final long token, final ChallengeDTO challengeDTO) {
        try {
            return this.serviceLocator.getService().getChallengeProgress(token, challengeDTO);
        } catch (final RemoteException e) {
            System.err.println("# Error getting challenge progress: " + e);

            return -1;
        }
    }

}
