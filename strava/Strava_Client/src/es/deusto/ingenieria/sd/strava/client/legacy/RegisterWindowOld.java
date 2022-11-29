package es.deusto.ingenieria.sd.strava.client.legacy;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.*;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.gui.LoginWindow;
import es.deusto.ingenieria.sd.strava.client.gui.MainWindowActivities;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;

public class RegisterWindow extends JFrame {

    private final AthleteController athleteController;
    private LoginWindow loginWindow;
    private MainWindowActivities mainWindow;

    private JPanel mainPane;
    private JButton loginButton, registerButton, googleRegisterButton, facebookRegisterButton;
    private JTextField emailField, nameField, heightField, weightField, dateOfBirthField, maxHeartRateField,
            restingHeartRateField;
    private JLabel emailLabel, passLabel, nameLabel, heightLabel, weightLabel, dateOfBirthLabel, maxHeartRateLabel,
            restingHeartRateLabel;
    private JPasswordField passField;
    private JLabel warning;

    SimpleDateFormat formatter = new SimpleDateFormat("DD-mm-yyyy", Locale.ENGLISH);

    public RegisterWindow(final AthleteController athleteController) {
        this.athleteController = athleteController;

        initPane();
        setTitle("Create a new Account");
        setContentPane(mainPane);
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void initPane() {
        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

        // Email prompt
        final JPanel emailLine = new JPanel();
        emailLabel = new JLabel("Email*");
        emailField = new JTextField(20);
        emailLine.add(emailLabel, BorderLayout.WEST);
        emailLine.add(emailField, BorderLayout.CENTER);

        // password prompt
        final JPanel passLine = new JPanel();
        passLabel = new JLabel("Password*");
        passField = new JPasswordField(20);
        passLine.add(passLabel, BorderLayout.WEST);
        passLine.add(passField, BorderLayout.CENTER);

        final JPanel nameLine = new JPanel();
        nameLabel = new JLabel("Name*");
        nameField = new JTextField(20);
        nameLine.add(nameLabel, BorderLayout.WEST);
        nameLine.add(nameField, BorderLayout.CENTER);

        final JPanel heightLine = new JPanel();
        heightLabel = new JLabel("Height (cm)");
        heightField = new JTextField(20);
        heightLine.add(heightLabel, BorderLayout.WEST);
        heightLine.add(heightField, BorderLayout.CENTER);

        final JPanel weightLine = new JPanel();
        weightLabel = new JLabel("Weight in kg");
        weightField = new JTextField(20);
        weightLine.add(weightLabel, BorderLayout.WEST);
        weightLine.add(weightField, BorderLayout.CENTER);

        final JPanel dateOfBirthLine = new JPanel();
        dateOfBirthLabel = new JLabel("Date of Birth* (dd-MM-yyyy)");
        dateOfBirthField = new JTextField(20);
        dateOfBirthLine.add(dateOfBirthLabel, BorderLayout.WEST);
        dateOfBirthLine.add(dateOfBirthField, BorderLayout.CENTER);

        final JPanel maxHeartRateLine = new JPanel();
        maxHeartRateLabel = new JLabel("Max Heart Rate in bpm");
        maxHeartRateField = new JTextField(20);
        maxHeartRateLine.add(maxHeartRateLabel, BorderLayout.WEST);
        maxHeartRateLine.add(maxHeartRateField, BorderLayout.CENTER);

        final JPanel restingHeartRateLine = new JPanel();
        restingHeartRateLabel = new JLabel("Resting Heart Rate in bpm");
        restingHeartRateField = new JTextField(20);
        restingHeartRateLine.add(restingHeartRateLabel, BorderLayout.WEST);
        restingHeartRateLine.add(restingHeartRateField, BorderLayout.CENTER);

        mainPane.add(emailLine);
        mainPane.add(passLine);
        mainPane.add(nameLine);
        mainPane.add(heightLine);
        mainPane.add(weightLine);
        mainPane.add(dateOfBirthLine);
        mainPane.add(maxHeartRateLine);
        mainPane.add(restingHeartRateLine);
        warning = new JLabel("All fields with * are NOT optional");
        mainPane.add(warning);

        final JPanel buttonLine = new JPanel();
        loginButton = new JButton("Back to Login");
        registerButton = new JButton("Create a new Account");
        googleRegisterButton = new JButton("Register with Google Account");
        facebookRegisterButton = new JButton("Register with Facebook Account");
        loginButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                loginWindow.setVisible(true);
                setVisible(false);
            }

        });
        registerButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                register();
            }

        });

        googleRegisterButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                registerGoogle();
            }

        });

        facebookRegisterButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                registerFacebook();
            }

        });

        buttonLine.add(loginButton);
        buttonLine.add(registerButton);
        buttonLine.add(googleRegisterButton);
        buttonLine.add(facebookRegisterButton);
        mainPane.add(buttonLine);

        this.add(mainPane);
    }

    public void setLoginWindow(final LoginWindow l) {
        this.loginWindow = l;
    }

    public void setMainWindow(final MainWindowActivities mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void register() {
        final String email = emailField.getText();
        final String password = String.valueOf(passField.getPassword());
        final String name = nameField.getText();

        Date dateOfBirth = null;
        try {
            dateOfBirth = formatter.parse(dateOfBirthField.getText());
        } catch (final ParseException e1) {
        }

        Double weight = null;
        try {
            weight = Double.parseDouble(weightField.getText());
        } catch (final Exception e) {
        }

        Integer height = null;
        try {
            height = Integer.parseInt(heightField.getText());
        } catch (final Exception e) {
        }

        Integer maxHeartRate = null;
        try {
            maxHeartRate = Integer.parseInt(maxHeartRateField.getText());
        } catch (final Exception e) {
        }

        Integer restingHeartRate = null;
        try {
            restingHeartRate = Integer.parseInt(restingHeartRateField.getText());
        } catch (final Exception e) {
        }

        final AthleteDTO athleteDTO = new AthleteDTO();
        athleteDTO.setName(name);
        athleteDTO.setEmail(name);
        athleteDTO.setDateOfBirth(dateOfBirth);
        athleteDTO.setWeight(weight);
        athleteDTO.setHeight(height);
        athleteDTO.setRestingHeartRate(restingHeartRate);
        athleteDTO.setMaxHeartRate(maxHeartRate);
        if (athleteController.register(password, athleteDTO) && athleteController.login(email, password)) {
            this.setVisible(false);
            mainWindow.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Error in registration");
        }
    }


    public void registerGoogle() {
        final String email = emailField.getText();
        final String password = String.valueOf(passField.getPassword());
        final String name = nameField.getText();

        Date dateOfBirth = null;
        try {
            dateOfBirth = formatter.parse(dateOfBirthField.getText());
        } catch (final ParseException e1) {
        }

        Double weight = null;
        try {
            weight = Double.parseDouble(weightField.getText());
        } catch (final Exception e) {
        }

        Integer height = null;
        try {
            height = Integer.parseInt(heightField.getText());
        } catch (final Exception e) {
        }

        Integer maxHeartRate = null;
        try {
            maxHeartRate = Integer.parseInt(maxHeartRateField.getText());
        } catch (final Exception e) {
        }

        Integer restingHeartRate = null;
        try {
            restingHeartRate = Integer.parseInt(restingHeartRateField.getText());
        } catch (final Exception e) {
        }

        final AthleteDTO athleteDTO = new AthleteDTO();
        athleteDTO.setName(name);
        athleteDTO.setEmail(name);
        athleteDTO.setDateOfBirth(dateOfBirth);
        athleteDTO.setWeight(weight);
        athleteDTO.setHeight(height);
        athleteDTO.setRestingHeartRate(restingHeartRate);
        athleteDTO.setMaxHeartRate(maxHeartRate);
        if (athleteController.registerGoogle(athleteDTO) && athleteController.login(email, password)) {
            this.setVisible(false);
            mainWindow.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Error in registration");
        }
    }

    public void registerFacebook() {
        final String email = emailField.getText();
        final String password = String.valueOf(passField.getPassword());
        final String name = nameField.getText();

        Date dateOfBirth = null;
        try {
            dateOfBirth = formatter.parse(dateOfBirthField.getText());
        } catch (final ParseException e1) {
        }

        Double weight = null;
        try {
            weight = Double.parseDouble(weightField.getText());
        } catch (final Exception e) {
        }

        Integer height = null;
        try {
            height = Integer.parseInt(heightField.getText());
        } catch (final Exception e) {
        }

        Integer maxHeartRate = null;
        try {
            maxHeartRate = Integer.parseInt(maxHeartRateField.getText());
        } catch (final Exception e) {
        }

        Integer restingHeartRate = null;
        try {
            restingHeartRate = Integer.parseInt(restingHeartRateField.getText());
        } catch (final Exception e) {
        }

        final AthleteDTO athleteDTO = new AthleteDTO();
        athleteDTO.setName(name);
        athleteDTO.setEmail(name);
        athleteDTO.setDateOfBirth(dateOfBirth);
        athleteDTO.setWeight(weight);
        athleteDTO.setHeight(height);
        athleteDTO.setRestingHeartRate(restingHeartRate);
        athleteDTO.setMaxHeartRate(maxHeartRate);
        if (athleteController.registerFacebook(athleteDTO) && athleteController.login(email, password)) {
            this.setVisible(false);
            mainWindow.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Error in registration");
        }
    }

}