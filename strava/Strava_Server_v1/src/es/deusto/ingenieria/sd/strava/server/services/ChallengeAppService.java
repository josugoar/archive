package es.deusto.ingenieria.sd.strava.server.services;

import java.util.ArrayList;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;

public class ChallengeAppService {

    public List<Challenge> getActiveChallenges(Athlete athlete) {
        List<Challenge> challenges = new ArrayList<>();
        for (Challenge challenge : athlete.getChallenges()) {
            if (challenge.isActive()) {
                challenges.add(challenge);
            }
        }
        return challenges;

    }

    public void createChallenge(Athlete athlete, Challenge challenge) {
        athlete.addChallenge(challenge);
    }

    public void acceptChallenge(Athlete athlete, Challenge challenge) {
        athlete.addChallenge(challenge);
    }

    public static float getChallengeState(Challenge challenge) {
        return 0;
    }

}
