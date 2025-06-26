package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import controller.CompanyController;
import model.Company;
import model.User;

public class CompanyView extends JFrame {
    private JTable companyTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private CompanyController controller;
    private User currentUser;

    public CompanyView(User user) {
        this.currentUser = user;
        controller = new CompanyController();
        initializeUI();
        loadCompanyData();
        setupAccessControls();
    }

    private void initializeUI() {
        setTitle("Company Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return currentUser.getRole().equals("admin") && column != 0;
            }
        };
        tableModel.setColumnIdentifiers(new String[]{"ID", "Client ID", "Deposit ID", "Security Paper ID"});

        companyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(companyTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(this::addCompany);
        editButton.addActionListener(this::editCompany);
        deleteButton.addActionListener(this::deleteCompany);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupAccessControls() {
        boolean isAdmin = currentUser.getRole().equals("admin");
        addButton.setEnabled(isAdmin);
        editButton.setEnabled(isAdmin);
        deleteButton.setEnabled(isAdmin);
    }

    private void loadCompanyData() {
        tableModel.setRowCount(0);
        for (Company company : controller.getAllCompanies()) {
            tableModel.addRow(new Object[]{
                    company.getCompanyId(),
                    company.getClientId(),
                    company.getDepositId(),
                    company.getSecurityPaperId()
            });
        }
    }

    private void addCompany(ActionEvent e) {
        JTextField clientIdField = new JTextField();
        JTextField depositIdField = new JTextField();
        JTextField securityPaperIdField = new JTextField();

        Object[] fields = {
                "Client ID:", clientIdField,
                "Deposit ID:", depositIdField,
                "Security Paper ID:", securityPaperIdField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Company",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                Company newCompany = new Company(
                        Integer.parseInt(clientIdField.getText()),
                        depositIdField.getText().isEmpty() ? null : Integer.parseInt(depositIdField.getText()),
                        securityPaperIdField.getText().isEmpty() ? null : Integer.parseInt(securityPaperIdField.getText())
                );

                if (controller.addCompany(newCompany)) {
                    JOptionPane.showMessageDialog(this, "Company added successfully");
                    loadCompanyData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCompany(ActionEvent e) {
        int selectedRow = companyTable.getSelectedRow();
        if (selectedRow >= 0) {
            Company updatedCompany = new Company(
                    (int) tableModel.getValueAt(selectedRow, 0),
                    (int) tableModel.getValueAt(selectedRow, 1),
                    tableModel.getValueAt(selectedRow, 2) == null ? null : (int) tableModel.getValueAt(selectedRow, 2),
                    tableModel.getValueAt(selectedRow, 3) == null ? null : (int) tableModel.getValueAt(selectedRow, 3)
            );

            if (controller.updateCompany(updatedCompany)) {
                JOptionPane.showMessageDialog(this, "Company updated successfully");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a company to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCompany(ActionEvent e) {
        int selectedRow = companyTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this company?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int companyId = (int) tableModel.getValueAt(selectedRow, 0);
                if (controller.deleteCompany(companyId)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Company deleted successfully");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a company to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}