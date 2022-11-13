package es.deusto.ingenieria.sd.strava.client.gui;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;

public class MainWindow {

    private ActivityController activityController;
    private AthleteController athleteController;
    private ChallengeController challengeController;

    public MainWindow(ActivityController activityController, AthleteController athleteController, ChallengeController challengeController) {
        this.activityController = activityController;
        this.athleteController = athleteController;
        this.challengeController = challengeController;
    }

    public void getActivities() {
    }

    public void getActiveChallenges() {
    }

    public void logout() {
    }

}
