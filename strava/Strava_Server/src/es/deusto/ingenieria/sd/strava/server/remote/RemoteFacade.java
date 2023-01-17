package es.deusto.ingenieria.sd.strava.server.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityAssembler;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteAssembler;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeAssembler;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;
import es.deusto.ingenieria.sd.strava.server.services.ActivityAppService;
import es.deusto.ingenieria.sd.strava.server.services.AthleteAppService;
import es.deusto.ingenieria.sd.strava.server.services.ChallengeAppService;

public class RemoteFacade extends UnicastRemoteObject implements IRemoteFacade {

    private final Map<Long, Athlete> serverState = new HashMap<>();

    public RemoteFacade() throws RemoteException {
        super();
    }

    @Override
    public synchronized void register(final String password, final AthleteDTO athleteDTO) throws RemoteException {
        System.err.println(" * RemoteFacade register: " + password + " / " + athleteDTO);

        if (password == null) {
            throw new RemoteException("password is null!");
        }

        if (athleteDTO == null) {
            throw new RemoteException("athleteDTO is null!");
        }

        try {
            AthleteAppService.getInstance().register(
                    password, AthleteAssembler.getInstance().DTOToAthlete(athleteDTO));
        } catch (final IllegalArgumentException e) {
            throw new RemoteException("register() fails!", e);
        }
    }

    @Override
    public synchronized void registerGoogle(final AthleteDTO athleteDTO) throws RemoteException {
        System.err.println(" * RemoteFacade registerGoogle: " + athleteDTO);

        if (athleteDTO == null) {
            throw new RemoteException("athleteDTO is null!");
        }

        try {
            AthleteAppService.getInstance().registerGoogle(
                    AthleteAssembler.getInstance().DTOToAthlete(athleteDTO));
        } catch (final IllegalArgumentException e) {
            throw new RemoteException("register() fails!", e);
        }
    }

    @Override
    public synchronized void registerFacebook(final AthleteDTO athleteDTO) throws RemoteException {
        System.err.println(" * RemoteFacade registerFacebook: " + athleteDTO);

        if (athleteDTO == null) {
            throw new RemoteException("athleteDTO is null!");
        }

        try {
            AthleteAppService.getInstance().registerFacebook(
                    AthleteAssembler.getInstance().DTOToAthlete(athleteDTO));
        } catch (final IllegalArgumentException e) {
            throw new RemoteException("register() fails!", e);
        }
    }

    @Override
    public synchronized long login(final String email, final String password) throws RemoteException {
        System.err.println(" * RemoteFacade login: " + email + " / " + password);

        if (email == null) {
            throw new RemoteException("email is null!");
        }

        if (password == null) {
            throw new RemoteException("password is null!");
        }

        final Athlete athlete;
        try {
            athlete = AthleteAppService.getInstance().login(email, password);
        } catch (final IllegalArgumentException e) {
            throw new RemoteException("login() fails!", e);
        }

        if (serverState.containsValue(athlete)) {
            throw new RemoteException("Athlete is already logged in!");
        }

        final long token = Calendar.getInstance().getTimeInMillis();

        serverState.put(token, athlete);

        return token;
    }

    @Override
    public synchronized void logout(final long token) throws RemoteException {
        System.err.println(" * RemoteFacade logout:" + token);

        if (!serverState.containsKey(token)) {
            throw new RemoteException("Athlete is not logged in!");
        }

        serverState.remove(token);
    }

    @Override
    public synchronized void createActivity(final long token, final ActivityDTO activityDTO) throws RemoteException {
        System.err.println(" * RemoteFacade createActivity: " + token + " / " + activityDTO);

        if (activityDTO == null) {
            throw new RemoteException("activityDTO is null!");
        }

        if (!serverState.containsKey(token)) {
            throw new RemoteException("Athlete is not logged in!");
        }

        try {
            ActivityAppService.getInstance().createActivity(
                    serverState.get(token), ActivityAssembler.getInstance().DTOToActivity(activityDTO));
        } catch (final IllegalArgumentException e) {
            throw new RemoteException("createActivity() fails!", e);
        }
    }

    @Override
    public synchronized List<ActivityDTO> getActivities(final long token) throws RemoteException {
        System.err.println(" * RemoteFacade getActivities: " + token);

        if (!serverState.containsKey(token)) {
            throw new RemoteException("Athlete is not logged in!");
        }

        return ActivityAssembler.getInstance()
                .activityToDTO(ActivityAppService.getInstance().getActivities(serverState.get(token)));
    }

    @Override
    public synchronized AthleteDTO getAthlete(final long token) throws RemoteException {
        System.err.println(" * RemoteFacade getAthlete: " + token);

        if (!serverState.containsKey(token)) {
            throw new RemoteException("Athlete is not logged in!");
        }

        return AthleteAssembler.getInstance().athleteToDTO(serverState.get(token));
    }

    @Override
    public synchronized void createChallenge(final long token, final ChallengeDTO challengeDTO) throws RemoteException {
        System.err.println(" * RemoteFacade createChallenge: " + token + " / " + challengeDTO);

        if (challengeDTO == null) {
            throw new RemoteException("challengeDTO is null!");
        }

        if (!serverState.containsKey(token)) {
            throw new RemoteException("Athlete is not logged in!");
        }

        try {
            ChallengeAppService.getInstance().createChallenge(
                    serverState.get(token), ChallengeAssembler.getInstance().DTOToChallenge(challengeDTO));
        } catch (final IllegalArgumentException e) {
            throw new RemoteException("createChallenge() fails!", e);
        }
    }

    @Override
    public synchronized List<ChallengeDTO> getChallenges(final long token) throws RemoteException {
        System.err.println(" * RemoteFacade getChallenges: " + token);

        if (!serverState.containsKey(token)) {
            throw new RemoteException("Athlete is not logged in!");
        }

        return ChallengeAssembler.getInstance().challengeToDTO(ChallengeAppService.getInstance().getChallenges());
    }

    @Override
    public synchronized void acceptChallenge(final long token, final ChallengeDTO challengeDTO) throws RemoteException {
        System.err.println(" * RemoteFacade acceptChallenge: " + token + " / " + challengeDTO);

        if (challengeDTO == null) {
            throw new RemoteException("challengeDTO is null!");
        }

        if (!serverState.containsKey(token)) {
            throw new RemoteException("Athlete is not logged in!");
        }

        try {
            ChallengeAppService.getInstance().acceptChallenge(
                    serverState.get(token), ChallengeAssembler.getInstance().DTOToChallenge(challengeDTO));
        } catch (final IllegalArgumentException e) {
            throw new RemoteException("acceptChallenge() fails!", e);
        }
    }

    @Override
    public synchronized double getChallengeProgress(final long token, final ChallengeDTO challengeDTO)
            throws RemoteException {
        System.err.println(" * RemoteFacade getChallengeProgress: " + token + " / " + challengeDTO);

        if (challengeDTO == null) {
            throw new RemoteException("challengeDTO is null!");
        }

        if (!serverState.containsKey(token)) {
            throw new RemoteException("Athlete is not logged in!");
        }

        return ChallengeAppService.getInstance().getChallengeProgress(
                serverState.get(token), ChallengeAssembler.getInstance().DTOToChallenge(challengeDTO));
    }

}
