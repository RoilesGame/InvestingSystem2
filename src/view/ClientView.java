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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return currentUser.getRole().equals("admin") && column != 0;
            }
        };
        tableModel.setColumnIdentifiers(new String[]{"ID", "Title", "Address", "Phone", "Type"});

        clientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(clientTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(this::addClient);
        editButton.addActionListener(this::editClient);
        deleteButton.addActionListener(this::deleteClient);

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