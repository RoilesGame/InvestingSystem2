package controller;

import model.Quote;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuoteController {
    public List<Quote> getAllQuotes() {
        List<Quote> quotes = new ArrayList<>();
        String sql = "SELECT * FROM quote";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                quotes.add(new Quote(
                        rs.getInt("quote_id"),
                        rs.getTimestamp("timestamp"),
                        rs.getBigDecimal("price"),
                        rs.getInt("security_paper_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quotes;
    }

    public boolean addQuote(Quote quote) {
        String sql = "INSERT INTO quote (timestamp, price, security_paper_id) VALUES ('" +
                quote.getTimestamp() + "', " +
                quote.getPrice() + ", " +
                quote.getSecurityPaperId() + ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateQuote(Quote quote) {
        String sql = "UPDATE quote SET " +
                "price = " + quote.getPrice() + ", " +
                "security_paper_id = " + quote.getSecurityPaperId() + " " +
                "WHERE quote_id = " + quote.getQuoteId();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteQuote(int quoteId) {
        String sql = "DELETE FROM quote WHERE quote_id = " + quoteId;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}