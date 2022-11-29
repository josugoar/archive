package es.deusto.ingenieria.sd.strava.client.gui;


import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class CreateActivity extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField distanceField;
	private JTextField timeField;
	private JTextField startField;


	/**
	 * Create the dialog.
	 */
	public CreateActivity() {
		setTitle("Create Activity");
		setBounds(100, 100, 578, 615);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		JPanel panel_6 = new JPanel();
		contentPanel.add(panel_6);
		
		JPanel panel = new JPanel();
		contentPanel.add(panel);
		
		JLabel lblNewLabel = new JLabel("                            Name    ");
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel.add(lblNewLabel);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameField.setColumns(20);
		panel.add(nameField);
		
		JPanel panel_1 = new JPanel();
		contentPanel.add(panel_1);
		
		JLabel lblDistance = new JLabel("                        Distance    ");
		lblDistance.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_1.add(lblDistance);
		
		distanceField = new JTextField();
		distanceField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		distanceField.setColumns(20);
		panel_1.add(distanceField);
		
		JPanel panel_2 = new JPanel();
		contentPanel.add(panel_2);
		
		JLabel lblElapsedTime = new JLabel("       Elapsed Time ( days )  ");
		lblElapsedTime.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_2.add(lblElapsedTime);
		
		timeField = new JTextField();
		timeField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		timeField.setColumns(20);
		panel_2.add(timeField);
		
		JPanel panel_3 = new JPanel();
		contentPanel.add(panel_3);
		
		JLabel lblStartDate = new JLabel("Start Date ( DD/MM/YYYY )");
		panel_3.add(lblStartDate);
		lblStartDate.setFont(new Font("Tahoma", Font.ITALIC, 18));
		
		startField = new JTextField();
		startField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		startField.setColumns(20);
		panel_3.add(startField);
		
		JPanel panel_4 = new JPanel();
		contentPanel.add(panel_4);
		
		JComboBox sportBox = new JComboBox();
		panel_4.add(sportBox);
		sportBox.setToolTipText("");
		sportBox.setFont(new Font("Tahoma", Font.ITALIC, 18));
		
		JPanel panel_5 = new JPanel();
		contentPanel.add(panel_5);
		
		JButton btnCreate = new JButton("  Create Activity  ");
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_5.add(btnCreate);
		
		JButton btnBack = new JButton("          Back          ");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_5.add(btnBack);
	}

}
