package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.util.List;

import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;
import es.deusto.ingenieria.sd.strava.server.data.domain.SportType;

public class ChallengeAssembler {

    private static ChallengeAssembler instance;

    private ChallengeAssembler() {
    }

    public static ChallengeAssembler getInstance() {
        if (instance == null) {
            instance = new ChallengeAssembler();
        }

        return instance;
    }

    public ChallengeDTO challengeToDTO(final Challenge challenge) {
        final ChallengeDTO challengeDTO = new ChallengeDTO();
        challengeDTO.setName(challenge.getName());
        challengeDTO.setStartDate(challenge.getStartDate());
        challengeDTO.setEndDate(challenge.getEndDate());
        challengeDTO.setDistance(challenge.getDistance());
        challengeDTO.setTime(challenge.getTime());
        for (final SportType sportType : challenge.getType()) {
            challengeDTO.addType(sportType.name());
        }
        return challengeDTO;
    }

    public List<ChallengeDTO> challengeToDTO(final List<Challenge> challenges) {
        return challenges.stream().map(this::challengeToDTO).toList();
    }

    public Challenge DTOToChallenge(final ChallengeDTO challengeDTO) throws IllegalArgumentException {
        final Challenge challenge = new Challenge();
        challenge.setName(challengeDTO.getName());
        challenge.setStartDate(challengeDTO.getStartDate());
        challenge.setEndDate(challengeDTO.getEndDate());
        challenge.setDistance(challengeDTO.getDistance());
        challenge.setTime(challengeDTO.getTime());
        for (final String sportType : challengeDTO.getType()) {
            challenge.addType(SportType.valueOf(sportType));
        }
        return challenge;
    }

    public List<Challenge> DTOToChallenge(final List<ChallengeDTO> challengeDTOs) throws IllegalArgumentException {
        return challengeDTOs.stream().map(this::DTOToChallenge).toList();
    }

}