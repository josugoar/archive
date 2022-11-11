package es.deusto.ingenieria.sd.strava.server.services;

import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;

public class ActivityAppService {

    public List<Activity> getActivities(Athlete athlete) {
        return athlete.getActivities();
    }

    public void createActivity(Athlete athlete, Activity activity) {
        athlete.addActivities(activity);
    }

}
