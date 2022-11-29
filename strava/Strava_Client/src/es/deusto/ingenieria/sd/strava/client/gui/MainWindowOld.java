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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
    private final ActivityController activityController;
    private final AthleteController athleteController;
    private final ChallengeController challengeController;

    private LoginWindow loginWindow;

    private JPanel mainPane;
    private JPanel contentPane;
    private JPanel buttonPane;
    private List<ActivityDTO> activities;
    private List<ChallengeDTO> challenges;
    private JButton bCreateActivity;
    private JButton bCreateChallenge;
    private JButton bViewProfile;
    private JButton bLogout;
    private JTextField challengeIDField;
    private JButton bAccept;


    public MainWindow(final ActivityController activityController, final AthleteController athleteController, final ChallengeController challengeController) {
        this.activityController = activityController;
        this.athleteController = athleteController;
        this.challengeController = challengeController;

        addComponentListener(new ComponentAdapter() {
            public void componentShown(final ComponentEvent e) {
                getActiveChallenges();
                getActivities();
                initPane();
                getContentPane().removeAll();
                getContentPane().add(mainPane);
                revalidate();
                repaint();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                logout();
            }
        });

        setTitle("STRAVA");
        setSize(900, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void initPane() {

        mainPane = new JPanel();

        //scrollPane = new JScrollPane();
        //mainPane.add(scrollPane, BorderLayout.CENTER);

        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        mainPane.add(contentPane, BorderLayout.CENTER);

        buttonPane = new JPanel();
        mainPane.add(buttonPane, BorderLayout.NORTH);

        for (final ActivityDTO activityDTO : activities) {
            final JPanel pane = new JPanel();
            pane.add(new JLabel("Name: " + activityDTO.getName()));
            pane.add(new JLabel("Type :" + activityDTO.getType()));
            pane.add(new JLabel("Distance: " + activityDTO.getDistance()));
            pane.add(new JLabel("Start Date :" + activityDTO.getStartDate().toString()));
            pane.add(new JLabel("Elapsed Time: " + activityDTO.getElapsedTime()));
            contentPane.add(pane);
        }

        for (final ChallengeDTO challengeDTO : challenges) {
            final JPanel pane = new JPanel();
            pane.add(new JLabel("Name: " + challengeDTO.getName()));
            if (challengeDTO.getDistance() != null) {
                pane.add(new JLabel("Distance: " + challengeDTO.getDistance()));
            }
            pane.add(new JLabel("Start Date: " + challengeDTO.getStartDate().toString()));
            pane.add(new JLabel("End Date: " + challengeDTO.getEndDate().toString()));
            if (challengeDTO.getTime() != null) {
                pane.add(new JLabel("Time: " + challengeDTO.getTime().toString()));
            }
            pane.add(new JLabel("Completion :" + getChallengeProgress(challengeDTO)));
            for (final String sportType : challengeDTO.getType()) {
                pane.add(new JLabel(sportType));
            }
            contentPane.add(pane);
        }

        bCreateActivity = new JButton("New Activity");
        bCreateChallenge = new JButton("New Challenge");
        bViewProfile = new JButton("Profile");
        bLogout = new JButton("Log Out");
        bAccept = new JButton("Accept Challenge");
        challengeIDField = new JTextField(20);

        mainPane.add(challengeIDField, BorderLayout.SOUTH);

        buttonPane.add(bCreateActivity);
        buttonPane.add(bCreateChallenge);
        buttonPane.add(bViewProfile);
        buttonPane.add(bLogout);
        buttonPane.add(bAccept);

        bAccept.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                acceptChallenge();

            }

        });

        bCreateActivity.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                activityDialog.setVisible(true);

            }

        });

        bCreateChallenge.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                challengeDialog.setVisible(true);

            }

        });

        bViewProfile.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                athleteDialog.setVisible(true);

            }

        });

        bLogout.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                logout();

            }

        });

    }

    public void setLoginWindow(final LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
    }

    public void setActivityDialog(final ActivityDialog activityDialog) {
        this.activityDialog = activityDialog;
        activityDialog.addComponentListener(new ComponentAdapter() {
            public void componentHidden(final ComponentEvent e) {
                getActiveChallenges();
                getActivities();
                initPane();
                getContentPane().removeAll();
                getContentPane().add(mainPane);
                revalidate();
                repaint();
            }
        });
    }

    public void setChallengeDialog( final ChallengeDialog challengeDialog) {
        this.challengeDialog = challengeDialog;
        challengeDialog.addComponentListener(new ComponentAdapter() {
            public void componentHidden(final ComponentEvent e) {
                getActiveChallenges();
                getActivities();
                initPane();
                getContentPane().removeAll();
                getContentPane().add(mainPane);
                revalidate();
                repaint();
                System.err.println("Exited challenge dialog");
            }
        });
    }

    public void setAthleteDialog(final AthleteDialog athleteDialog) {
        this.athleteDialog = athleteDialog;
    }

    public void getActivities() {
        activities = activityController.getActivities(this.athleteController.getToken());
    }

    public void getActiveChallenges() {
        challenges = challengeController.getChallenges(this.athleteController.getToken());
        System.err.println("Challenges: " + challenges);
    }

    public void acceptChallenge() {
        try {
            if (true) {
                JOptionPane.showMessageDialog(rootPane, "Error accepting challenge");

            } else {
                getActiveChallenges();
                getActivities();
                initPane();
                getContentPane().removeAll();
                getContentPane().add(mainPane);
                revalidate();
                repaint();
            }
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Challenge ID not an Integer");
        }

    }

    public double getChallengeProgress(final ChallengeDTO challengeDTO) {
        return challengeController.getChallengeProgress(athleteController.getToken(), challengeDTO);
    }

    public void logout() {
        athleteController.logout();
        setVisible(false);
        loginWindow.setVisible(true);
    }

}

