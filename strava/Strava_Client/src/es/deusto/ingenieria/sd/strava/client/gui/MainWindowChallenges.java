package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

public class MainWindowChallenges extends JFrame {

	private JPanel contentPane;
	private JTable tableMyChallenges;
	private JTable tableAvailableChallenges;

	private AthleteDialog athleteDialog;
	private ChallengeDialog challengeDialog;
	private MainWindowActivities mainWindowActivities;
	private LoginWindow loginWindow;

	private AthleteController athleteController;
	private ChallengeController challengeController;

	private List<ChallengeDTO> challenges;


	/**
	 * Create the frame.
	 */
	public MainWindowChallenges(AthleteController athleteController, ChallengeController challengeController) {

		this.athleteController = athleteController;
		this.challengeController = challengeController;

		addComponentListener(new ComponentListener() {
            public void componentShown(ComponentEvent e) {
				update();
            }

			@Override
			public void componentResized(ComponentEvent e) {

			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}

        });


		addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                logout();
            }

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}
        });

		setVisible(false);
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
		btnActivities.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				mainWindowActivities.setVisible(true);

			}

		});

		JButton btnChallenges = new JButton("Challenges");
		btnChallenges.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(btnChallenges);

		JLabel lblNewLabel = new JLabel("                                                                                                                                                           ");
		panel.add(lblNewLabel);

		JButton btnUser = new JButton("Profile");
		btnUser.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(btnUser);
		btnUser.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				athleteDialog.setVisible(true);

			}});

		JButton btnNewButton = new JButton("+");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		contentPane.add(btnNewButton, BorderLayout.SOUTH);
		btnNewButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				challengeDialog.setVisible(true);
			}
		});


		JPanel panelCenter = new JPanel();
		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		panelCenter.add(scrollPane);

		tableMyChallenges = new JTable();
		tableMyChallenges.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Distance", "Time", "Sports", "Start Date", "End Date", "Completion"
			}
		));
		tableMyChallenges.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tableMyChallenges.setRowHeight(50);
		scrollPane.setViewportView(tableMyChallenges);

		JPanel panel_2 = new JPanel();
		panelCenter.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

		JButton btnAdd = new JButton("<");
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_2.add(btnAdd);

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int index = tableAvailableChallenges.getSelectedRow();
					challengeController.acceptChallenge(athleteController.getToken(), challenges.get(index));
				} catch (Exception ece) {
					System.err.println("Select row in table");
				}
				update();
			}
		});

		JLabel lblNewLabel_1 = new JLabel(" ");
		panel_2.add(lblNewLabel_1);

		JButton btnRemove = new JButton(">");
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_2.add(btnRemove);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// TODO
				} catch (Exception ece) {
					System.err.println("Select row in table");
				}
				update();
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		panelCenter.add(scrollPane_1);

		tableAvailableChallenges = new JTable();
		tableAvailableChallenges.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Distance", "Time", "Sports", "Start Date", "End Date", "Completion"
			}
		));
		tableAvailableChallenges.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tableAvailableChallenges.setRowHeight(50);
		scrollPane_1.setViewportView(tableAvailableChallenges);
	}



	public List<ChallengeDTO>  getActiveChallenges() {
        return challengeController.getChallenges(this.athleteController.getToken());
    }

	public void setChallengeDialog( final ChallengeDialog challengeDialog) {
        this.challengeDialog = challengeDialog;
        challengeDialog.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {

			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentShown(ComponentEvent e) {

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				update();
                System.err.println("Exited challenge dialog");

			}
        });
    }

	public void setAthleteDialog(final AthleteDialog athleteDialog) {
        this.athleteDialog = athleteDialog;
    }



	private void update() {

		DefaultTableModel activeModel = (DefaultTableModel) tableAvailableChallenges.getModel();
		DefaultTableModel myModel = (DefaultTableModel) tableMyChallenges.getModel();
		myModel.setRowCount(0);
		activeModel.setRowCount(0);
		challenges = new ArrayList<>();
		for (ChallengeDTO challengeDTO : getActiveChallenges()) {

			String time = "";
			Double distance = 0.0;
			if (challengeDTO.getTime() != null) {
				time = challengeDTO.getTime().toString();
			}

			if (challengeDTO.getDistance() != null) {
				distance = challengeDTO.getDistance();
			}
			double progress = getChallengeProgress(challengeDTO);
			if (progress != -1) {
				Object[] data = {
					challengeDTO.getName(),
					distance,
					time,
					challengeDTO.getType(),
					challengeDTO.getStartDate().toString(),
					challengeDTO.getEndDate().toString(),
					progress
				};
				myModel.addRow(data);
			} else {
				Object[] data = {
					challengeDTO.getName(),
					distance,
					time,
					challengeDTO.getType(),
					challengeDTO.getStartDate().toString(),
					challengeDTO.getEndDate().toString()
				};
				challenges.add(challengeDTO);
				activeModel.addRow(data);
			}

        }
		revalidate();
		repaint();

	}

	public void setLoginWindow(LoginWindow l) {
		this.loginWindow = l;
	}

	public void setMainWindowActivities(MainWindowActivities m) {
		this.mainWindowActivities = m;
	}

	public void logout() {
        athleteController.logout();
        setVisible(false);
        loginWindow.setVisible(true);
    }


	public double getChallengeProgress(final ChallengeDTO challengeDTO) {
        return challengeController.getChallengeProgress(athleteController.getToken(), challengeDTO);
    }

}