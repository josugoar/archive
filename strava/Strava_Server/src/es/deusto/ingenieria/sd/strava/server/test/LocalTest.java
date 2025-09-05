package es.deusto.ingenieria.sd.strava.server.test;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;
import es.deusto.ingenieria.sd.strava.server.remote.IRemoteFacade;
import es.deusto.ingenieria.sd.strava.server.remote.RemoteFacade;

public class LocalTest {

    public static void main(final String[] args) throws RemoteException {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        final IRemoteFacade remoteFacade = new RemoteFacade();

        final String pasword = org.apache.commons.codec.digest.DigestUtils.sha1Hex("password");

        try {
            remoteFacade.login("john.doe@gmail.com", pasword);
        } catch (final Exception e) {
            System.err.println("\t# Error: " + e.getMessage());
        }

        try {
            final AthleteDTO athleteDTO = new AthleteDTO();
            athleteDTO.setName("John Doe");
            athleteDTO.setEmail("john.doe@gmail.com");
            athleteDTO.setDateOfBirth(formatter.parse("25-02-1953"));

            remoteFacade.register(pasword, athleteDTO);

            final long token = remoteFacade.login(athleteDTO.getEmail(), pasword);

            final Set<String> type = new HashSet<>();
            type.add("RUNNING");

            final ChallengeDTO challengeDTO = new ChallengeDTO();
            challengeDTO.setName("BREAKFASTER by New Balance");
            challengeDTO.setStartDate(formatter.parse("24-10-2022"));
            challengeDTO.setEndDate(formatter.parse("04-12-2022"));
            challengeDTO.setDistance(252.);
            challengeDTO.setType(type);

            remoteFacade.createChallenge(token, challengeDTO);

            final List<ChallengeDTO> challenges = remoteFacade.getChallenges(token);
            for (final ChallengeDTO challenge : challenges) {
                System.err.println(challenge);
            }

            final double challengeState = remoteFacade.getChallengeProgress(token, challengeDTO);
            System.err.println(challengeState);

            remoteFacade.logout(token);
        } catch (final Exception e) {
            e.printStackTrace();
            System.err.println("\t# Error: " + e.getMessage());
        }

        System.exit(0);
    }

}