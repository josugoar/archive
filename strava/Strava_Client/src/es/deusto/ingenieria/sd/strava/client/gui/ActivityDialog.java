package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.JDialog;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;

public class ActivityDialog extends JDialog {

    private ActivityController activityController;
    private AthleteController athleteController;

    public ActivityDialog(ActivityController activityController, AthleteController athleteController) {
        this.activityController = activityController;
        this.athleteController = athleteController;
    }

    public void createActivity() {
    }

}
