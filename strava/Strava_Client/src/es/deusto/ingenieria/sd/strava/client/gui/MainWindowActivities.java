package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

import javax.swing.JScrollPane;

public class MainWindowActivities extends JFrame {

	private JPanel contentPane;
	private JTable tableActivities;

    private ActivityDialog activityDialog;
    private AthleteDialog athleteDialog;
    private ActivityController activityController;
    private AthleteController athleteController;
	private LoginWindow loginWindow;

	private List<ActivityDTO> activities;




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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

        });


		addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                logout();
            }

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
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
		
		JLabel lblNewLabel = new JLabel("                                                                                                                                                           ");
		panel.add(lblNewLabel);
		
		JButton btnUser = new JButton("Profile");
		btnUser.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(btnUser);
		
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
		// TODO Refresh avtivity table
		DefaultTableModel model = (DefaultTableModel) tableActivities.getModel();
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

	public void setActivityDialog(final ActivityDialog activityDialog) {
        this.activityDialog = activityDialog;
        activityDialog.addComponentListener(new ComponentListener() {
            public void componentHidden(final ComponentEvent e) {
                update();
            }

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
    }


	public void setAthleteDialog(final AthleteDialog athleteDialog) {
        this.athleteDialog = athleteDialog;
    }

	public void getActivities() {
        activities = activityController.getActivities(this.athleteController.getToken());
    }


}

