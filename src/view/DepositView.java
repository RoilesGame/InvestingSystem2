package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import controller.DepositController;
import model.Deposit;
import model.User;

public class DepositView extends JFrame {
    private JTable depositTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private DepositController controller;
    private User currentUser;

    public DepositView(User user) {
        this.currentUser = user;
        this.controller = new DepositController();
        initializeUI();
        loadDepositData();
        setupAccessControls();
    }

    private void initializeUI() {
        setTitle("Deposit Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(AppColors.BACKGROUND);

        JLabel headerLabel = new JLabel("Deposit Management", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(AppColors.PRIMARY);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Type"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return currentUser.getRole().equals("admin") && column != 0;
            }
        };

        depositTable = new JTable(tableModel);
        AppColors.styleTable(depositTable);

        JScrollPane scrollPane = new JScrollPane(depositTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppColors.PRIMARY, 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        addButton = createStyledButton("Add", new Color(220, 240, 255));
        editButton = createStyledButton("Edit", new Color(220, 255, 220));
        deleteButton = createStyledButton("Delete", new Color(255, 220, 220));

        addButton.addActionListener(this::addDeposit);
        editButton.addActionListener(this::editDeposit);
        deleteButton.addActionListener(this::deleteDeposit);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(AppColors.BUTTON_TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BUTTON_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        return button;
    }

    private void setupAccessControls() {
        boolean isAdmin = currentUser.getRole().equals("admin");
        addButton.setEnabled(isAdmin);
        editButton.setEnabled(isAdmin);
        deleteButton.setEnabled(isAdmin);
    }

    private void loadDepositData() {
        tableModel.setRowCount(0);
        try {
            for (Deposit deposit : controller.getAllDeposits()) {
                tableModel.addRow(new Object[]{
                        deposit.getDepositId(),
                        deposit.getType()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading deposits: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDeposit(ActionEvent e) {
        JTextField typeField = new JTextField();

        Object[] fields = {
                "Type:", typeField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Deposit",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION && !typeField.getText().isEmpty()) {
            Deposit newDeposit = new Deposit(typeField.getText());
            if (controller.addDeposit(newDeposit)) {
                JOptionPane.showMessageDialog(this, "Deposit added successfully");
                loadDepositData();
            }
        }
    }

    private void editDeposit(ActionEvent e) {
        int selectedRow = depositTable.getSelectedRow();
        if (selectedRow >= 0) {
            Deposit updatedDeposit = new Deposit(
                    (int) tableModel.getValueAt(selectedRow, 0),
                    (String) tableModel.getValueAt(selectedRow, 1)
            );

            if (controller.updateDeposit(updatedDeposit)) {
                JOptionPane.showMessageDialog(this, "Deposit updated successfully");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a deposit to edit",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDeposit(ActionEvent e) {
        int selectedRow = depositTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this deposit?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int depositId = (int) tableModel.getValueAt(selectedRow, 0);
                if (controller.deleteDeposit(depositId)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Deposit deleted successfully");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a deposit to delete",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}