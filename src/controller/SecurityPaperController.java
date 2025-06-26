package controller;

import model.SecurityPaper;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecurityPaperController {
    public List<SecurityPaper> getAllSecurityPapers() {
        List<SecurityPaper> papers = new ArrayList<>();
        String sql = "SELECT * FROM security_paper";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                papers.add(new SecurityPaper(
                        rs.getInt("security_paper_id"),
                        rs.getString("type"),
                        rs.getBigDecimal("quote")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return papers;
    }

    public boolean addSecurityPaper(SecurityPaper paper) {
        String sql = "INSERT INTO security_paper (type, quote) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, paper.getType());
            pstmt.setBigDecimal(2, paper.getQuote());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSecurityPaper(SecurityPaper paper) {
        String sql = "UPDATE security_paper SET type = ?, quote = ? WHERE security_paper_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paper.getType());
            pstmt.setBigDecimal(2, paper.getQuote());
            pstmt.setInt(3, paper.getSecurityPaperId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSecurityPaper(int paperId) {
        String sql = "DELETE FROM security_paper WHERE security_paper_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, paperId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}