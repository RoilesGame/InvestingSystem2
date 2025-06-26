package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import controller.SecurityPaperController;
import model.SecurityPaper;
import model.User;

public class SecurityPaperView extends JFrame {
    private JTable paperTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private SecurityPaperController controller;
    private User currentUser;

    public SecurityPaperView(User user) {
        this.currentUser = user;
        this.controller = new SecurityPaperController();
        initializeUI();
        loadPaperData();
        setupAccessControls();
    }

    private void initializeUI() {
        setTitle("Security Paper Management");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(AppColors.BACKGROUND);

        // Header
        JLabel headerLabel = new JLabel("Security Papers", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(AppColors.PRIMARY);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Type", "Quote"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return currentUser.getRole().equals("admin") && column != 0;
            }
        };

        paperTable = new JTable(tableModel);
        AppColors.styleTable(paperTable);

        JScrollPane scrollPane = new JScrollPane(paperTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppColors.PRIMARY, 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel - Инициализация кнопок ДО их использования
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        addButton = createStyledButton("Add", new Color(220, 240, 255));
        editButton = createStyledButton("Edit", new Color(220, 255, 220));
        deleteButton = createStyledButton("Delete", new Color(255, 220, 220));

        addButton.addActionListener(this::addPaper);
        editButton.addActionListener(this::editPaper);
        deleteButton.addActionListener(this::deletePaper);

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
        if (addButton != null) addButton.setEnabled(isAdmin);
        if (editButton != null) editButton.setEnabled(isAdmin);
        if (deleteButton != null) deleteButton.setEnabled(isAdmin);
    }

    private void loadPaperData() {
        tableModel.setRowCount(0);
        try {
            for (SecurityPaper paper : controller.getAllSecurityPapers()) {
                tableModel.addRow(new Object[]{
                        paper.getSecurityPaperId(),
                        paper.getType(),
                        paper.getQuote()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading security papers: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addPaper(ActionEvent e) {
        JTextField typeField = new JTextField();
        JTextField quoteField = new JTextField();

        Object[] fields = {
                "Type:", typeField,
                "Quote:", quoteField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Security Paper",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                SecurityPaper newPaper = new SecurityPaper(
                        typeField.getText(),
                        new BigDecimal(quoteField.getText())
                );

                if (controller.addSecurityPaper(newPaper)) {
                    JOptionPane.showMessageDialog(this, "Security paper added successfully");
                    loadPaperData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editPaper(ActionEvent e) {
        int selectedRow = paperTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                SecurityPaper updatedPaper = new SecurityPaper(
                        (int) tableModel.getValueAt(selectedRow, 0),
                        (String) tableModel.getValueAt(selectedRow, 1),
                        new BigDecimal(tableModel.getValueAt(selectedRow, 2).toString())
                );

                if (controller.updateSecurityPaper(updatedPaper)) {
                    JOptionPane.showMessageDialog(this, "Security paper updated successfully");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a paper to edit",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePaper(ActionEvent e) {
        int selectedRow = paperTable.getSelectedRow();
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
                    JOptionPane.showMessageDialog(this, "Security paper deleted successfully");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a paper to delete",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}