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

public class CreateChallenge extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField startField;
	private JTextField endField;
	private JTextField distanceField;
	private JTextField timeField;


	/**
	 * Create the dialog.
	 */
	public CreateChallenge() {
		setTitle("Create Challenge");
		setBounds(100, 100, 563, 576);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		JPanel panel_7 = new JPanel();
		contentPanel.add(panel_7);
		
		JPanel panel = new JPanel();
		contentPanel.add(panel);
		
		JLabel lblNewLabel = new JLabel("                            Name    ");
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel.add(lblNewLabel);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(nameField);
		nameField.setColumns(20);
		
		JPanel panel_1 = new JPanel();
		contentPanel.add(panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("Start Date ( DD/MM/YYYY )");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_1.add(lblNewLabel_1);
		
		startField = new JTextField();
		startField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_1.add(startField);
		startField.setColumns(20);
		
		JPanel panel_2 = new JPanel();
		contentPanel.add(panel_2);
		
		JLabel lblNewLabel_2 = new JLabel(" End Date ( DD/MM/YYYY )");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_2.add(lblNewLabel_2);
		
		endField = new JTextField();
		endField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_2.add(endField);
		endField.setColumns(20);
		
		JPanel panel_3 = new JPanel();
		contentPanel.add(panel_3);
		
		JLabel lblNewLabel_3 = new JLabel("                         Distance  ");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_3.add(lblNewLabel_3);
		
		distanceField = new JTextField();
		distanceField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_3.add(distanceField);
		distanceField.setColumns(20);
		
		JPanel panel_4 = new JPanel();
		contentPanel.add(panel_4);
		
		JLabel lblNewLabel_4 = new JLabel("                            Time    ");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_4.add(lblNewLabel_4);
		
		timeField = new JTextField();
		timeField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_4.add(timeField);
		timeField.setColumns(20);
		
		JPanel panel_5 = new JPanel();
		contentPanel.add(panel_5);
		
		JComboBox sportBox = new JComboBox();
		sportBox.setToolTipText("");
		sportBox.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_5.add(sportBox);
		
		JPanel panel_6 = new JPanel();
		contentPanel.add(panel_6);
		
		JButton btnCreate = new JButton("Create Challenge");
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_6.add(btnCreate);
		
		JButton btnBack = new JButton("           Back            ");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_6.add(btnBack);
	}

}
