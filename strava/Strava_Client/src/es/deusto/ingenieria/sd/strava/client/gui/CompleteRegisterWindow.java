package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;
import es.deusto.ingenieria.sd.strava.server.data.dto.AthleteDTO;

public class CompleteRegisterWindow extends JFrame {

	private String loginType;
	private JPanel contentPane;
	private JTextField emailField;
	private JPasswordField passField;
	private JTextField nameField;
	private JTextField heightField;
	private JTextField weightField;
	private JTextField birthField;
	private JTextField maxField;
	private JTextField restField;

	private AthleteController athleteController;
	private RegisterWindow registerWindow;
	private MainWindowActivities mainWindow;

	SimpleDateFormat formatter = new SimpleDateFormat("DD-mm-yyyy", Locale.ENGLISH);



	/**
	 * Create the frame.
	 */
	public CompleteRegisterWindow(AthleteController athleteController) {
		this.athleteController = athleteController;

		setTitle("Complete Registration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 638, 662);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel panel_10 = new JPanel();
		contentPane.add(panel_10);

		JPanel panel = new JPanel();
		contentPane.add(panel);

		JLabel lblNewLabel = new JLabel("                                   Email  ");
		lblNewLabel.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel.add(lblNewLabel);

		emailField = new JTextField();
		emailField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(30);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);

		JLabel lblNewLabel_1 = new JLabel("                             Password  ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_1.add(lblNewLabel_1);

		passField = new JPasswordField();
		passField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_1.add(passField);
		passField.setColumns(30);

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);

		JLabel lblNewLabel_2 = new JLabel("                                  Name  ");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_2.add(lblNewLabel_2);

		nameField = new JTextField();
		nameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_2.add(nameField);
		nameField.setColumns(30);

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);

		JLabel lblNewLabel_3 = new JLabel("                       Height ( cm )  ");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_3.add(lblNewLabel_3);

		heightField = new JTextField();
		heightField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_3.add(heightField);
		heightField.setColumns(30);

		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);

		JLabel lblNewLabel_4 = new JLabel("                       Weight ( kg )  ");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_4.add(lblNewLabel_4);

		weightField = new JTextField();
		weightField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_4.add(weightField);
		weightField.setColumns(30);

		JPanel panel_5 = new JPanel();
		contentPane.add(panel_5);

		JLabel lblNewLabel_5 = new JLabel("Date of Birth ( dd-MM-yyyy ) ");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_5.add(lblNewLabel_5);

		birthField = new JTextField();
		birthField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_5.add(birthField);
		birthField.setColumns(30);

		JPanel panel_6 = new JPanel();
		contentPane.add(panel_6);

		JLabel lblNewLabel_6 = new JLabel("Maximum Heart Rate ( bpm )");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_6.add(lblNewLabel_6);

		maxField = new JTextField();
		maxField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_6.add(maxField);
		maxField.setColumns(30);

		JPanel panel_7 = new JPanel();
		contentPane.add(panel_7);

		JLabel lblNewLabel_7 = new JLabel("Resting Heart Rate ( bpm )   ");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.ITALIC, 16));
		panel_7.add(lblNewLabel_7);

		restField = new JTextField();
		restField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_7.add(restField);
		restField.setColumns(30);

		JPanel panel_8 = new JPanel();
		contentPane.add(panel_8);

		JButton btnBack = new JButton("      Back      ");
		panel_8.add(btnBack);
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 18));

		JButton btnCreate = new JButton("Create Account");
		panel_8.add(btnCreate);
		btnCreate.setFont(new Font("Tahoma", Font.BOLD, 18));

		JPanel panel_9 = new JPanel();
		contentPane.add(panel_9);

		//Button Actions

		btnBack.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                setVisible(false);
				registerWindow.setVisible(true);
            }
        });

		btnCreate.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                register();
            }

        });


	}

	public void setLoginType(String type) {
		loginType = type;
	}


	public void setMainWindow(final MainWindowActivities mainWindow) {
        this.mainWindow = mainWindow;
    }

	public void setRegisterWindow(final RegisterWindow registerWindow) {
        this.registerWindow = registerWindow;
    }

	public void register() {
        final String email = emailField.getText();
        final String password = String.valueOf(passField.getPassword());
        final String name = nameField.getText();

        java.util.Date dateOfBirth = null;
        try {
            dateOfBirth = formatter.parse(birthField.getText());
        } catch (final ParseException e1) {
        }

        Double weight = null;
        try {
            weight = Double.parseDouble(weightField.getText());
        } catch (final Exception e) {
        }

        Integer height = null;
        try {
            height = Integer.parseInt(heightField.getText());
        } catch (final Exception e) {
        }

        Integer maxHeartRate = null;
        try {
            maxHeartRate = Integer.parseInt(maxField.getText());
        } catch (final Exception e) {
        }

        Integer restingHeartRate = null;
        try {
            restingHeartRate = Integer.parseInt(restField.getText());
        } catch (final Exception e) {
        }

        final AthleteDTO athleteDTO = new AthleteDTO();
        athleteDTO.setName(name);
        athleteDTO.setEmail(email);
        athleteDTO.setDateOfBirth(dateOfBirth);
        athleteDTO.setWeight(weight);
        athleteDTO.setHeight(height);
        athleteDTO.setRestingHeartRate(restingHeartRate);
        athleteDTO.setMaxHeartRate(maxHeartRate);

		if (loginType.equals("google")) {
			if (athleteController.registerGoogle(athleteDTO) && athleteController.login(email, password)) {
				this.setVisible(false);
				mainWindow.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(rootPane, "Error in registration");
			}
		} else if (loginType.equals("facebook")) {
			if (athleteController.registerFacebook(athleteDTO) && athleteController.login(email, password)) {
				this.setVisible(false);
				mainWindow.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(rootPane, "Error in registration");
			}
		} else if (loginType.equals("local")) {
			if (athleteController.register(password, athleteDTO) && athleteController.login(email, password)) {
				this.setVisible(false);
				mainWindow.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(rootPane, "Error in registration");
			}
		} else {
			System.err.println("Wrong login Type value");
		}

    }


}


