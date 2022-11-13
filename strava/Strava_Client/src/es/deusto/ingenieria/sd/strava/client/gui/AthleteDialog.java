package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.JDialog;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;

public class AthleteDialog extends JDialog {

    private AthleteController athleteController;

    public AthleteDialog(AthleteController athleteController) {
        this.athleteController = athleteController;
    }

    public void getAthlete() {
    }

}
