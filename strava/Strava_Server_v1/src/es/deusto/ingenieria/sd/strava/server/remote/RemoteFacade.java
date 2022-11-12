package es.deusto.ingenieria.sd.strava.server.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeAssembler;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;
import es.deusto.ingenieria.sd.strava.server.services.ActivityAppService;
import es.deusto.ingenieria.sd.strava.server.services.AthleteAppService;
import es.deusto.ingenieria.sd.strava.server.services.ChallengeAppService;

public class RemoteFacade extends UnicastRemoteObject implements IRemoteFacade {
	private static final long serialVersionUID = 1L;

	//Data structure for manage Server State
	private Map<Long, Athlete> serverState = new HashMap<>();

	private ActivityAppService activityService = new ActivityAppService();
	private AthleteAppService athleteService = new AthleteAppService();
	private ChallengeAppService challengeService = new ChallengeAppService();

	public RemoteFacade() throws RemoteException {
		super();
	}

	@Override
	public synchronized long login(String email, String password) throws RemoteException {
		System.out.println(" * RemoteFacade login(): " + email + " / " + password);

		//Perform login() using LoginAppService
		Athlete user = athleteService.login(email, password);

		//If login() success user is stored in the Server State
		if (user != null) {
			//If user is not logged in
			if (!this.serverState.values().contains(user)) {
				Long token = Calendar.getInstance().getTimeInMillis();
				this.serverState.put(token, user);
				return(token);
			} else {
				throw new RemoteException("Athlete is already logged in!");
			}
		} else {
			throw new RemoteException("Login fails!");
		}

	}

	@Override
	public synchronized void logout(long token) throws RemoteException {
		System.out.println(" * RemoteFacade logout(): " + token);

		if (this.serverState.containsKey(token)) {
			//Logout means remove the User from Server State
			this.serverState.remove(token);
		} else {
			throw new RemoteException("Athlete is not logged in!");
		}
	}

	@Override
	public synchronized List<ChallengeDTO> getActiveChallenges(long token) throws RemoteException {
		if (this.serverState.containsKey(token)) {
			return ChallengeAssembler.getInstance().challengeToDTO(challengeService.getActiveChallenges(serverState.get(token)));
		} else {
			throw new RemoteException("Athlete is not logged in!");
		}
	}

		@Override
	public synchronized long register(String email, String password, String name, Date birthDate, float weight, int height,
			int restingHeartrate, int maxHeartrate) throws RemoteException {
		return athleteService.register(email, password, name, birthDate, weight, height, restingHeartrate, maxHeartrate);
	}

	@Override
	public synchronized void createActivity(long token, String name, float distance, Duration elapsedTime, String type,
			Date startDate) throws RemoteException {
		if (this.serverState.containsKey(token)) {
			if (!activityService.createActivity(serverState.get(token), name, distance, elapsedTime, type, startDate))
			{
				throw new RemoteException("Bad arguments in call");
			}
		} else {
			throw new RemoteException("Athlete is not logged in!");
		}

	}

	@Override
	public synchronized void createChallenge(long token, Date startDate, Date endDate, float distance, Duration time,
			boolean isCycling, boolean isRunning) throws RemoteException {
		if (this.serverState.containsKey(token)) {
			if (!challengeService.createChallenge(serverState.get(token), startDate, endDate, distance, time, isRunning, isCycling))
			{
				throw new RemoteException("Bad arguments in call");
			}
		} else {
			throw new RemoteException("Athlete is not logged in!");
		}

	}

	@Override
	public synchronized void acceptChallenge(long token, int challengeId) throws RemoteException {
		if (this.serverState.containsKey(token)) {
			Athlete athlete = serverState.get(token);
			if (!challengeService.acceptChallenge(athlete, challengeService.getChallenge(challengeId)))
			{
				throw new RemoteException("Challenge already accepted!");
			}
		} else {
			throw new RemoteException("Athlete is not logged in!");
		}
	}

	@Override
	public synchronized float getChallengeState(int challengeId) throws RemoteException {
		return challengeService.getChallengeState(challengeService.getChallenge(challengeId));
	}
}