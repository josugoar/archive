package es.deusto.ingenieria.sd.strava.server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

public interface IRemoteFacade extends Remote {

    public void register(String password, AthleteDTO athleteDTO) throws RemoteException;

    public void registerGoogle(AthleteDTO athleteDTO) throws RemoteException;

    public void registerFacebook(AthleteDTO athleteDTO) throws RemoteException;

    public long login(String email, String password) throws RemoteException;

    public void logout(long token) throws RemoteException;

    public void createActivity(long token, ActivityDTO activityDTO) throws RemoteException;

    public List<ActivityDTO> getActivities(long token) throws RemoteException;

    public AthleteDTO getAthlete(long token) throws RemoteException;

    public void createChallenge(long token, ChallengeDTO challengeDTO) throws RemoteException;

    public List<ChallengeDTO> getChallenges(long token) throws RemoteException;

    public void acceptChallenge(long token, ChallengeDTO challengeDTO) throws RemoteException;

    public double getChallengeProgress(long token, ChallengeDTO challengeDTO) throws RemoteException;

}