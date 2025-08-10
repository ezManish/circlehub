package com.circlehub.dao;

import com.circlehub.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenServiceDAO {

    public int getUserTokens(int userId) {
        String sql = "SELECT tokens FROM users WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, userId);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return rs.getInt("tokens");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public boolean addTokens(int userId, int amount) {
        if (amount <= 0) return false;
        String sql = "UPDATE users SET tokens = tokens + ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, amount);
            p.setInt(2, userId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean transferTokens(int fromUserId, int toUserId, int amount) {
        if (fromUserId == toUserId || amount <= 0) return false;
        String selectForUpdate = "SELECT tokens FROM users WHERE id = ? FOR UPDATE";
        String updateFrom = "UPDATE users SET tokens = tokens - ? WHERE id = ? AND tokens >= ?";
        String updateTo = "UPDATE users SET tokens = tokens + ? WHERE id = ?";
        String insertContribution = "INSERT INTO contributions (from_user_id, to_user_id, tokens_amount) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection()) {
            try {
                c.setAutoCommit(false);
                try (PreparedStatement ps = c.prepareStatement(selectForUpdate)) {
                    ps.setInt(1, fromUserId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { c.rollback(); return false; }
                        int balance = rs.getInt("tokens");
                        if (balance < amount) { c.rollback(); return false; }
                    }
                }
                try (PreparedStatement pd = c.prepareStatement(updateFrom)) {
                    pd.setInt(1, amount); pd.setInt(2, fromUserId); pd.setInt(3, amount);
                    if (pd.executeUpdate() == 0) { c.rollback(); return false; }
                }
                try (PreparedStatement pt = c.prepareStatement(updateTo)) {
                    pt.setInt(1, amount); pt.setInt(2, toUserId);
                    if (pt.executeUpdate() == 0) { c.rollback(); return false; }
                }
                try (PreparedStatement pin = c.prepareStatement(insertContribution)) {
                    pin.setInt(1, fromUserId); pin.setInt(2, toUserId); pin.setInt(3, amount); pin.executeUpdate();
                }
                c.commit();
                return true;
            } catch (SQLException ex) { c.rollback(); ex.printStackTrace(); return false; } finally { c.setAutoCommit(true); }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
