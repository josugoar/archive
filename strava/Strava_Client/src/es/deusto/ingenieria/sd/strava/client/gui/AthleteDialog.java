package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.*;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;

public class AthleteDialog extends JDialog {

    private AthleteController athleteController;

    public AthleteDialog(AthleteController athleteController) {
        this.athleteController = athleteController;
    }


    public AthleteDTO getAthlete() {
        return athleteController.getAthlete();
    }

}
