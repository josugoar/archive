package es.deusto.ingenieria.sd.strava.server.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;
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
				throw new RemoteException("User is already logged in!");
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
			throw new RemoteException("User is not logged in!");
		}
	}

	@Override
	public long register(String email, String password, String name, Date birthDate) throws RemoteException {
		return 0;
	}

	@Override
	public void createActivity(long token, ActivityDTO activity) throws RemoteException {

	}

	@Override
	public void createChallenge(long token, ChallengeDTO challenge) throws RemoteException {

	}

	@Override
	public List<ChallengeDTO> getActiveChallenges(long token) throws RemoteException {
		if (this.serverState.containsKey(token)) {
			//Logout means remove the User from Server State
			return ChallengeAssembler.getInstance().challengeToDTO(challengeService.getActiveChallenges(serverState.get(token)));
		} else {
			throw new RemoteException("User is not logged in!");
		}
	}

	@Override
	public void acceptChallenge(long token, ChallengeDTO challenge) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public float getChallengeState(ChallengeDTO challenge) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
}