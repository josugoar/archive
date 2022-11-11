package es.deusto.ingenieria.sd.strava.server.test;

import es.deusto.ingenieria.sd.strava.server.remote.RemoteFacade;

// TODO
public class LocalTest {

	public static void main(String[] args) {
		RemoteFacade facade = null;
		long token = 0l;

		try {
		} catch (Exception e) {
			System.out.println("\t# Error: " + e.getMessage());
		}

		//Force exit to stop RMI Server
		System.exit(0);
	}
}