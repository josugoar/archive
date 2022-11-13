package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;

public class LoginWindow extends JFrame {

    private AthleteController athleteController;
    private RegisterWindow registerWindow;
    private MainWindow mainWindow;

    private JPanel mainPane;
    private JButton loginButton;
    private JButton registerButton;
    private JTextField emailField;
    private JLabel emailLabel;
    private JPasswordField passField;
    private JLabel passLabel;


    public LoginWindow(AthleteController athleteController) {
        this.athleteController = athleteController;

        initPane();
        setTitle("Welcome to STRAVA");
        setContentPane(mainPane);
        setSize(400,200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void initPane() {
        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
        JPanel emailLine = new JPanel();
        emailLabel = new JLabel("Email");
        emailField = new JTextField(20);
        emailLine.add(emailLabel, BorderLayout.WEST);
        emailLine.add(emailField, BorderLayout.CENTER);

        JPanel passLine = new JPanel();
        passLabel = new JLabel("Password");
        passField = new JPasswordField(20);
        passLine.add(passLabel, BorderLayout.WEST);
        passLine.add(passField, BorderLayout.CENTER);

        mainPane.add(emailLine);
        mainPane.add(passLine);

        JPanel buttonLine = new JPanel();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        registerButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerWindow.setVisible(true);
                setVisible(false);
            }
        });

        buttonLine.add(loginButton);
        buttonLine.add(registerButton);
        mainPane.add(buttonLine);


        this.add(mainPane);
    }

    public void setRegisterWindow(RegisterWindow r) {
        this.registerWindow = r;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void login() {
        String email = emailField.getText();
        String password = String.valueOf(passField.getPassword());

        if (athleteController.login(email, password))
        {
            this.setVisible(false);
            mainWindow.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Error in login");
        }
    }

}
