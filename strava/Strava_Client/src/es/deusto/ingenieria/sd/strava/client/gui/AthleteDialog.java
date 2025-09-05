package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;

public class AthleteDialog extends JFrame {

	private JPanel contentPane;
	private JTable atributeTable;

	private AthleteController athleteController;
	private LoginWindow loginWindow;
	private AthleteDTO athlete;

	/**
	 * Create the frame.
	 */
	public AthleteDialog(AthleteController athleteController, LoginWindow loginWindow) {
		this.athleteController = athleteController;
		this.loginWindow = loginWindow;

		addComponentListener(new ComponentListener() {
            public void componentShown(ComponentEvent e) {
				getAthlete();
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
	}

	public void getAthlete() {
        athlete = athleteController.getAthlete();

		setTitle("Athlete Profile");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 537, 582);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		contentPane.add(panel);

		JLabel lblNewLabel = new JLabel("Athlete Info ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(lblNewLabel);

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);

		String name = athlete.getName();
		String email = athlete.getEmail();
		Integer height = athlete.getHeight();
		Double weight = athlete.getWeight();
		Integer resting = athlete.getRestingHeartRate();
		Integer max = athlete.getMaxHeartRate();
		String date = athlete.getDateOfBirth().toString();


		atributeTable = new JTable();
		atributeTable.setFillsViewportHeight(true);
		atributeTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		atributeTable.setModel(new DefaultTableModel(
			new Object[][] {
				{"Name: \n", name},
				{"Email: \n", email},
				{"Height: \n", height},
				{"Weight: \n", weight},
				{"Resting Heartrate: \n", resting},
				{"Maximum Heartrate: \n", max},
				{"Date of Birth: \n", date},
			},
			new String[] {
				"Atribute", "Value"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		atributeTable.getColumnModel().getColumn(0).setResizable(false);
		atributeTable.getColumnModel().getColumn(1).setResizable(false);
		atributeTable.setPreferredSize(new Dimension(500, 350));
		atributeTable.setRowHeight(50);
		panel_2.add(atributeTable);

		JPanel panel_8 = new JPanel();
		contentPane.add(panel_8);

		JButton btnBack = new JButton("Back");
		panel_8.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 20));

		JButton btnLogout = new JButton("Log Out");
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Window window : getWindows()) {
					window.setVisible(false);
				}
				loginWindow.setVisible(true);
				athleteController.logout();
			}
		});
		panel_8.add(btnLogout);
    }

}

