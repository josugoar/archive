package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static javax.swing.JOptionPane.showMessageDialog;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

public class ChallengeDialog extends JDialog {

    private ChallengeController challengeController;
    private AthleteController athleteController;

    private JPanel mainPane;
    private JLabel nameLabel, startDateLabel, endDateLabel, distanceLabel, timeLabel, sportLabel;
    private JTextField nameField, startDateField, endDateField, distanceField, timeField;
    private JComboBox<String> sportCombo;
    private JButton acceptButton, cancelButton;
    private String sport[]={"Running","Cycling","Both"};

    SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);

    public ChallengeDialog(ChallengeController challengeController, AthleteController athleteController) {
        this.challengeController = challengeController;
        this.athleteController = athleteController;

        setTitle("Create new Challenge");
        initDialog();
        setContentPane(mainPane);
        setSize(400,400);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setModal(true);
    }

    public void initDialog() {

        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

        // Name
        JPanel nameLine = new JPanel();
        nameLabel = new JLabel("Name");
        nameField = new JTextField(20);
        nameLine.add(nameLabel, BorderLayout.WEST);
        nameLine.add(nameField, BorderLayout.CENTER);

        // Start date
        JPanel startDateLine = new JPanel();
        startDateLabel = new JLabel("Start date (DD/MM/YYYY)");
        startDateField = new JTextField(20);
        startDateLine.add(startDateLabel, BorderLayout.WEST);
        startDateLine.add(startDateField, BorderLayout.CENTER);

        // End date
        JPanel endDateLine = new JPanel();
        endDateLabel = new JLabel("End date (DD/MM/YYYY)");
        endDateField = new JTextField(20);
        endDateLine.add(endDateLabel, BorderLayout.WEST);
        endDateLine.add(endDateField, BorderLayout.CENTER);

        // Distance
        JPanel distanceLine = new JPanel();
        distanceLabel = new JLabel("Distance");
        distanceField = new JTextField(20);
        distanceLine.add(distanceLabel, BorderLayout.WEST);
        distanceLine.add(distanceField, BorderLayout.CENTER);

        // Time
        JPanel timeLine = new JPanel();
        timeLabel = new JLabel("Time");
        timeField = new JTextField(20);
        timeLine.add(timeLabel, BorderLayout.WEST);
        timeLine.add(timeField, BorderLayout.CENTER);

        // Sport
        JPanel sportLine = new JPanel();
        sportLabel = new JLabel("Sport");
        sportCombo = new JComboBox<String>(sport);
        sportLine.add(sportLabel, BorderLayout.WEST);
        sportLine.add(sportCombo, BorderLayout.CENTER);


        mainPane.add(nameLine);
        mainPane.add(startDateLine);
        mainPane.add(endDateLine);
        mainPane.add(distanceLine);
        mainPane.add(timeLine);
        mainPane.add(sportLine);

        JPanel buttonLine = new JPanel();
        acceptButton = new JButton("Accept");
        cancelButton = new JButton("Cancel");
        buttonLine.add(acceptButton);
        buttonLine.add(cancelButton);
        mainPane.add(buttonLine);

        cancelButton.addActionListener(e -> {
            this.setVisible(false);
        });

        acceptButton.addActionListener(e -> {
            this.createChallenge();
        });

         this.add(mainPane);
    }

    public void createChallenge() {
        String name = nameField.getText();

        Duration time = null;
        try {
            time = Duration.parse("P" + timeField.getText() + "D");
        } catch (Exception e) {
        }

        Float distance = null;
        try {
            if (distanceField.getText() != null) {
                distance = Float.parseFloat(distanceField.getText());
            }
        } catch (NumberFormatException e) {
        }

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = formatter.parse(startDateField.getText());
            endDate = formatter.parse(endDateField.getText());
        } catch (RuntimeException | ParseException e1) {
            showMessageDialog(null, "Wrong date, use dd-mm-yyyy");
        }

        String sport = sportCombo.getSelectedItem().toString();
        Set<String> type = new HashSet<>();
        if (sport.equals("Both")) {
            type.add("Cycling");
            type.add("Running");
        } else {
            type.add(sport);
        }

        Long token = athleteController.getToken();

        ChallengeDTO challenge = challengeController.createChallenge(token, name, startDate, endDate, distance, time, type);
        if(challenge == null){
            showMessageDialog(null, "Error creating challenge");
        }

        setVisible(false);
    }


}
