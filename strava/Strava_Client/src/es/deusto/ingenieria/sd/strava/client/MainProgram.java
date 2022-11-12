package es.deusto.ingenieria.sd.strava.client;

import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;
import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;


public class MainProgram {

	public static void main(String[] args) {
		ServiceLocator serviceLocator = new ServiceLocator();

		//args[0] = RMIRegistry IP
		//args[1] = RMIRegistry Port
		//args[2] = Service Name
		serviceLocator.setService(args[0], args[1], args[2]);

		ActivityController activityController = new ActivityController(serviceLocator);
		AthleteController athleteController = new AthleteController(serviceLocator);
		ChallengeController challengeController = new ChallengeController(serviceLocator);
	}
}