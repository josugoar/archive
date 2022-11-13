package es.deusto.ingenieria.sd.strava.client.gui;

import javax.swing.JDialog;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;

import static javax.swing.JOptionPane.showMessageDialog;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;

public class ActivityDialog extends JDialog {

    private ActivityController activityController;
    private AthleteController athleteController;

    private JPanel mainPane;
    private JLabel nameLabel, distanceLabel, elapsedTimeLabel, typeLabel, startDateLabel;
    private JTextField nameField, distanceField, elapsedTimeField, startDateField;
    private JComboBox<String> typeCombo;
    private JButton acceptButton, cancelButton;
    private String type[]={"Running","Cycling"};

    SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);

    public ActivityDialog(ActivityController activityController, AthleteController athleteController) {
        this.activityController = activityController;
        this.athleteController = athleteController;

        initDialog();
        setContentPane(mainPane);
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

         // Distance
         JPanel distanceLine = new JPanel();
         distanceLabel = new JLabel("Distance");
         distanceField = new JTextField(20);
         distanceLine.add(distanceLabel, BorderLayout.WEST);
         distanceLine.add(distanceField, BorderLayout.CENTER);

         // Elapsed time
         JPanel elapsedTimeLine = new JPanel();
         elapsedTimeLabel = new JLabel("Elapsed time");
         elapsedTimeField = new JTextField(20);
         elapsedTimeLine.add(elapsedTimeLabel, BorderLayout.WEST);
         elapsedTimeLine.add(elapsedTimeField, BorderLayout.CENTER);

         // Type
         JPanel typeLine = new JPanel();
         typeLabel = new JLabel("Sport");
         typeCombo = new JComboBox<String>(type);
         typeLine.add(typeLabel, BorderLayout.WEST);
         typeLine.add(typeCombo, BorderLayout.CENTER);


         mainPane.add(nameLine);
         mainPane.add(distanceLine);
         mainPane.add(elapsedTimeLine);
         mainPane.add(typeLine);
         mainPane.add(startDateLine);

         JPanel buttonLine = new JPanel();
         acceptButton = new JButton("Accept");
         cancelButton = new JButton("Cancel");
         buttonLine.add(acceptButton);
         buttonLine.add(cancelButton);
         mainPane.add(buttonLine);
 
         cancelButton.addActionListener(e -> { 
            this.setVisible(false);;
        });

        acceptButton.addActionListener(e -> {
            this.createActivity();
        });
 
         this.add(mainPane);


    }

    public void createActivity() {
        String name = nameField.getText();
        Duration elapsedTime = Duration.parse(elapsedTimeField.getText());
        Float distance = Float.parseFloat(distanceField.getText());
        Date startDate = null;
        try {
            startDate = formatter.parse(startDateField.getText());
        } catch (ParseException e1) {
            startDate = null;
            showMessageDialog(null, "Wrong date format, use dd-mm-yyyy");
        }
        String type = typeCombo.getSelectedItem().toString();

        
        Long token = athleteController.getToken();
        
        ActivityDTO activity = activityController.createActivity(token, name, distance, elapsedTime, type, startDate);
        if(activity == null){
            showMessageDialog(null, "Error creating activity");
        }
    }

}
