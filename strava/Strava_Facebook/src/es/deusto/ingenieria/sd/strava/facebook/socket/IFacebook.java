package es.deusto.ingenieria.sd.strava.facebook.socket;

public interface IFacebook {

    public boolean checkEmail(String email);

    public boolean checkEmailAndPassword(String email, String password);

}
