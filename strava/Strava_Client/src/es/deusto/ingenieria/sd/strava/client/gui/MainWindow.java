package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

public class MainWindow extends JFrame {

    private ChallengeDialog challengeDialog;
    private ActivityDialog activityDialog;
    private AthleteDialog athleteDialog;
    private ActivityController activityController;
    private AthleteController athleteController;
    private ChallengeController challengeController;

    private RegisterWindow registerWindow;

    private JPanel mainPane;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JPanel buttonPane;
    private List<ActivityDTO> activities;
    private List<ChallengeDTO> challenges;
    private JButton bCreateActivity;
    private JButton bCreateChallenge;
    private JButton bViewProfile;
    private JButton bLogout;


    public MainWindow(ActivityController activityController, AthleteController athleteController, ChallengeController challengeController) {
        this.activityController = activityController;
        this.athleteController = athleteController;
        this.challengeController = challengeController;

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                getActiveChallenges();
                getActivities();
                initPane();
                getContentPane().add(mainPane);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        setTitle("STRAVA");
        setSize(400, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void initPane() {

        mainPane = new JPanel();

        scrollPane = new JScrollPane();
        mainPane.add(scrollPane, BorderLayout.CENTER);

        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        scrollPane.add(contentPane);

        buttonPane = new JPanel();
        mainPane.add(buttonPane, BorderLayout.SOUTH);

        for (ActivityDTO activityDTO : activities) {
            JPanel pane = new JPanel();
            pane.add(new JLabel("Name: " + activityDTO.getName()));
            pane.add(new JLabel("Type :" + activityDTO.getType()));
            pane.add(new JLabel("Distance: " + activityDTO.getDistance()));
            pane.add(new JLabel("Start Date :" + activityDTO.getStartDate().toString()));
            pane.add(new JLabel("Elapsed Time: " + activityDTO.getElapsedTime().toString()));
            contentPane.add(pane);
        }

        for (ChallengeDTO challengeDTO : challenges) {
            JPanel pane = new JPanel();
            pane.add(new JLabel("Name: " + challengeDTO.getName()));
            pane.add(new JLabel("Distance: " + challengeDTO.getDistance()));
            pane.add(new JLabel("Start Date: " + challengeDTO.getStartDate().toString()));
            pane.add(new JLabel("End Date: " + challengeDTO.getEndDate().toString()));
            pane.add(new JLabel("Time: " + challengeDTO.getTime().toString()));
            pane.add(new JLabel("Completion :" + getChallengeState(challengeDTO.getId())));
            if (challengeDTO.isCycling()) {
                pane.add(new JLabel("Cycling"));
            } else if (challengeDTO.isRunning()) {
                pane.add(new JLabel("Running"));
            }
            contentPane.add(pane);
        }

        bCreateActivity = new JButton("New Activity");
        bCreateChallenge = new JButton("New Challenge");
        bViewProfile = new JButton("Profile");
        bLogout = new JButton("Log Out");

        buttonPane.add(bCreateActivity);
        buttonPane.add(bCreateChallenge);
        buttonPane.add(bViewProfile);
        buttonPane.add(bLogout);

        bCreateActivity.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                activityDialog.setVisible(true);

            }

        });

        bCreateChallenge.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                challengeDialog.setVisible(true);

            }

        });

        bViewProfile.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                athleteDialog.setVisible(true);

            }

        });

        bLogout.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                logout();

            }

        });

    }

    public void setRegisterWindow(RegisterWindow registerWindow) {
        this.registerWindow = registerWindow;
    }

    public void setActivityDialog(ActivityDialog activityDialog) {
        this.activityDialog = activityDialog;
        activityDialog.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                getActiveChallenges();
                getActivities();
                initPane();
            }
        });
    }

    public void setChallengeDialog( ChallengeDialog challengeDialog) {
        this.challengeDialog = challengeDialog;
        challengeDialog.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                getActiveChallenges();
                getActivities();
                initPane();
            }
        });
    }

    public void setAthleteDialog(AthleteDialog athleteDialog) {
        this.athleteDialog = athleteDialog;
    }

    public void getActivities() {
        activities = activityController.getActivities(this.athleteController.getToken());
    }

    public void getActiveChallenges() {
        challenges = challengeController.getActiveChallenges(this.athleteController.getToken());
    }

    public void acceptChallenge() {
        // TODO
    }

    public float getChallengeState(Integer id) {
        return challengeController.getChallengeState(athleteController.getToken(), id);
    }

    public void logout() {
        athleteController.logout();
        setVisible(false);
        registerWindow.setVisible(true);
    }

}

