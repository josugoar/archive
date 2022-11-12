package es.deusto.ingenieria.sd.strava.client.controller;

import es.deusto.ingenieria.sd.auctions.client.remote.ServiceLocator;

public class ActivityController {

    ServiceLocator serviceLocator;

    public ActivityController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

}
