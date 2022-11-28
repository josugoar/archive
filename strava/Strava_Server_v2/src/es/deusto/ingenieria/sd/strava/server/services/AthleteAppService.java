package es.deusto.ingenieria.sd.strava.server.services;

import java.util.HashMap;
import java.util.Map;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.LoginType;
import es.deusto.ingenieria.sd.strava.server.factory.GatewayFactory;

public class AthleteAppService {

    private static AthleteAppService instance;

    private final Map<String, Athlete> athletes = new HashMap<>();
    private final Map<String, String> passwords = new HashMap<>();

    private AthleteAppService() {
    }

    public static AthleteAppService getInstance() {
        if (instance == null) {
            instance = new AthleteAppService();
        }

        return instance;
    }

    public void register(final String password, final Athlete athlete) throws IllegalArgumentException {
        if (athletes.containsKey(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        athlete.setLoginType(LoginType.LOCAL);
        athletes.put(athlete.getEmail(), athlete);

        passwords.put(athlete.getEmail(), password);
    }

    public void registerGoogle(final Athlete athlete) throws IllegalArgumentException {
        if (athletes.containsKey(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        if (!GatewayFactory.createGateway("Google").checkEmail(athlete.getEmail())) {
            throw new IllegalArgumentException("Invalid email!");
        }

        athlete.setLoginType(LoginType.GOOGLE);
        athletes.put(athlete.getEmail(), athlete);
    }

    public void registerFacebook(final Athlete athlete) throws IllegalArgumentException {
        if (athletes.containsKey(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        if (!GatewayFactory.createGateway("Facebook").checkEmail(athlete.getEmail())) {
            throw new IllegalArgumentException("Invalid email!");
        }

        athlete.setLoginType(LoginType.FACEBOOK);
        athletes.put(athlete.getEmail(), athlete);
    }

    public Athlete login(final String email, final String password) throws IllegalArgumentException {
        if (!athletes.containsKey(email)) {
            throw new IllegalArgumentException("Athlete is not registered!");
        }

        final Athlete athlete = athletes.get(email);

        switch (athlete.getLoginType()) {
            case LOCAL:
                if (!passwords.get(email).equals(password)) {
                    throw new IllegalArgumentException("Invalid password!");
                }

                break;

            case GOOGLE:
                if (!GatewayFactory.createGateway("Google").checkEmailAndPassword(email, password)) {
                    throw new IllegalArgumentException("Invalid password!");
                }

                break;

            case FACEBOOK:
                if (!GatewayFactory.createGateway("Facebook").checkEmailAndPassword(email, password)) {
                    throw new IllegalArgumentException("Invalid password!");
                }

                break;

            default:
                throw new IllegalArgumentException("Invalid password!");
        }

        return athlete;
    }

}
