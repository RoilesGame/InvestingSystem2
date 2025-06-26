package controller;

import model.Client;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("client_id"),
                        rs.getString("title"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("type_property")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public boolean addClient(Client client) {
        String sql = "INSERT INTO client (title, address, phone_number, type_property) VALUES ('" +
                client.getTitle() + "', '" +
                client.getAddress() + "', '" +
                client.getPhoneNumber() + "', '" +
                client.getTypeProperty() + "')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateClient(Client client) {
        String sql = "UPDATE client SET " +
                "title = '" + client.getTitle() + "', " +
                "address = '" + client.getAddress() + "', " +
                "phone_number = '" + client.getPhoneNumber() + "', " +
                "type_property = '" + client.getTypeProperty() + "' " +
                "WHERE client_id = " + client.getClientId();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClient(int clientId) {
        String sql = "DELETE FROM client WHERE client_id = " + clientId;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}