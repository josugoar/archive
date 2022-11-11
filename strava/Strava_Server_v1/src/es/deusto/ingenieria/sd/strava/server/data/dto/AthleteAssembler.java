package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.util.ArrayList;
import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;

//This class is part of the DTO pattern. It also implements Singleton Pattern.
public class AthleteAssembler {
	private static AthleteAssembler instance;

	private AthleteAssembler() { }

	public static AthleteAssembler getInstance() {
		if (instance == null) {
			instance = new AthleteAssembler();
		}

		return instance;
	}

	public AthleteDTO athleteToDTO(Athlete athlete) {
		AthleteDTO dto = new AthleteDTO();
		dto.setDateofbirth(athlete.getDateofbirth());
		dto.setEmail(athlete.getEmail());
		dto.setHeight(athlete.getHeight());
		dto.setMaxHeartrate(athlete.getMaxHeartrate());
		dto.setName(athlete.getName());
		dto.setRestingHeartrate(athlete.getRestingHeartrate());
		dto.setWeight(athlete.getWeight());

		return dto;
	}

	public List<AthleteDTO> athleteToDTO(List<Athlete> athletes) {
		List<AthleteDTO> dtos = new ArrayList<>();

		for (Athlete athlete : athletes) {
			dtos.add(this.athleteToDTO(athlete));
		}

		return dtos;
	}
}