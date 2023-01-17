package es.deusto.ingenieria.sd.strava.server.dao;

import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;

public interface IDAO {

    public void storeActivity(Activity activity);

    public Activity getActivity();

    public List<Activity> getActivity(String condition);

    public List<Activity> getActivities();

    public void updateActivity(Activity activity);

    public void deleteActivity(Activity activity);

    public void deleteActivities();

    public void storeAthlete(Athlete athlete);

    public Athlete getAthlete();

    public List<Athlete> getAthletes();

    public List<Athlete> getAthletes(String condition);

    public void updateAthlete(Athlete athlete);

    public void deleteAthlete(Athlete athlete);

    public void deleteAthletes();

    public void storeChallenge(Challenge challenge);

    public Challenge getChallenge();

    public List<Challenge> getChallenges();

    public List<Challenge> getChallenges(String condition);

    public void updateChallenge(Challenge challenge);

    public void deleteChallenge(Challenge challenge);

    public void deleteChallenges();

}
