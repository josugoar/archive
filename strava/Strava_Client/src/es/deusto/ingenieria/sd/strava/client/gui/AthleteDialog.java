package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;

public class AthleteDialog extends JFrame {

	private JPanel contentPane;
	private JTable atributeTable;

	private AthleteController athleteController;
	private AthleteDTO athlete;

	/**
	 * Create the frame.
	 */
	public AthleteDialog(AthleteController athleteController) {
		this.athleteController = athleteController;

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
		
		atributeTable = new JTable();
		atributeTable.setFillsViewportHeight(true);
		atributeTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		atributeTable.setModel(new DefaultTableModel(
			new Object[][] {
				{"Name: \n", null},
				{"Email: \n", null},
				{"Height: \n", null},
				{"Weight: \n", null},
				{"Resting Heartrate: \n", null},
				{"Maximum Heartrate: \n", null},
				{"Date of Birth: \n", null},
			},
			new String[] {
				"Atribute", "Value"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, true
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
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JButton btnLogout = new JButton("Log Out");
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_8.add(btnLogout);
	}

	public void getAthlete() {
        athlete = athleteController.getAthlete();
    }

}

