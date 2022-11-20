package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;

public class AthleteDialog extends JDialog {

    private AthleteController athleteController;
    private AthleteDTO athlete;
    private JPanel mainPane;
    private JLabel nameLabel, emailLabel, weightLabel, heightLabel, restingHeartrateLabel, maxHeartrateLabel, dateofBirthLabel;

    public AthleteDialog(AthleteController athleteController) {
        this.athleteController = athleteController;

        setTitle("Athlete Profile");
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                initDialog();
                setContentPane(mainPane);
                revalidate();
                repaint();
            }
        });

    }

    public void initDialog(){
        getAthlete();
        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

        // Name
        JPanel nameLine = new JPanel();
        nameLabel = new JLabel("Name: " + athlete.getName());
        nameLine.add(nameLabel, BorderLayout.CENTER);

        // Email
        JPanel emailLine = new JPanel();
        emailLabel = new JLabel("Email: " + athlete.getEmail());
        emailLine.add(emailLabel, BorderLayout.CENTER);

         // Weight
         JPanel weightLine = new JPanel();
         weightLabel = new JLabel("Weight: " + athlete.getWeight());
         weightLine.add(weightLabel, BorderLayout.CENTER);

         // Height
         JPanel heightLine = new JPanel();
         heightLabel = new JLabel("Height: " + athlete.getHeight());
         heightLine.add(heightLabel, BorderLayout.CENTER);

         // Resting Heartrate
         JPanel restingHeartrateLine = new JPanel();
         restingHeartrateLabel = new JLabel("Resting Heartrate: " + athlete.getRestingHeartrate());
         restingHeartrateLine.add(restingHeartrateLabel, BorderLayout.CENTER);

         // Max Heartrate
         JPanel maxHeartrateLine = new JPanel();
         maxHeartrateLabel = new JLabel("Max Heartrate: " + athlete.getMaxHeartrate());
         maxHeartrateLine.add(maxHeartrateLabel, BorderLayout.CENTER);

         // Date of Birth
         JPanel dateofBirthLine = new JPanel();
         dateofBirthLabel = new JLabel("Date of Birth (DD/MM/YYYY): " + athlete.getDateofbirth());
         dateofBirthLine.add(dateofBirthLabel, BorderLayout.CENTER);

         // Add to the main panel
         mainPane.add(nameLine);
         mainPane.add(emailLine);
         mainPane.add(weightLine);
         mainPane.add(heightLine);
         mainPane.add(restingHeartrateLine);
         mainPane.add(maxHeartrateLine);
         mainPane.add(dateofBirthLine);

         this.add(mainPane);

    }

    public void getAthlete() {
        athlete = athleteController.getAthlete();
    }

}
