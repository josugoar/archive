package es.deusto.ingenieria.sd.strava.client.gui;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import es.deusto.ingenieria.sd.strava.client.controller.ActivityController;
import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.server.data.dto.ActivityDTO;

public class ActivityDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField distanceField;
	private JTextField timeField;
	private JTextField startField;
	private JComboBox<String> sportBox;
	private String type[] = { "RUNNING", "CYCLING" };

    SimpleDateFormat formatter = new SimpleDateFormat("DD-mm-yyyy");

	private AthleteController athleteController;
	private ActivityController activityController;

	/**
	 * Create the dialog.
	 */
	public ActivityDialog(ActivityController activityController, AthleteController athleteController) {

		setTitle("Create Activity");
		setBounds(100, 100, 578, 615);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
        setModal(true);
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

		sportBox = new JComboBox<>(type);
		panel_4.add(sportBox);
		sportBox.setToolTipText("");
		sportBox.setFont(new Font("Tahoma", Font.ITALIC, 18));

		JPanel panel_5 = new JPanel();
		contentPanel.add(panel_5);

		JButton btnCreate = new JButton("  Create Activity  ");
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createActivity();;
			}
		});
		panel_5.add(btnCreate);

		JButton btnBack = new JButton("          Back          ");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		panel_5.add(btnBack);
	}

	public void createActivity() {
        String name = nameField.getText();

        int elapsedTime = 0;
        try {
            elapsedTime = Integer.parseInt(timeField.getText());
        } catch (Exception e) {
            System.err.println("Wrong elapsed time");
        }

        Double distance = null;
        try {
            distance = Double.parseDouble(distanceField.getText());
        } catch (Exception e) {
            System.err.println("Wrong distance");
        }

        Date startDate = null;
        try {
            startDate = formatter.parse(startField.getText());
        } catch (ParseException e1) {
            System.err.println("Wrong date, use dd-mm-yyyy");
        }
        String type = sportBox.getSelectedItem().toString();

        Long token = athleteController.getToken();

        final ActivityDTO activity = new ActivityDTO();
        activity.setName(name);
        activity.setDistance(distance);
        activity.setElapsedTime(elapsedTime);
        activity.setType(type);
        activity.setStartDate(startDate);
        if (!activityController.createActivity(token, activity)) {
			System.err.println("Error creating activity");
        }
        setVisible(false);
    }

}
