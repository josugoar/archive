package es.deusto.ingenieria.sd.strava.client.controller;

import java.rmi.RemoteException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;

public class ActivityController {

    ServiceLocator serviceLocator;

    public ActivityController(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ActivityDTO createActivity(long token, String name, float distance, Duration elapsedTime, String type,
            Date startDate) {
        try {
            return this.serviceLocator.getService().createActivity(token, name, distance, elapsedTime, type, startDate);
        } catch (RemoteException e) {
            System.out.println("# Error creating an activity: " + e);
            return null;
        }
    }

    public List<ActivityDTO> getActivities(long token) {
        try {
            return this.serviceLocator.getService().getActivities(token);
        } catch (RemoteException e) {
            System.out.println("# Error getting activities: " + e);
            return new ArrayList<>();
        }
    }

}
