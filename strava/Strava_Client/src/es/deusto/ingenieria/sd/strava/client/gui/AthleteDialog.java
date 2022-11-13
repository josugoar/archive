package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.*;
import java.awt.*;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;

public class AthleteDialog extends JDialog {

    private AthleteController athleteController;

    private JPanel mainPane;
    private JLabel emailLabel, passwordLabel, weightLabel, heightLabel, restingHeartrateLabel, maxHeartrateLabel, dateofBirthLabel;
    private JTextField emailField, passwordField, weightField, heightField, restingHeartrateField, maxHeartrateField, dateofBirthField;
    private JButton acceptButton, cancelButton;
 

    public AthleteDialog(AthleteController athleteController) {
        this.athleteController = athleteController;

        initDialog();
        setContentPane(mainPane);
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void initDialog(){
        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

        // Email
        JPanel emailLine = new JPanel();
        emailLabel = new JLabel("Email");
        emailField = new JTextField(20);
        emailLine.add(emailLabel, BorderLayout.WEST);
        emailLine.add(emailField, BorderLayout.CENTER);

         // Password
         JPanel passwordLine = new JPanel();
         passwordLabel = new JLabel("Password");
         passwordField = new JTextField(20);
         passwordLine.add(passwordLabel, BorderLayout.WEST);
         passwordLine.add(passwordField, BorderLayout.CENTER);

         // Weight
         JPanel weightLine = new JPanel();
         weightLabel = new JLabel("Weight");
         weightField = new JTextField(20);
         weightLine.add(weightLabel, BorderLayout.WEST);
         weightLine.add(weightField, BorderLayout.CENTER);

         // Height
         JPanel heightLine = new JPanel();
         heightLabel = new JLabel("Height)");
         heightField = new JTextField(20);
         heightLine.add(heightLabel, BorderLayout.WEST);
         heightLine.add(heightField, BorderLayout.CENTER);

         // Resting Heartrate
         JPanel restingHeartrateLine = new JPanel();
         restingHeartrateLabel = new JLabel("Resting Heartrate");
         restingHeartrateField = new JTextField(20);
         restingHeartrateLine.add(restingHeartrateLabel, BorderLayout.WEST);
         restingHeartrateLine.add(restingHeartrateField, BorderLayout.CENTER);

         // Max Heartrate
         JPanel maxHeartrateLine = new JPanel();
         maxHeartrateLabel = new JLabel("Max Heartrate");
         maxHeartrateField = new JTextField(20);
         maxHeartrateLine.add(maxHeartrateLabel, BorderLayout.WEST);
         maxHeartrateLine.add(maxHeartrateField, BorderLayout.CENTER);

         // Date of Birth
         JPanel dateofBirthLine = new JPanel();
         dateofBirthLabel = new JLabel("Date of Birth (DD/MM/YYYY)");
         dateofBirthField = new JTextField(20);
         dateofBirthLine.add(dateofBirthLabel, BorderLayout.WEST);
         dateofBirthLine.add(dateofBirthField, BorderLayout.CENTER);

         // Add to the main panel
         mainPane.add(emailLine);
         mainPane.add(passwordLine);
         mainPane.add(weightLine);
         mainPane.add(heightLine);
         mainPane.add(restingHeartrateLine);
         mainPane.add(maxHeartrateLine);
         mainPane.add(dateofBirthLine);
         

         JPanel buttonLine = new JPanel();
         acceptButton = new JButton("Accept");
         cancelButton = new JButton("Cancel");
         buttonLine.add(acceptButton);
         buttonLine.add(cancelButton);
         mainPane.add(buttonLine);


         this.add(mainPane);

    }

    public void getAthlete() {
    }

}
