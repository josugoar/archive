package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;

import javax.swing.JScrollPane;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTable tableActivities;

	private ChallengeDialog challengeDialog;
    private ActivityDialog activityDialog;
    private AthleteDialog athleteDialog;
    private final ActivityController activityController;
    private final AthleteController athleteController;
    private final ChallengeController challengeController;
	private final LoginWindow loginWindow;



	/**
	 * Create the frame.
	 */
	public MainWindow(final ActivityController activityController, final AthleteController athleteController, final ChallengeController challengeController) {
        this.activityController = activityController;
        this.athleteController = athleteController;
        this.challengeController = challengeController;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 971, 573);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JButton btnActivities = new JButton("Activities");
		btnActivities.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(btnActivities);
		
		JButton btnChallenges = new JButton("Challenges");
		btnChallenges.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(btnChallenges);
		
		JLabel lblNewLabel = new JLabel("                                                                                                                                                           ");
		panel.add(lblNewLabel);
		
		JButton btnUser = new JButton("Profile");
		btnUser.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(btnUser);
		
		JButton btnNewButton = new JButton("+");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		contentPane.add(btnNewButton, BorderLayout.SOUTH);
		
		JScrollPane scrollActivities = new JScrollPane();
		contentPane.add(scrollActivities, BorderLayout.CENTER);
		
		tableActivities = new JTable();
		tableActivities.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Distance", "Elapsed Time", "Sport", "Start Date"
			}
		));
		tableActivities.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tableActivities.setRowHeight(50);
		scrollActivities.setViewportView(tableActivities);
	}

}

