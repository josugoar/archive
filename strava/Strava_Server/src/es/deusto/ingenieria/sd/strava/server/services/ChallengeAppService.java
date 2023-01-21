package es.deusto.ingenieria.sd.strava.server.services;

import java.util.Date;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.dao.DAO;
import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;

public class ChallengeAppService {

    private static ChallengeAppService instance;

    private ChallengeAppService() {
    }

    public static ChallengeAppService getInstance() {
        if (instance == null) {
            instance = new ChallengeAppService();
        }

        return instance;
    }

    public void createChallenge(final Athlete athlete, final Challenge challenge) throws IllegalArgumentException {
        if (DAO.getInstance().containsChallenge(challenge.getName())) {
            throw new IllegalArgumentException("Challenge is already created!");
        }

        DAO.getInstance().storeChallenge(challenge);
    }

    public List<Challenge> getChallenges() {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        return DAO.getInstance().getChallenges("'" + date + "' > startDate AND '" + date + "' < endDate");
    }

    public void acceptChallenge(final Athlete athlete, final Challenge challenge) throws IllegalArgumentException {
        if (!DAO.getInstance().containsChallenge(challenge.getName())) {
            throw new IllegalArgumentException("Challenge is not created!");
        }

        athlete.addChallenge(challenge);
        DAO.getInstance().storeAthlete(athlete);
    }

    public double getChallengeProgress(final Athlete athlete, final Challenge challenge) {
        if (!athlete.hasChallenge(challenge)) {
            return -1;
        }

        double progress = 0;
        for (final Activity activity : athlete.getActivities()) {
            if (challenge.hasType(activity.getType())
                    && activity.getStartDate().after(challenge.getStartDate())
                    && activity.getStartDate().before(challenge.getEndDate())) {
                if (challenge.getDistance() != null) {
                    progress += activity.getDistance();
                } else if (challenge.getTime() != null) {
                    progress += activity.getElapsedTime();
                }
            }
        }

        if (challenge.getDistance() != null) {
            return progress / challenge.getDistance();
        } else if (challenge.getTime() != null) {
            return progress / challenge.getTime();
        }

        throw new IllegalArgumentException("Invalid challenge!");
    }

}
