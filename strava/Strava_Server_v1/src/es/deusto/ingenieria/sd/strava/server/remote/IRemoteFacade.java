package es.deusto.ingenieria.sd.strava.server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

//This interface defines the API of the Server. It represents the Remote Facade pattern
public interface IRemoteFacade extends Remote {

	public long register(String email, String password, String name, Date birthDate, float weight, int height,
			int restingHeartrate, int maxHeartrate) throws RemoteException;

	public long login(String email, String password) throws RemoteException;

	public void logout(long token) throws RemoteException;

	public void createActivity(long token, String name, float distance, Duration elapsedTime, String type,
			Date startDate) throws RemoteException;

	public void createChallenge(long token, Date startDate,
			Date endDate,
			float distance,
			Duration time,
			boolean isCycling,
			boolean isRunning) throws RemoteException;

	public List<ChallengeDTO> getActiveChallenges(long token) throws RemoteException;

	public void acceptChallenge(long token, int challenge) throws RemoteException;

	public float getChallengeState(int challenge) throws RemoteException;
}