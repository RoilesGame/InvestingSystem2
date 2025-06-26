package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import controller.QuoteController;
import model.Quote;
import model.User;

public class QuoteView extends JFrame {
    private JTable quoteTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private QuoteController controller;
    private User currentUser;

    public QuoteView(User user) {
        this.currentUser = user;
        this.controller = new QuoteController();
        initializeUI();
        loadQuoteData();
        setupAccessControls();
    }

    private void initializeUI() {
        setTitle("Quote Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return currentUser.getRole().equals("admin") && column != 0;
            }
        };
        tableModel.setColumnIdentifiers(new String[]{"ID", "Timestamp", "Price", "Security Paper ID"});

        quoteTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(quoteTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(this::addQuote);
        editButton.addActionListener(this::editQuote);
        deleteButton.addActionListener(this::deleteQuote);

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

    private void loadQuoteData() {
        tableModel.setRowCount(0);
        for (Quote quote : controller.getAllQuotes()) {
            tableModel.addRow(new Object[]{
                    quote.getQuoteId(),
                    quote.getTimestamp(),
                    quote.getPrice(),
                    quote.getSecurityPaperId()
            });
        }
    }

    private void addQuote(ActionEvent e) {
        JTextField priceField = new JTextField();
        JTextField securityPaperIdField = new JTextField();

        Object[] fields = {
                "Price:", priceField,
                "Security Paper ID:", securityPaperIdField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Quote",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                Quote newQuote = new Quote(
                        new Timestamp(System.currentTimeMillis()),
                        new BigDecimal(priceField.getText()),
                        Integer.parseInt(securityPaperIdField.getText())
                );

                if (controller.addQuote(newQuote)) {
                    JOptionPane.showMessageDialog(this, "Quote added successfully");
                    loadQuoteData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editQuote(ActionEvent e) {
        int selectedRow = quoteTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Quote updatedQuote = new Quote(
                        (int) tableModel.getValueAt(selectedRow, 0),
                        (Timestamp) tableModel.getValueAt(selectedRow, 1),
                        new BigDecimal(tableModel.getValueAt(selectedRow, 2).toString()),
                        Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString())
                );

                if (controller.updateQuote(updatedQuote)) {
                    JOptionPane.showMessageDialog(this, "Quote updated successfully");
                    loadQuoteData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating quote", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a quote to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteQuote(ActionEvent e) {
        int selectedRow = quoteTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this quote?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int quoteId = (int) tableModel.getValueAt(selectedRow, 0);
                if (controller.deleteQuote(quoteId)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Quote deleted successfully");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a quote to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}