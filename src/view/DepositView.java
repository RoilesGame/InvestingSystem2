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
        controller = new DepositController();
        initializeUI();
        loadDepositData();
        setupAccessControls();
    }

    private void initializeUI() {
        setTitle("Deposit Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return currentUser.getRole().equals("admin") && column != 0;
            }
        };
        tableModel.setColumnIdentifiers(new String[]{"ID", "Type"});

        depositTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(depositTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(this::addDeposit);
        editButton.addActionListener(this::editDeposit);
        deleteButton.addActionListener(this::deleteDeposit);

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

    private void loadDepositData() {
        tableModel.setRowCount(0);
        for (Deposit deposit : controller.getAllDeposits()) {
            tableModel.addRow(new Object[]{
                    deposit.getDepositId(),
                    deposit.getType()
            });
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
            JOptionPane.showMessageDialog(this, "Please select a deposit to edit", "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Please select a deposit to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}