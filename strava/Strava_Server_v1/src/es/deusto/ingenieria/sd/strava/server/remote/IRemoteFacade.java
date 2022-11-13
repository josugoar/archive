package es.deusto.ingenieria.sd.strava.server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

//This interface defines the API of the Server. It represents the Remote Facade pattern
public interface IRemoteFacade extends Remote {

	public long register(String email, String password, String name, Date birthDate, Float weight, Integer height,
			Integer restingHeartrate, Integer maxHeartrate) throws RemoteException;

	public long login(String email, String password) throws RemoteException;

	public void logout(long token) throws RemoteException;

	public AthleteDTO getAthlete(long token) throws RemoteException;

	public ActivityDTO createActivity(long token, String name, float distance, Duration elapsedTime, String type,
			Date startDate) throws RemoteException;

	public List<ActivityDTO> getActivities(long token) throws RemoteException;

	public ChallengeDTO createChallenge(long token, String name, Date startDate,
			Date endDate,
			Float distance,
			Duration time,
			boolean isCycling,
			boolean isRunning) throws RemoteException;

	public List<ChallengeDTO> getActiveChallenges(long token) throws RemoteException;

	public void acceptChallenge(long token, int challengeId) throws RemoteException;

	public float getChallengeState(long token, int challengeId) throws RemoteException;
}