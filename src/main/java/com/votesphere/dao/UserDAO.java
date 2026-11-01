package com.votesphere.dao;

import com.votesphere.model.User;
import com.votesphere.model.UserSummary;
import com.votesphere.util.Database;
import com.votesphere.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User authenticate(String login, String password) throws SQLException {
        String sql = "SELECT id, full_name, username, email, password_hash, role FROM users "
                + "WHERE username = ? OR email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, login);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next() || !PasswordUtil.matches(password, result.getString("password_hash"))) {
                    return null;
                }
                return new User(result.getLong("id"), result.getString("full_name"),
                        result.getString("username"), result.getString("email"), result.getString("role"));
            }
        }
    }

    public void register(String fullName, String username, String email, String password) throws SQLException {
        String sql = "INSERT INTO users(full_name, username, email, password_hash) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fullName);
            statement.setString(2, username);
            statement.setString(3, email);
            statement.setString(4, PasswordUtil.hash(password));
            statement.executeUpdate();
        }
    }

    public long count() throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users");
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getLong(1);
        }
    }

    public long countVoters() throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE role = 'VOTER'");
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getLong(1);
        }
    }

    public List<UserSummary> findRecent() throws SQLException {
        String sql = "SELECT full_name, username, email, role, created_at FROM users ORDER BY created_at DESC LIMIT 100";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            List<UserSummary> users = new ArrayList<>();
            while (result.next()) {
                users.add(new UserSummary(result.getString("full_name"), result.getString("username"),
                        result.getString("email"), result.getString("role"),
                        result.getTimestamp("created_at").toLocalDateTime()));
            }
            return users;
        }
    }
}
