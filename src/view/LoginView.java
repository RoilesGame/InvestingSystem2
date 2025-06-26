package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import controller.UserController;
import model.User;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Financial Service - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(AppColors.BACKGROUND);

        JLabel headerLabel = new JLabel("Financial Service", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(AppColors.PRIMARY);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(AppColors.BACKGROUND);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(AppColors.TEXT);
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(AppColors.TEXT);
        passwordField = new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        loginButton = createStyledButton("Login", AppColors.PRIMARY);
        loginButton.addActionListener(this::handleLogin);

        registerButton = createStyledButton("Register", AppColors.SECONDARY);
        registerButton.addActionListener(e -> new RegisterView().setVisible(true));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(AppColors.BUTTON_TEXT);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.PRIMARY.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = new UserController().authenticate(username, password);
        if (user != null) {
            dispose();
            new MainView(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}