package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.*;
import java.awt.*;

import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;

public class ChallengeDialog extends JDialog{

    private ChallengeController challengeController;

    private JPanel mainPane;
    private JLabel nameLabel, startDateLabel, endDateLabel, distanceLabel, timeLabel, sportLabel;
    private JTextField nameField, startDateField, endDateField, distanceField, timeField;
    private JComboBox<String> sportCombo;
    private JButton acceptButton, cancelButton;
    private String sport[]={"Running","Cycling"};

    public ChallengeDialog(ChallengeController challengeController) {
        this.challengeController = challengeController;

        initDialog();
        setContentPane(mainPane);
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
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
         distanceLabel = new JLabel("Distance (optional)");
         distanceField = new JTextField(20);
         distanceLine.add(distanceLabel, BorderLayout.WEST);
         distanceLine.add(distanceField, BorderLayout.CENTER);

         // Time
         JPanel timeLine = new JPanel();
         timeLabel = new JLabel("Time (optional)");
         timeField = new JTextField(20);
         timeLine.add(timeLabel, BorderLayout.WEST);
         timeLine.add(timeField, BorderLayout.CENTER);

         // Sport
         JPanel sportLine = new JPanel();
         sportLabel = new JLabel("Sport");
         sportCombo = new JComboBox<>(sport);
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


         this.add(mainPane);


    }

    public void createChallenge() {
    }

}
