package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.client.controller.ChallengeController;
import es.deusto.ingenieria.sd.strava.server.data.dto.ChallengeDTO;

public class ChallengeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField startField;
	private JTextField endField;
	private JTextField distanceField;
	private JTextField timeField;
	private JComboBox<String> sportCombo;

	SimpleDateFormat formatter = new SimpleDateFormat("DD-mm-yyyy", Locale.ENGLISH);

	private ChallengeController challengeController;
	private AthleteController athleteController;
	private String sport[] = { "RUNNING", "CYCLING", "BOTH" };




	/**
	 * Create the dialog.
	 */
	public ChallengeDialog(ChallengeController challengeController, AthleteController athleteController) {
		this.challengeController = challengeController;
		this.athleteController = athleteController;

		setTitle("Create Challenge");
		setBounds(100, 100, 563, 576);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
        setModal(true);
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

		sportCombo = new JComboBox<String>(sport);
		sportCombo.setToolTipText("");
		sportCombo.setFont(new Font("Tahoma", Font.ITALIC, 18));
		panel_5.add(sportCombo);

		JPanel panel_6 = new JPanel();
		contentPanel.add(panel_6);

		JButton btnCreate = new JButton("Create Challenge");
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createChallenge();
			}
		});
		panel_6.add(btnCreate);

		JButton btnBack = new JButton("           Back            ");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		panel_6.add(btnBack);

	}

	public void createChallenge() {
        String name = nameField.getText();

        Integer time = null;
        try {
            time = Integer.valueOf(timeField.getText());
        } catch (Exception e) {
        }

        Double distance = null;
        try {
            if (distanceField.getText() != null) {
                distance = Double.parseDouble(distanceField.getText());
            }
        } catch (NumberFormatException e) {
        }

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = formatter.parse(startField.getText());
            endDate = formatter.parse(endField.getText());
        } catch (RuntimeException | ParseException e1) {
			JOptionPane.showMessageDialog(rootPane, "Wrong date, use DD-mm-yyyy");
			return;
        }

        String sport = sportCombo.getSelectedItem().toString();
        Set<String> type = new HashSet<>();
        if (sport.equals("BOTH")) {
            type.add("CYCLING");
            type.add("RUNNING");
        } else {
            type.add(sport);
        }

        Long token = athleteController.getToken();

        ChallengeDTO challenge = new ChallengeDTO();
        challenge.setName(name);
        challenge.setStartDate(startDate);
        challenge.setEndDate(endDate);
        challenge.setDistance(distance);
        challenge.setTime(time);
        challenge.setType(type);
        if (!challengeController.createChallenge(token, challenge)) {
			JOptionPane.showMessageDialog(rootPane, "Error creating challenge");
			return;
        }

        setVisible(false);
    }

}
