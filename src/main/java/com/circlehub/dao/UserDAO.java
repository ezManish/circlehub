package com.circlehub.dao;

import com.circlehub.model.User;
import com.circlehub.util.DBConnection;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDAO {

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    User u = map(rs);
                    return Optional.of(u);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public boolean create(User user, String plainPasswordHash) {
        String sql = "INSERT INTO users (uuid, username, email, password_hash, avatar_path, tokens, display_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, user.getUuid() == null ? UUID.randomUUID().toString() : user.getUuid());
            p.setString(2, user.getUsername());
            p.setString(3, user.getEmail());
            p.setString(4, plainPasswordHash);
            p.setString(5, user.getAvatarPath());
            p.setInt(6, user.getTokens());
            p.setString(7, user.getDisplayName());
            int rows = p.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = p.getGeneratedKeys()) { if (keys.next()) user.setId(keys.getInt(1)); }
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUuid(rs.getString("uuid"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setAvatarPath(rs.getString("avatar_path"));
        u.setTokens(rs.getInt("tokens"));
        u.setDisplayName(rs.getString("display_name"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
