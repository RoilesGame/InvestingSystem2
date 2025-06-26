package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import controller.SecurityPaperController;
import model.SecurityPaper;
import model.User;

public class SecurityPaperView extends JFrame {
    private JTable securityPaperTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private SecurityPaperController controller;
    private User currentUser;

    public SecurityPaperView(User user) {
        this.currentUser = user;
        controller = new SecurityPaperController();
        initializeUI();
        loadSecurityPaperData();
        setupAccessControls();
    }

    private void initializeUI() {
        setTitle("Security Paper Management");
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

        securityPaperTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(securityPaperTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(this::addSecurityPaper);
        editButton.addActionListener(this::editSecurityPaper);
        deleteButton.addActionListener(this::deleteSecurityPaper);

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

    private void loadSecurityPaperData() {
        tableModel.setRowCount(0);
        for (SecurityPaper paper : controller.getAllSecurityPapers()) {
            tableModel.addRow(new Object[]{
                    paper.getSecurityPaperId(),
                    paper.getType()
            });
        }
    }

    private void addSecurityPaper(ActionEvent e) {
        JTextField typeField = new JTextField();

        Object[] fields = {
                "Type:", typeField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Security Paper",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION && !typeField.getText().isEmpty()) {
            SecurityPaper newPaper = new SecurityPaper(typeField.getText());
            if (controller.addSecurityPaper(newPaper)) {
                JOptionPane.showMessageDialog(this, "Security Paper added successfully");
                loadSecurityPaperData();
            }
        }
    }

    private void editSecurityPaper(ActionEvent e) {
        int selectedRow = securityPaperTable.getSelectedRow();
        if (selectedRow >= 0) {
            SecurityPaper updatedPaper = new SecurityPaper(
                    (int) tableModel.getValueAt(selectedRow, 0),
                    (String) tableModel.getValueAt(selectedRow, 1)
            );

            if (controller.updateSecurityPaper(updatedPaper)) {
                JOptionPane.showMessageDialog(this, "Security Paper updated successfully");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a security paper to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSecurityPaper(ActionEvent e) {
        int selectedRow = securityPaperTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this security paper?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int paperId = (int) tableModel.getValueAt(selectedRow, 0);
                if (controller.deleteSecurityPaper(paperId)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Security Paper deleted successfully");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a security paper to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}