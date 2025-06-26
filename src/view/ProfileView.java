package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import controller.UserController;
import model.User;

public class ProfileView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton updateButton;
    private UserController userController;
    private User currentUser;
    private Runnable onProfileUpdate;

    public ProfileView(User user, Runnable onProfileUpdate) {
        this.currentUser = user;
        this.onProfileUpdate = onProfileUpdate;
        this.userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Profile Settings");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(AppColors.BACKGROUND);

        JLabel headerLabel = new JLabel("Edit Profile", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(AppColors.PRIMARY);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBackground(AppColors.BACKGROUND);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField = new JTextField(currentUser.getUsername());
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField = new JPasswordField(currentUser.getPassword());
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel());

        updateButton = new JButton("Save Changes");
        updateButton.setBackground(AppColors.TEXT);
        updateButton.setForeground(AppColors.TEXT);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.addActionListener(this::handleUpdate);
        formPanel.add(updateButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void handleUpdate(ActionEvent e) {
        String newUsername = usernameField.getText().trim();
        String newPassword = new String(passwordField.getPassword());

        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser.setUsername(newUsername);
        currentUser.setPassword(newPassword);

        if (userController.updateUser(currentUser)) {

            User updatedUser = userController.getUserById(currentUser.getUserId());
            if (updatedUser != null) {
                currentUser.setUsername(updatedUser.getUsername());
                currentUser.setPassword(updatedUser.getPassword());
                currentUser.setRole(updatedUser.getRole());
            }

            JOptionPane.showMessageDialog(this, "Profile updated successfully");
            onProfileUpdate.run();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}