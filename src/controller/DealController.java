package controller;

import model.Deal;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DealController {
    public List<Deal> getAllDeals() {
        List<Deal> deals = new ArrayList<>();
        String sql = "SELECT * FROM deal";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                deals.add(new Deal(
                        rs.getInt("deal_id"),
                        rs.getInt("client_id"),
                        rs.getInt("deposit_id"),
                        rs.getObject("security_paper_id") == null ? null : rs.getInt("security_paper_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deals;
    }

    public boolean addDeal(Deal deal) {
        String sql = "INSERT INTO deal (client_id, deposit_id, security_paper_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deal.getClientId());
            pstmt.setInt(2, deal.getDepositId());
            if (deal.getSecurityPaperId() != null) {
                pstmt.setInt(3, deal.getSecurityPaperId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDeal(Deal deal) {
        String sql = "UPDATE deal SET client_id=?, deposit_id=?, security_paper_id=? WHERE deal_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deal.getClientId());
            pstmt.setInt(2, deal.getDepositId());

            if (deal.getSecurityPaperId() != null) {
                pstmt.setInt(3, deal.getSecurityPaperId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.setInt(4, deal.getDealId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDeal(int dealId) {
        String sql = "DELETE FROM deal WHERE deal_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, dealId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}