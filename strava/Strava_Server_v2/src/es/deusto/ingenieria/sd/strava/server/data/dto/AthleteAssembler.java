package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;

public class AthleteAssembler {

    private static AthleteAssembler instance;

    private AthleteAssembler() {
    }

    public static AthleteAssembler getInstance() {
        if (instance == null) {
            instance = new AthleteAssembler();
        }

        return instance;
    }

    public AthleteDTO athleteToDTO(final Athlete athlete) {
        final AthleteDTO athleteDTO = new AthleteDTO();
        athleteDTO.setName(athlete.getName());
        athleteDTO.setEmail(athlete.getEmail());
        athleteDTO.setWeight(athlete.getWeight());
        athleteDTO.setHeight(athlete.getHeight());
        athleteDTO.setRestingHeartRate(athlete.getRestingHeartRate());
        athleteDTO.setMaxHeartRate(athlete.getMaxHeartRate());
        athleteDTO.setDateOfBirth(athlete.getDateOfBirth());
        return athleteDTO;
    }

    public List<AthleteDTO> athleteToDTO(final List<Athlete> athletes) {
        return athletes.stream().map(this::athleteToDTO).toList();
    }

    // TODO: setPassword()
    public Athlete DTOToAthlete(final AthleteDTO athleteDTO) throws IllegalArgumentException {
        final Athlete athlete = new Athlete();
        athlete.setName(athleteDTO.getName());
        athlete.setEmail(athleteDTO.getEmail());
        athlete.setWeight(athleteDTO.getWeight());
        athlete.setHeight(athleteDTO.getHeight());
        athlete.setRestingHeartRate(athleteDTO.getRestingHeartRate());
        athlete.setMaxHeartRate(athleteDTO.getMaxHeartRate());
        athlete.setDateOfBirth(athleteDTO.getDateOfBirth());
        return athlete;
    }

    public List<Athlete> DTOToAthlete(final List<AthleteDTO> athleteDTOs) throws IllegalArgumentException {
        return athleteDTOs.stream().map(this::DTOToAthlete).toList();
    }

}