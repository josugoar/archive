package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.util.ArrayList;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;

//This class is part of the DTO pattern. It also implements Singleton Pattern.
public class ActivityAssembler {
	private static ActivityAssembler instance;

	private ActivityAssembler() { }

	public static ActivityAssembler getInstance() {
		if (instance == null) {
			instance = new ActivityAssembler();
		}

		return instance;
	}

	public ActivityDTO activityToDTO(Activity activity) {
		ActivityDTO dto = new ActivityDTO();
		dto.setDistance(activity.getDistance());
		dto.setElapsedTime(activity.getElapsedTime());
		dto.setName(activity.getName());
		dto.setStartDate(activity.getStartDate());
		dto.setType(activity.getType());

		return dto;
	}

	public List<ActivityDTO> activityToDTO(List<Activity> activities) {
		List<ActivityDTO> dtos = new ArrayList<>();

		for (Activity activity : activities) {
			dtos.add(this.activityToDTO(activity));
		}

		return dtos;
	}
}