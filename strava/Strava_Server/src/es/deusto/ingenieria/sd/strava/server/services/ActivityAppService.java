package es.deusto.ingenieria.sd.strava.server.services;

import java.util.List;

import es.deusto.ingenieria.sd.strava.server.dao.DAO;
import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;

public class ActivityAppService {

    private static ActivityAppService instance;

    private ActivityAppService() {
    }

    public static ActivityAppService getInstance() {
        if (instance == null) {
            instance = new ActivityAppService();
        }

        return instance;
    }

    public void createActivity(Athlete athlete, final Activity activity) throws IllegalArgumentException {
        athlete = DAO.getInstance().getAthlete(athlete.getEmail());
        activity.setEmail(athlete.getEmail());
        if (!DAO.getInstance().containsActivity(activity.getName(), activity.getEmail())) {
            athlete.addActivity(activity);
            DAO.getInstance().storeAthlete(athlete);
            DAO.getInstance().storeActivity(activity);
        }
    }

    public List<Activity> getActivities(Athlete athlete) {
        athlete = DAO.getInstance().getAthlete(athlete.getEmail());
        return List.copyOf(athlete.getActivities());
    }

}
