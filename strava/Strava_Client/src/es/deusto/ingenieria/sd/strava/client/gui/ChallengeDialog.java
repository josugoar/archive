package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.JDialog;

import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;

public class ChallengeDialog extends JDialog {

    private ChallengeController challengeController;

    public ChallengeDialog(ChallengeController challengeController) {
        this.challengeController = challengeController;
    }

    public void createChallenge() {
    }

}
