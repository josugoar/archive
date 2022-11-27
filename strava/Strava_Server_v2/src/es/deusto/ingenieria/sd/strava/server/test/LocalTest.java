package es.deusto.ingenieria.sd.strava.server.test;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;
import es.deusto.ingenieria.sd.strava.server.remote.IRemoteFacade;
import es.deusto.ingenieria.sd.strava.server.remote.RemoteFacade;

public class LocalTest {

    public static void main(final String[] args) throws RemoteException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);

        final IRemoteFacade remoteFacade = new RemoteFacade();

        final String pasword = org.apache.commons.codec.digest.DigestUtils.sha1Hex("password");

        try {
            remoteFacade.login("john.doe@gmail.com", pasword);
        } catch (final RemoteException e) {
            System.out.println("\t# Error: " + e.getMessage());
        }

        try {
            final Set<String> type = new HashSet<>();
            type.add("CYCLING");

            final AthleteDTO athleteDTO = new AthleteDTO();
            athleteDTO.setName("Peter Oben");
            athleteDTO.setEmail("john.doe@gmail.com");
            athleteDTO.setDateOfBirth(new Date());

            remoteFacade.register(pasword, athleteDTO);

            final long token = remoteFacade.login(athleteDTO.getEmail(), pasword);

            final ChallengeDTO challengeDTO = new ChallengeDTO();
            challengeDTO.setName(pasword);
            challengeDTO.setStartDate(formatter.parse("22-01-2022"));
            challengeDTO.setEndDate(formatter.parse("22-01-2023"));
            challengeDTO.setDistance(10d);
            challengeDTO.setType(type);

            remoteFacade.createChallenge(token, challengeDTO);

            final List<ChallengeDTO> challenges = remoteFacade.getChallenges(token);
            for (final ChallengeDTO challenge : challenges) {
                System.out.println(challenge);
            }

            final double challengeState = remoteFacade.getChallengeProgress(token, challengeDTO);
            System.out.println(challengeState);

            remoteFacade.logout(token);
        } catch (final Exception e) {
            System.out.println("\t# Error: " + e.getMessage());
        }

        System.exit(0);
    }

}