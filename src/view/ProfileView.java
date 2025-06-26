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
        setTitle("Profile");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Username field
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField(currentUser.getUsername());
        panel.add(usernameField);

        // Password field
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(currentUser.getPassword());
        panel.add(passwordField);

        // Update button
        updateButton = new JButton("Update");
        updateButton.addActionListener(this::handleUpdate);
        panel.add(updateButton);

        add(panel);
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
            // Refresh user data from DB
            User updatedUser = userController.getUserById(currentUser.getUserId());
            if (updatedUser != null) {
                currentUser.setUsername(updatedUser.getUsername());
                currentUser.setPassword(updatedUser.getPassword());
                currentUser.setRole(updatedUser.getRole());
            }

            JOptionPane.showMessageDialog(this, "Profile updated successfully");
            onProfileUpdate.run(); // Trigger welcome message update
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}