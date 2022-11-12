package es.deusto.ingenieria.sd.strava.server.services;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;

public class ActivityAppService {

    public List<Activity> getActivities(Athlete athlete) {
        return athlete.getActivities();
    }

    public Activity createActivity(Athlete athlete, String name, float distance, Duration elapsedTime, String type,
            Date startDate) throws IllegalArgumentException {
        Activity activity = new Activity();
        activity.setDistance(distance);
        activity.setElapsedTime(elapsedTime);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setType(type);

        if (!activity.checkActivity()) {
            throw new IllegalArgumentException("Bad arguments!");
        }

        athlete.addActivities(activity);

        return activity;
    }

}
