package es.deusto.ingenieria.sd.strava.client.test;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.gui.LoginWindow;
import es.deusto.ingenieria.sd.strava.client.gui.RegisterWindow;
import es.deusto.ingenieria.sd.strava.client.remote.ServiceLocator;

public class LocalTest {
    

    public static void main(String[] args) {
        ServiceLocator serviceLocator = new ServiceLocator();
        AthleteController athleteController = new AthleteController(serviceLocator);

        RegisterWindow registerWindow = new RegisterWindow(athleteController);
        LoginWindow loginWindow = new LoginWindow(athleteController);

        loginWindow.setRegisterWindow(registerWindow);
        registerWindow.setLoginWindow(loginWindow);

        




    }
}
