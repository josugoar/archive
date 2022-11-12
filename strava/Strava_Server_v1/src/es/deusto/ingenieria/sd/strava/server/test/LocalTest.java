package es.deusto.ingenieria.sd.strava.server.test;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;
import es.deusto.ingenieria.sd.strava.server.remote.IRemoteFacade;
import es.deusto.ingenieria.sd.strava.server.remote.RemoteFacade;

public class LocalTest {

	public static void main(String[] args) throws RemoteException {
		IRemoteFacade facade = new RemoteFacade();
		long token = 0l;
		String pasword = org.apache.commons.codec.digest.DigestUtils.sha1Hex("$!9PhNz,");

		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH);

		try {
			// Login (fails no registered athlete)
			facade.login("Peter Oben", pasword);
		} catch (Exception e) {
			System.out.println("\t# Error: " + e.getMessage());
		}

		try {
			token = facade.register("peter.oben@gmail.com", "Peter Oben", pasword, new Date(), null, null, null, null);
			ChallengeDTO newChallenge = facade.createChallenge(token, "hike", formatter.parse("22-01-2022"), formatter.parse("23-01-2029"), 10,
					Duration.ofHours(5), true, false);
			List<ChallengeDTO> challenges = facade.getActiveChallenges(token);
			for (ChallengeDTO challenge : challenges) {
				System.out.println(challenge);
			}
			facade.logout(token);
			float challengeState = facade.getChallengeState(newChallenge.getId());
			System.out.println(challengeState);
		} catch (Exception e) {
			System.out.println("\t# Error: " + e.getMessage());
		}

		// Force exit to stop RMI Server
		System.exit(0);
	}
}