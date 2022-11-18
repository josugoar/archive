package es.deusto.ingenieria.sd.strava.server.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;
import es.deusto.ingenieria.sd.strava.server.data.domain.SportType;

public class ChallengeAppService {

    private Map<Integer, Challenge> state = new HashMap<>();

    public List<Challenge> getActiveChallenges() {
        List<Challenge> challenges = new ArrayList<>();
        for (Challenge challenge : state.values()) {
            if (challenge.isActive()) {
                challenges.add(challenge);
            }
        }
        return challenges;

    }

    public Challenge createChallenge(Athlete athlete, String name, Date startDate, Date endDate, Float distance, Duration time,
            Set<String> type) throws IllegalArgumentException {
        System.out.println("Creating challenge in ChallengeAppService");
        Challenge challenge = new Challenge();
        for (String sportType : type) {
            challenge.addType(SportType.fromString(sportType));
        }
        challenge.setDistance(distance);
        challenge.setEndDate(endDate);
        challenge.setName(name);
        challenge.setStartDate(startDate);
        challenge.setTime(time);
        challenge.setId(state.size());

        if (!challenge.checkChallenge()) {
            throw new IllegalArgumentException("Bad arguments!");
        }

        state.put(challenge.getId(), challenge);
        athlete.addChallenge(challenge);

        return challenge;
    }

    public void acceptChallenge(Athlete athlete, int challengeId) throws IllegalArgumentException {
        Challenge challenge = state.get(challengeId);
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge does not exist!");
        }

        if (athlete.getChallenges().contains(challenge)) {
            throw new IllegalArgumentException("Athlete already in challenge!");
        }

        athlete.addChallenge(challenge);
    }

    public float getChallengeState(Athlete athlete, int challengeId) throws IllegalArgumentException {
        Challenge challenge = state.get(challengeId);
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge does not exist!");
        }

        float distance = 0;
        Duration duration = Duration.ZERO;
        for (Activity activity : athlete.getActivities()) {
            if (activity.getType()== SportType.RUNNING && challenge.getType().contains(SportType.RUNNING) || activity.getType()== SportType.CYCLING && challenge.getType().contains(SportType.CYCLING)) {
                if (activity.getStartDate().after(challenge.getStartDate()) && activity.getStartDate().before(challenge.getEndDate())) {
                    if (challenge.getDistance() != null) {
                        distance += activity.getDistance();
                    } else {
                        duration = duration.plus(activity.getElapsedTime());
                    }
                }
            }
        }

        if (challenge.getDistance() != null) {
            if (challenge.getDistance() == 0) {
                return 1;
            }
            return distance / challenge.getDistance();
        } else {
            if (challenge.getTime().isZero()) {
                return 1;
            }
            return ((float) duration.toNanos()) / ((float) challenge.getTime().toNanos());
        }
    }

}
