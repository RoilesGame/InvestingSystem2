package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import controller.ClientController;
import model.Client;
import model.User;

public class ClientView extends JFrame {
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private ClientController clientController;
    private User currentUser;

    public ClientView(User user) {
        this.currentUser = user;
        this.clientController = new ClientController();
        initializeUI();
        loadClientData();
        setupAccessControls();
    }

    private void initializeUI() {
        setTitle("Client Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(AppColors.BACKGROUND);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return currentUser.getRole().equals("admin") && column != 0;
            }
        };
        tableModel.setColumnIdentifiers(new String[]{"ID", "Title", "Address", "Phone", "Type"});

        clientTable = new JTable(tableModel);
        clientTable.setSelectionBackground(AppColors.SECONDARY);
        clientTable.setSelectionForeground(Color.WHITE);
        clientTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        clientTable.getTableHeader().setBackground(AppColors.PRIMARY);
        clientTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(clientTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppColors.PRIMARY, 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(AppColors.BACKGROUND);

        addButton = createStyledButton("Add", new Color(220, 240, 255));
        editButton = createStyledButton("Edit", new Color(220, 255, 220));
        deleteButton = createStyledButton("Delete", new Color(255, 220, 220));

        addButton.addActionListener(this::addClient);
        editButton.addActionListener(this::editClient);
        deleteButton.addActionListener(this::deleteClient);

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

    private void loadClientData() {
        tableModel.setRowCount(0);
        for (Client client : clientController.getAllClients()) {
            tableModel.addRow(new Object[]{
                    client.getClientId(),
                    client.getTitle(),
                    client.getAddress(),
                    client.getPhoneNumber(),
                    client.getTypeProperty()
            });
        }
    }

    private void addClient(ActionEvent e) {
        JTextField titleField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField typeField = new JTextField();

        Object[] fields = {
                "Title:", titleField,
                "Address:", addressField,
                "Phone:", phoneField,
                "Type:", typeField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Client",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            Client newClient = new Client(
                    titleField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    typeField.getText()
            );

            if (clientController.addClient(newClient)) {
                JOptionPane.showMessageDialog(this, "Client added successfully");
                loadClientData();
            }
        }
    }

    private void editClient(ActionEvent e) {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow >= 0) {
            Client updatedClient = new Client(
                    (int) tableModel.getValueAt(selectedRow, 0),
                    (String) tableModel.getValueAt(selectedRow, 1),
                    (String) tableModel.getValueAt(selectedRow, 2),
                    (String) tableModel.getValueAt(selectedRow, 3),
                    (String) tableModel.getValueAt(selectedRow, 4)
            );

            if (clientController.updateClient(updatedClient)) {
                JOptionPane.showMessageDialog(this, "Client updated successfully");
                loadClientData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a client to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteClient(ActionEvent e) {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this client?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int clientId = (int) tableModel.getValueAt(selectedRow, 0);
                if (clientController.deleteClient(clientId)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Client deleted successfully");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a client to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}