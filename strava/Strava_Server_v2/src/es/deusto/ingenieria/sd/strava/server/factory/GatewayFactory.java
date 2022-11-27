package es.deusto.ingenieria.sd.strava.server.factory;

import es.deusto.ingenieria.sd.strava.server.gateway.FacebookServiceGateway;
import es.deusto.ingenieria.sd.strava.server.gateway.GoogleServiceGateway;
import es.deusto.ingenieria.sd.strava.server.gateway.IGateway;

public class GatewayFactory {

    public static IGateway createGateway(final String gateway) throws IllegalArgumentException {
        switch (gateway) {
            case "Facebook":
                return FacebookServiceGateway.getInstance();

            case "Google":
                return GoogleServiceGateway.getInstance();

            default:
                throw new IllegalArgumentException("Invalid gateway!");
        }
    }

}
