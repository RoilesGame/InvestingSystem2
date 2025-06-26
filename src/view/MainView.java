package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import model.User;

public class MainView extends JFrame {
    private User currentUser;
    private JLabel welcomeLabel;

    public MainView(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Main Menu - " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem profileItem = new JMenuItem("Profile");
        JMenuItem exitItem = new JMenuItem("Exit");

        profileItem.addActionListener(e -> {
            new ProfileView(currentUser, this::updateWelcomeMessage).setVisible(true);
        });

        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(profileItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu dataMenu = new JMenu("Data");

        if (currentUser.getRole().equals("admin")) {
            dataMenu.add(new JMenuItem("Clients")).addActionListener(e -> new ClientView(currentUser).setVisible(true));
            dataMenu.add(new JMenuItem("Companies")).addActionListener(e -> new CompanyView(currentUser).setVisible(true));
            dataMenu.add(new JMenuItem("Deposits")).addActionListener(e -> new DepositView(currentUser).setVisible(true));
            dataMenu.add(new JMenuItem("Security Papers")).addActionListener(e -> new SecurityPaperView(currentUser).setVisible(true));
            dataMenu.add(new JMenuItem("Quotes")).addActionListener(e -> new QuoteView(currentUser).setVisible(true));
        } else {
            dataMenu.add(new JMenuItem("Clients")).addActionListener(e -> new ClientView(currentUser).setVisible(true));
            dataMenu.add(new JMenuItem("Deposits")).addActionListener(e -> new DepositView(currentUser).setVisible(true));
        }

        menuBar.add(fileMenu);
        menuBar.add(dataMenu);
        setJMenuBar(menuBar);

        welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
    }

    public void updateWelcomeMessage() {
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
        setTitle("Main Menu - " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        revalidate();
        repaint();
    }
}