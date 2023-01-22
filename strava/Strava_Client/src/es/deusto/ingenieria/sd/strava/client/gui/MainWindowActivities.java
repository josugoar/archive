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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;

public class MainWindowActivities extends JFrame {

	private JPanel contentPane;
	private JTable tableActivities;

    private ActivityDialog activityDialog;
    private AthleteDialog athleteDialog;
    private ActivityController activityController;
    private AthleteController athleteController;
	private LoginWindow loginWindow;
	private MainWindowChallenges mainWindowChallenges;

	private List<ActivityDTO> activities = new ArrayList<>();




	/**
	 * Create the frame.
	 */
	public MainWindowActivities(final ActivityController activityController, final AthleteController athleteController) {
        this.activityController = activityController;
        this.athleteController = athleteController;


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
		btnChallenges.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				mainWindowChallenges.setVisible(true);

			}});

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
				activityDialog.setVisible(true);
			}});

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
		update();
	}

	private void update() {

		getActivities();
		// TODO Refresh activity table
		DefaultTableModel model = (DefaultTableModel) tableActivities.getModel();
		model.setRowCount(0); // Clear table
		for (ActivityDTO activityDTO : activities) {
			Object[] row = {
				activityDTO.getName(),
				activityDTO.getDistance(),
				activityDTO.getElapsedTime(),
				activityDTO.getType(),
				activityDTO.getStartDate().toString()
			};
			model.addRow(row);
        }

		revalidate();
		repaint();
	}


	public void logout() {
        athleteController.logout();
        setVisible(false);
        loginWindow.setVisible(true);
    }



	public void setLoginWindow(LoginWindow l) {
		this.loginWindow = l;
	}

	public void setMainWindowChallenges(MainWindowChallenges m) {
		this.mainWindowChallenges = m;
	}

	public void setActivityDialog(final ActivityDialog activityDialog) {
        this.activityDialog = activityDialog;
        activityDialog.addComponentListener(new ComponentListener() {
            public void componentHidden(final ComponentEvent e) {
                update();
            }

			@Override
			public void componentResized(ComponentEvent e) {

			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentShown(ComponentEvent e) {

			}
        });
    }


	public void setAthleteDialog(final AthleteDialog athleteDialog) {
        this.athleteDialog = athleteDialog;
    }

	public void getActivities() {
		final Long token = athleteController.getToken();

		if (token == null) {
			return;
		}

        activities = activityController.getActivities(token);
    }


}

