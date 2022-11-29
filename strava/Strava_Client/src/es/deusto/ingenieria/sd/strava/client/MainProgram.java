package es.deusto.ingenieria.sd.strava.client;

import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;
import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;
import es.deusto.ingenieria.sd.strava.client.gui.ActivityDialog;
import es.deusto.ingenieria.sd.strava.client.gui.AthleteDialog;
import es.deusto.ingenieria.sd.strava.client.gui.ChallengeDialog;
import es.deusto.ingenieria.sd.strava.client.gui.CompleteRegisterWindow;
import es.deusto.ingenieria.sd.strava.client.gui.LoginWindow;
import es.deusto.ingenieria.sd.strava.client.gui.MainWindowActivities;
import es.deusto.ingenieria.sd.strava.client.gui.MainWindowChallenges;
import es.deusto.ingenieria.sd.strava.client.gui.RegisterWindow;



public class MainProgram {

	public static void main(String[] args) {
		ServiceLocator serviceLocator = new ServiceLocator();

		//args[0] = RMIRegistry IP
		//args[1] = RMIRegistry Port
		//args[2] = Service Name
		if (args.length >= 3) {
			serviceLocator.setService(args[0], args[1], args[2]);
		} else {
			System.err.println("# Enter arguments to locate remote facade");
		}

		ActivityController activityController = new ActivityController(serviceLocator);
        AthleteController athleteController = new AthleteController(serviceLocator);
        ChallengeController challengeController = new ChallengeController(serviceLocator);

        RegisterWindow registerWindow = new RegisterWindow();
		CompleteRegisterWindow completeRegisterWindow = new CompleteRegisterWindow(athleteController);
        LoginWindow loginWindow = new LoginWindow(athleteController);
        MainWindowActivities mActivities = new MainWindowActivities(activityController, athleteController);
		MainWindowChallenges mChallenges = new MainWindowChallenges(athleteController, challengeController);
        AthleteDialog athleteDialog = new AthleteDialog(athleteController);
        ActivityDialog activityDialog = new ActivityDialog(activityController, athleteController);
        ChallengeDialog challengeDialog = new ChallengeDialog(challengeController, athleteController);


		registerWindow.setLoginWindow(loginWindow);
		registerWindow.setCompleteRegisterWindow(completeRegisterWindow);

		completeRegisterWindow.setMainWindow(mActivities);
		completeRegisterWindow.setRegisterWindow(registerWindow);

		loginWindow.setRegisterWindow(registerWindow);
		loginWindow.setMainWindow(mActivities);

		mActivities.setLoginWindow(loginWindow);
		mActivities.setActivityDialog(activityDialog);
		mActivities.setAthleteDialog(athleteDialog);
		mActivities.setMainWindowChallenges(mChallenges);

		mChallenges.setLoginWindow(loginWindow);
		mChallenges.setChallengeDialog(challengeDialog);
		mChallenges.setAthleteDialog(athleteDialog);
		mChallenges.setMainWindowActivities(mActivities);

		loginWindow.setVisible(true);
	}
}