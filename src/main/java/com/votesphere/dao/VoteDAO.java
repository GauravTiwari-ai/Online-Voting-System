package com.votesphere.dao;

import com.votesphere.model.VoteRecord;
import com.votesphere.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoteDAO {
    public void castVote(long pollId, long optionId, long userId) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String pollSql = "SELECT status, deadline FROM polls WHERE id = ? FOR UPDATE";
                try (PreparedStatement pollStatement = connection.prepareStatement(pollSql)) {
                    pollStatement.setLong(1, pollId);
                    try (ResultSet poll = pollStatement.executeQuery()) {
                        if (!poll.next()) {
                            throw new IllegalStateException("This poll is not open for voting.");
                        }
                        java.time.LocalDateTime deadline = poll.getObject("deadline", java.time.LocalDateTime.class);
                        if (!"ACTIVE".equals(poll.getString("status"))
                                || (deadline != null && !deadline.isAfter(java.time.LocalDateTime.now()))) {
                            throw new IllegalStateException("This poll is not open for voting.");
                        }
                    }
                }
                try (PreparedStatement optionStatement = connection.prepareStatement(
                        "SELECT id FROM voting_options WHERE id = ? AND poll_id = ?")) {
                    optionStatement.setLong(1, optionId);
                    optionStatement.setLong(2, pollId);
                    try (ResultSet option = optionStatement.executeQuery()) {
                        if (!option.next()) {
                            throw new IllegalArgumentException("Select a valid option for this poll.");
                        }
                    }
                }
                try (PreparedStatement voteStatement = connection.prepareStatement(
                        "INSERT INTO votes(poll_id, option_id, user_id) VALUES (?, ?, ?)")) {
                    voteStatement.setLong(1, pollId);
                    voteStatement.setLong(2, optionId);
                    voteStatement.setLong(3, userId);
                    voteStatement.executeUpdate();
                }
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<VoteRecord> findByUser(long userId) throws SQLException {
        String sql = "SELECT v.poll_id, p.title, o.option_text, p.status, v.voted_at, p.show_results "
                + "FROM votes v JOIN polls p ON p.id = v.poll_id "
                + "JOIN voting_options o ON o.id = v.option_id WHERE v.user_id = ? ORDER BY v.voted_at DESC";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                List<VoteRecord> records = new ArrayList<>();
                while (result.next()) {
                    records.add(new VoteRecord(result.getLong("poll_id"), result.getString("title"),
                            result.getString("option_text"), result.getString("status"),
                            result.getTimestamp("voted_at").toLocalDateTime(), result.getBoolean("show_results")));
                }
                return records;
            }
        }
    }

    public long count() throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM votes");
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getLong(1);
        }
    }

    public long countByUser(long userId) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM votes WHERE user_id = ?")) {
            statement.setLong(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getLong(1);
            }
        }
    }
}
