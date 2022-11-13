package es.deusto.ingenieria.sd.strava.client.test;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;
import es.deusto.ingenieria.sd.strava.client.gui.ActivityDialog;
import es.deusto.ingenieria.sd.strava.client.gui.AthleteDialog;
import es.deusto.ingenieria.sd.strava.client.gui.ChallengeDialog;
import es.deusto.ingenieria.sd.strava.client.gui.LoginWindow;
import es.deusto.ingenieria.sd.strava.client.gui.MainWindow;
import es.deusto.ingenieria.sd.strava.client.gui.RegisterWindow;
import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;

public class LocalTest {


    public static void main(String[] args) {
        ServiceLocator serviceLocator = new ServiceLocator();

        ActivityController activityController = new ActivityController(serviceLocator);
        AthleteController athleteController = new AthleteController(serviceLocator);
        ChallengeController challengeController = new ChallengeController(serviceLocator);

        RegisterWindow registerWindow = new RegisterWindow(athleteController);
        LoginWindow loginWindow = new LoginWindow(athleteController);
        MainWindow mainWindow = new MainWindow(activityController, athleteController, challengeController);
        AthleteDialog athleteDialog = new AthleteDialog(athleteController);
        ActivityDialog activityDialog = new ActivityDialog(activityController, athleteController);
        ChallengeDialog challengeDialog = new ChallengeDialog(challengeController, athleteController);
    }
}
