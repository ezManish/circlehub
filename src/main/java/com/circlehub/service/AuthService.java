package com.circlehub.service;

import com.circlehub.dao.UserDAO;
import com.circlehub.model.User;
import com.circlehub.util.PasswordUtil;

import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public Optional<User> register(User user, String plainPassword) throws SQLException {
        String hashed = PasswordUtil.hashPassword(plainPassword);
        boolean ok = userDAO.create(user, hashed);
        if (ok) {
            return userDAO.findById(user.getId());
        }
        return Optional.empty();
    }

    public Optional<User> login(String email, String plainPassword) throws SQLException {
        Optional<User> opt = userDAO.findByEmail(email);
        if (opt.isEmpty()) return Optional.empty();
        User u = opt.get();
        if (PasswordUtil.verifyPassword(plainPassword, u.getPasswordHash())) return Optional.of(u);
        return Optional.empty();
    }
}
