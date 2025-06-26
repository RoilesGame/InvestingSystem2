package view;

import javax.swing.*;
import java.awt.*;

import model.User;

public class MainView extends JFrame {
    private User currentUser;
    private JLabel welcomeLabel;

    public MainView(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Financial Service - " + currentUser.getUsername());
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(AppColors.BACKGROUND);


        welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(AppColors.PRIMARY);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(AppColors.BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton profileButton = createMenuButton("Profile");
        profileButton.addActionListener(e -> new ProfileView(currentUser, this::updateWelcomeMessage).setVisible(true));
        buttonPanel.add(profileButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Отступ между кнопками

        JButton clientsButton = createMenuButton("Clients");
        clientsButton.addActionListener(e -> new ClientView(currentUser).setVisible(true));
        buttonPanel.add(clientsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton depositsButton = createMenuButton("Deposits");
        depositsButton.addActionListener(e -> new DepositView(currentUser).setVisible(true));
        buttonPanel.add(depositsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        if (currentUser.getRole().equals("admin")) {
            JButton dealsButton = createMenuButton("Deals");
            dealsButton.addActionListener(e -> new DealView(currentUser).setVisible(true));
            buttonPanel.add(dealsButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            JButton papersButton = createMenuButton("Security Papers");
            papersButton.addActionListener(e -> new SecurityPaperView(currentUser).setVisible(true));
            buttonPanel.add(papersButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(AppColors.TEXT);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.PRIMARY, 1),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(200, 50));
        return button;
    }

    public void updateWelcomeMessage() {
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
    }
}