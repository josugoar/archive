package es.deusto.ingenieria.sd.strava.server.services;

import java.util.Date;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;

public class AthleteAppService {

    public long register(String email, String password, String name, Date birthDate, Float weight, Integer height, Integer restingHeartrate, Integer maxHeartrate) {
      login(email, password);
		  return 0;
    }

    public Athlete login(String email, String password) {
		return null;
    }

}
