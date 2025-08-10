package com.circlehub.dao;

import com.circlehub.model.Post;
import com.circlehub.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostDAO {

    public boolean create(Post post) {
        String sql = "INSERT INTO posts (uuid, author_id, title, body, image_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, post.getUuid() == null ? UUID.randomUUID().toString() : post.getUuid());
            p.setInt(2, post.getAuthorId());
            p.setString(3, post.getTitle());
            p.setString(4, post.getBody());
            p.setString(5, post.getImagePath());
            int rows = p.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = p.getGeneratedKeys()) { if (keys.next()) post.setId(keys.getInt(1)); }
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<Post> listRecent(int limit) {
        List<Post> out = new ArrayList<>();
        String sql = "SELECT p.*, u.username FROM posts p JOIN users u ON p.author_id = u.id ORDER BY p.created_at DESC LIMIT ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, limit);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Post post = map(rs);
                    out.add(post);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    public Optional<Post> findByUuid(String uuid) {
        String sql = "SELECT p.*, u.username FROM posts p JOIN users u ON p.author_id = u.id WHERE p.uuid = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, uuid);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    private Post map(ResultSet rs) throws SQLException {
        Post p = new Post();
        p.setId(rs.getInt("id"));
        p.setUuid(rs.getString("uuid"));
        p.setAuthorId(rs.getInt("author_id"));
        p.setTitle(rs.getString("title"));
        p.setBody(rs.getString("body"));
        p.setImagePath(rs.getString("image_path"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
