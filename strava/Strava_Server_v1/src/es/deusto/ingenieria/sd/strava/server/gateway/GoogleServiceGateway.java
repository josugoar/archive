package es.deusto.ingenieria.sd.strava.server.gateway;

public class GoogleServiceGateway {
    
    private static final GoogleServiceGateway INSTANCE = new GoogleServiceGateway();

    private GoogleServiceGateway() {}

    public static GoogleServiceGateway getInstance() {
        return INSTANCE;
    }

}
