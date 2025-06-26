package controller;

import model.Company;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyController {
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM company";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                companies.add(new Company(
                        rs.getInt("company_id"),
                        rs.getInt("client_id"),
                        rs.getObject("deposit_id") == null ? null : rs.getInt("deposit_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    public boolean addCompany(Company company) {
        String sql = "INSERT INTO company (client_id, deposit_id, security_paper_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, company.getClientId());
            if (company.getDepositId() != null) {
                pstmt.setInt(2, company.getDepositId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            if (company.getSecurityPaperId() != null) {
                pstmt.setInt(3, company.getSecurityPaperId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCompany(Company company) {
        String sql = "UPDATE company SET client_id=?, deposit_id=?, security_paper_id=? WHERE company_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, company.getClientId());

            // Обработка null значений для deposit_id
            if (company.getDepositId() != null) {
                pstmt.setInt(2, company.getDepositId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            // Обработка null значений для security_paper_id
            if (company.getSecurityPaperId() != null) {
                pstmt.setInt(3, company.getSecurityPaperId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.setInt(4, company.getCompanyId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCompany(int companyId) {
        String sql = "DELETE FROM company WHERE company_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, companyId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}