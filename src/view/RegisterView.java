package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.UserController;
import model.User;

public class RegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton cancelButton;

    public RegisterView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(AppColors.BACKGROUND);

        JLabel headerLabel = new JLabel("Create New Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(AppColors.PRIMARY);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBackground(AppColors.BACKGROUND);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String[] roles = {"user", "admin"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(roleLabel);
        formPanel.add(roleComboBox);
        formPanel.add(new JLabel());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        registerButton = new JButton("Register");
        registerButton.setBackground(AppColors.PRIMARY);
        registerButton.setForeground(AppColors.BUTTON_TEXT);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BUTTON_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(AppColors.SECONDARY);
        cancelButton.setForeground(AppColors.BUTTON_TEXT);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BUTTON_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = new User(username, password, role);
        UserController userController = new UserController();

        if (userController.registerUser(newUser)) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful! Please login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed. Username may already exist.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}