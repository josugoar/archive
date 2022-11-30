package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RegisterWindow {

	private JFrame frmStravaLogin;

	private CompleteRegisterWindow completeRegisterWindow;
	private LoginWindow loginWindow;

	/**
	 * Create the application.
	 */
	public RegisterWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStravaLogin = new JFrame();
		frmStravaLogin.setTitle("STRAVA Register");
		frmStravaLogin.setBounds(100, 100, 492, 474);
		frmStravaLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStravaLogin.getContentPane().setLayout(new BoxLayout(frmStravaLogin.getContentPane(), BoxLayout.Y_AXIS));

		JPanel panel_1 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_1);

		JPanel panel = new JPanel();
		frmStravaLogin.getContentPane().add(panel);

		JButton btnNewButton = new JButton("        Login with Google        ");
		panel.add(btnNewButton);
		btnNewButton.setBackground(new Color(128, 0, 0));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 30));

		JPanel panel_2 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_2);

		JButton btnNewButton_1 = new JButton("      Login with Facebook      ");
		panel_2.add(btnNewButton_1);
		btnNewButton_1.setBackground(new Color(0, 128, 255));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 30));

		JPanel panel_3 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_3);

		JButton btnRegister = new JButton("              Subscribe              ");
		panel_3.add(btnRegister);
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		JPanel panel_4 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_4);

		JButton btnLogin = new JButton("                 Login                 ");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 30));
		panel_4.add(btnLogin);

		JPanel panel_5 = new JPanel();
		frmStravaLogin.getContentPane().add(panel_5);

		// Button Actions

		btnLogin.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                loginWindow.setVisible(true);
                frmStravaLogin.setVisible(false);
            }

        });


		btnRegister.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
				completeRegisterWindow.setLoginType("local");
                completeRegisterWindow.setVisible(true);
				frmStravaLogin.setVisible(false);
            }

        });

		// Google Register
		btnNewButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
				completeRegisterWindow.setLoginType("google");
                completeRegisterWindow.setVisible(true);
				frmStravaLogin.setVisible(false);
            }

        });

		// Facebook Register
		btnNewButton_1.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
				completeRegisterWindow.setLoginType("facebook");
                completeRegisterWindow.setVisible(true);
				frmStravaLogin.setVisible(false);
            }

        });

	}


	public void setVisible(boolean b){
		frmStravaLogin.setVisible(b);
	}

	public void setLoginWindow(final LoginWindow l) {
        this.loginWindow = l;
    }

	public void setCompleteRegisterWindow(final CompleteRegisterWindow c) {
		this.completeRegisterWindow = c;
	}

}
