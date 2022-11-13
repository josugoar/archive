package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.JDialog;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;

public class ActivityDialog extends JDialog {

    private ActivityController activityController;

    public ActivityDialog(ActivityController activityController) {
        this.activityController = activityController;
    }

    public void createActivity() {
    }

}
