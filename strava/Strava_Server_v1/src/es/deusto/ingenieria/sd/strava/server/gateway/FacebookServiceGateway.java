package es.deusto.ingenieria.sd.strava.server.gateway;

public class FacebookServiceGateway {

    private static final FacebookServiceGateway INSTANCE = new FacebookServiceGateway();

    private FacebookServiceGateway() {}

    public static FacebookServiceGateway getInstance() {
        return INSTANCE;
    }
    
    
}
