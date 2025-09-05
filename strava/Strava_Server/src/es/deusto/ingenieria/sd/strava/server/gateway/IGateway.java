package es.deusto.ingenieria.sd.strava.server.gateway;

public interface IGateway {

    public boolean checkEmail(String email);

    public boolean checkEmailAndPassword(String email, String password);

}
