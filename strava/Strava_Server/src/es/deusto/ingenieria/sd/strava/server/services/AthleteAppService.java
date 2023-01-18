package es.deusto.ingenieria.sd.strava.server.services;

import es.deusto.ingenieria.sd.strava.server.dao.DAO;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.LoginType;
import es.deusto.ingenieria.sd.strava.server.factory.GatewayFactory;
import es.deusto.ingenieria.sd.strava.server.gateway.IGateway;

public class AthleteAppService {

    private static AthleteAppService instance;

    private AthleteAppService() {
    }

    public static AthleteAppService getInstance() {
        if (instance == null) {
            instance = new AthleteAppService();
        }

        return instance;
    }

    public void register(final String password, final Athlete athlete) throws IllegalArgumentException {
        if (DAO.getInstance().containsAthlete(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        athlete.setLoginType(LoginType.LOCAL);
        athlete.setPassword(password);
        DAO.getInstance().storeAthlete(athlete);
    }

    public void registerGoogle(final Athlete athlete) throws IllegalArgumentException {
        if (DAO.getInstance().containsAthlete(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        if (!GatewayFactory.createGateway(LoginType.GOOGLE).checkEmail(athlete.getEmail())) {
            throw new IllegalArgumentException("Invalid email!");
        }

        athlete.setLoginType(LoginType.GOOGLE);
        DAO.getInstance().storeAthlete(athlete);
    }

    public void registerFacebook(final Athlete athlete) throws IllegalArgumentException {
        if (DAO.getInstance().containsAthlete(athlete.getEmail())) {
            throw new IllegalArgumentException("Athlete is already registered!");
        }

        if (!GatewayFactory.createGateway(LoginType.FACEBOOK).checkEmail(athlete.getEmail())) {
            throw new IllegalArgumentException("Invalid email!");
        }

        athlete.setLoginType(LoginType.FACEBOOK);
        DAO.getInstance().storeAthlete(athlete);
    }

    public Athlete login(final String email, final String password) throws IllegalArgumentException {
        if (!DAO.getInstance().containsAthlete(email)) {
            throw new IllegalArgumentException("Athlete is not registered!");
        }

        final Athlete athlete = DAO.getInstance().getAthlete(email);

        final IGateway gateway = GatewayFactory.createGateway(athlete.getLoginType());

        if (gateway == null) {
            if (!athlete.getPassword().equals(password)) {
                throw new IllegalArgumentException("Invalid password!");
            }
        } else {
            if (!gateway.checkEmailAndPassword(email, password)) {
                throw new IllegalArgumentException("Invalid password!");
            }
        }

        return athlete;
    }

}
