package es.deusto.ingenieria.sd.strava.server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

//This interface defines the API of the Server. It represents the Remote Facade pattern
public interface IRemoteFacade extends Remote {

	public long register(String email, String password, String name, Date birthDate) throws RemoteException;

	public long login(String email, String password) throws RemoteException;

	public void logout(long token)  throws RemoteException;

	public void createActivity(long token, ActivityDTO activity) throws RemoteException;

	public void createChallenge(long token, ChallengeDTO challenge) throws RemoteException;

	public List<ChallengeDTO> getActiveChallenges(long token) throws RemoteException;

	public void acceptChallenge(long token, ChallengeDTO challenge) throws RemoteException;

	public float getChallengeState(ChallengeDTO challenge) throws RemoteException;
}