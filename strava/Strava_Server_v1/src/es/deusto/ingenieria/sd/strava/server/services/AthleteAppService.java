package es.deusto.ingenieria.sd.strava.server.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.LoginType;

public class AthleteAppService {

    private Map<String, String> emailPasswordState = new HashMap<>();
    private Map<String, Athlete> passwordAthleteState = new HashMap<>();

    public Athlete register(String email, String password, String name, Date birthDate, Float weight, Integer height,
            Integer restingHeartrate, Integer maxHeartrate) {
        Athlete athlete = new Athlete();
        athlete.setDateofbirth(birthDate);
        athlete.setEmail(email);
        athlete.setHeight(height);
        athlete.setMaxHeartrate(maxHeartrate);
        athlete.setName(name);
        athlete.setPassword(password);
        athlete.setRestingHeartrate(restingHeartrate);
        athlete.setWeight(weight);
        athlete.setLoginType(LoginType.LOCAL);

        if (!athlete.checkAthlete()) {
            return null;
        }

        if (emailPasswordState.containsKey(email)) {
            return null;
        }

        emailPasswordState.put(email, password);
        passwordAthleteState.put(password, athlete);

        return athlete;
    }

    public Athlete login(String email, String password) {
        String actualPassword = emailPasswordState.get(email);
        if (actualPassword == null) {
            return null;
        }
        if (!actualPassword.equals(password)) {
            return null;
        }
        return passwordAthleteState.get(password);
    }

}
