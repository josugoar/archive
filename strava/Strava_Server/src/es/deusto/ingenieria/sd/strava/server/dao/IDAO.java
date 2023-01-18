package es.deusto.ingenieria.sd.strava.server.dao;

import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;

public interface IDAO {

    public void storeActivity(Activity activity);

    public Activity getActivity(String name, String email);

    public boolean containsActivity(String name, String email);

    public List<Activity> getActivities(String condition);

    public void deleteActivity(Activity activity);

    public void storeAthlete(Athlete athlete);

    public Athlete getAthlete(String email);

    public boolean containsAthlete(String email);

    public List<Athlete> getAthletes(String condition);

    public void deleteAthlete(Athlete athlete);

    public void storeChallenge(Challenge challenge);

    public Challenge getChallenge(String name);

    public boolean containsChallenge(String name);

    public List<Challenge> getChallenges(String condition);

    public void deleteChallenge(Challenge challenge);

}
