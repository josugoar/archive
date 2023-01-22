package es.deusto.ingenieria.sd.strava.client.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;

public class ActivityController {

    ServiceLocator serviceLocator;

    public ActivityController(final ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public boolean createActivity(final long token, final ActivityDTO activityDTO) {
        try {
            serviceLocator.getService().createActivity(token, activityDTO);
            
            return true;
        } catch (final RemoteException e) {
            System.err.println("# Error creating an activity: " + e);

            return false;
        }
    }

    public List<ActivityDTO> getActivities(final long token) {
        try {
            return serviceLocator.getService().getActivities(token);
        } catch (final RemoteException e) {
            System.err.println("# Error getting activities: " + e);

            return new ArrayList<>();
        }
    }

}
