package es.deusto.ingenieria.sd.strava.server.factory;

import es.deusto.ingenieria.sd.strava.server.data.domain.LoginType;
import es.deusto.ingenieria.sd.strava.server.gateway.FacebookServiceGateway;
import es.deusto.ingenieria.sd.strava.server.gateway.GoogleServiceGateway;
import es.deusto.ingenieria.sd.strava.server.gateway.IGateway;

public class GatewayFactory {

    public static IGateway createGateway(final LoginType loginType) {
        switch (loginType) {
            case FACEBOOK:
                return FacebookServiceGateway.getInstance();

            case GOOGLE:
                return GoogleServiceGateway.getInstance();

            default:
                throw null;
        }
    }

}
