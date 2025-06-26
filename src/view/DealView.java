package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import controller.DealController;
import model.Deal;
import model.User;

public class DealView extends JFrame {
    private JTable dealTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private DealController controller;
    private User currentUser;

    public DealView(User user) {
        this.currentUser = user;
        this.controller = new DealController();
        initializeUI();
        loadDealData();
        //setupAccessControls();
    }

    private void initializeUI() {
        setTitle("Deal Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(AppColors.BACKGROUND);

        JLabel headerLabel = new JLabel("Deal Management", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(AppColors.PRIMARY);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Client ID", "Deposit ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return currentUser.getRole().equals("admin") && column != 0;
            }
        };

        dealTable = new JTable(tableModel);
        AppColors.styleTable(dealTable);

        JScrollPane scrollPane = new JScrollPane(dealTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppColors.PRIMARY, 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        addButton = createStyledButton("Add", new Color(220, 240, 255));
        editButton = createStyledButton("Edit", new Color(220, 255, 220));
        deleteButton = createStyledButton("Delete", new Color(255, 220, 220));

        addButton.addActionListener(this::addDeal);
        editButton.addActionListener(this::editDeal);
        deleteButton.addActionListener(this::deleteDeal);

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

    private void loadDealData() {
        tableModel.setRowCount(0);
        try {
            for (Deal deal : controller.getAllDeals()) {
                tableModel.addRow(new Object[]{
                        deal.getDealId(),
                        deal.getClientId(),
                        deal.getDepositId()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading deals: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDeal(ActionEvent e) {
        JTextField clientIdField = new JTextField();
        JTextField depositIdField = new JTextField();
        JTextField securityPaperIdField = new JTextField();

        Object[] fields = {
                "Client ID:", clientIdField,
                "Deposit ID:", depositIdField,
                "Security Paper ID (optional):", securityPaperIdField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Deal",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                Integer securityPaperId = securityPaperIdField.getText().isEmpty() ?
                        null : Integer.parseInt(securityPaperIdField.getText());

                Deal newDeal = new Deal(
                        Integer.parseInt(clientIdField.getText()),
                        Integer.parseInt(depositIdField.getText()),
                        securityPaperId
                );

                if (controller.addDeal(newDeal)) {
                    JOptionPane.showMessageDialog(this, "Deal added successfully");
                    loadDealData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editDeal(ActionEvent e) {
        int selectedRow = dealTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int dealId = (int) tableModel.getValueAt(selectedRow, 0);
                int clientId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                int depositId = Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString());

                Deal updatedDeal = new Deal(dealId, clientId, depositId, null);

                if (controller.updateDeal(updatedDeal)) {
                    JOptionPane.showMessageDialog(this, "Deal updated successfully");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating deal. Check that all IDs are numbers.\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a deal to edit",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDeal(ActionEvent e) {
        int selectedRow = dealTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this deal?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int dealId = (int) tableModel.getValueAt(selectedRow, 0);
                if (controller.deleteDeal(dealId)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Deal deleted successfully");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a deal to delete",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}