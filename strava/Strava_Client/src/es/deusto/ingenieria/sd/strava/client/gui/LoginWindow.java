package es.deusto.ingenieria.sd.strava.client.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.Registry;

import javax.swing.*;

import es.deusto.ingenieria.sd.strava.client.controller.AthleteController;

public class LoginWindow extends JFrame implements ActionListener {

    private AthleteController athleteController;
    private RegisterWindow registerWindow;

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
        setVisible(true);

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
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        buttonLine.add(loginButton);
        buttonLine.add(registerButton);
        mainPane.add(buttonLine);


        this.add(mainPane);

    }

    public void login() {
    }

    public void setRegisterWindow(RegisterWindow r) {
        this.registerWindow = r;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == registerButton) {

            registerWindow.setVisible(true);
            this.setVisible(false);

        } else if (e.getSource() == loginButton) {

            String email = emailField.getText();
            String password = String.valueOf(passField.getPassword());
            athleteController.login(email, password);

        }

    }

}
