package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.SportType;

public class ActivityAssembler {

    private static ActivityAssembler instance;

    private ActivityAssembler() {
    }

    public static ActivityAssembler getInstance() {
        if (instance == null) {
            instance = new ActivityAssembler();
        }

        return instance;
    }

    public ActivityDTO activityToDTO(final Activity activity) {
        final ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName(activity.getName());
        activityDTO.setDistance(activity.getDistance());
        activityDTO.setElapsedTime(activity.getElapsedTime());
        activityDTO.setType(activity.getType().name());
        activityDTO.setStartDate(activity.getStartDate());
        return activityDTO;
    }

    public List<ActivityDTO> activityToDTO(final List<Activity> activities) {
        return activities.stream().map(this::activityToDTO).toList();
    }

    public Activity DTOToActivity(final ActivityDTO activityDTO) throws IllegalArgumentException {
        final Activity activity = new Activity();
        activity.setName(activityDTO.getName());
        activity.setDistance(activityDTO.getDistance());
        activity.setElapsedTime(activityDTO.getElapsedTime());
        activity.setType(SportType.valueOf(activityDTO.getType()));
        activity.setStartDate(activityDTO.getStartDate());
        return activity;
    }

    public List<Activity> DTOToActivity(final List<ActivityDTO> activityDTOs) throws IllegalArgumentException {
        return activityDTOs.stream().map(this::DTOToActivity).toList();
    }

}