package com.votesphere.dao;

import com.votesphere.model.Poll;
import com.votesphere.model.PollOption;
import com.votesphere.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PollDAO {
    public List<Poll> findAll(Long userId) throws SQLException {
        String sql = "SELECT p.id, p.title, p.description, p.status, p.show_results, p.deadline, p.created_at, "
                + "COUNT(DISTINCT v.id) total_votes, COUNT(DISTINCT o.id) option_count, "
                + "MAX(CASE WHEN v.user_id = ? THEN 1 ELSE 0 END) voted "
                + "FROM polls p LEFT JOIN votes v ON v.poll_id = p.id "
                + "LEFT JOIN voting_options o ON o.poll_id = p.id "
                + "GROUP BY p.id ORDER BY p.created_at DESC";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                List<Poll> polls = new ArrayList<>();
                while (result.next()) {
                    polls.add(mapPoll(result, List.of()));
                }
                return polls;
            }
        }
    }

    public Poll findById(long id, Long userId) throws SQLException {
        String sql = "SELECT p.id, p.title, p.description, p.status, p.show_results, p.deadline, p.created_at, "
                + "COUNT(DISTINCT v.id) total_votes, COUNT(DISTINCT o.id) option_count, "
                + "MAX(CASE WHEN v.user_id = ? THEN 1 ELSE 0 END) voted "
                + "FROM polls p LEFT JOIN votes v ON v.poll_id = p.id "
                + "LEFT JOIN voting_options o ON o.poll_id = p.id WHERE p.id = ? GROUP BY p.id";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.setLong(2, id);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                Poll poll = mapPoll(result, findOptions(connection, id));
                return poll;
            }
        }
    }

    private List<PollOption> findOptions(Connection connection, long pollId) throws SQLException {
        String sql = "SELECT o.id, o.poll_id, o.option_text, o.description, COUNT(v.id) vote_count "
                + "FROM voting_options o LEFT JOIN votes v ON v.option_id = o.id "
                + "WHERE o.poll_id = ? GROUP BY o.id ORDER BY o.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, pollId);
            try (ResultSet result = statement.executeQuery()) {
                List<PollOption> options = new ArrayList<>();
                while (result.next()) {
                    options.add(new PollOption(result.getLong("id"), result.getLong("poll_id"),
                            result.getString("option_text"), result.getString("description"),
                            result.getLong("vote_count")));
                }
                return options;
            }
        }
    }

    private Poll mapPoll(ResultSet result, List<PollOption> options) throws SQLException {
        LocalDateTime deadline = result.getObject("deadline", LocalDateTime.class);
        Timestamp createdAt = result.getTimestamp("created_at");
        return new Poll(result.getLong("id"), result.getString("title"), result.getString("description"),
                result.getString("status"), result.getBoolean("show_results"),
                deadline,
                createdAt == null ? null : createdAt.toLocalDateTime(),
                result.getLong("total_votes"), result.getLong("option_count"), options, result.getBoolean("voted"));
    }

    public long create(String title, String description, String status, boolean showResults,
                       LocalDateTime deadline, long creatorId, List<String> options) throws SQLException {
        String pollSql = "INSERT INTO polls(title, description, status, show_results, deadline, created_by) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement pollStatement = connection.prepareStatement(pollSql, Statement.RETURN_GENERATED_KEYS)) {
                pollStatement.setString(1, title);
                pollStatement.setString(2, description);
                pollStatement.setString(3, status);
                pollStatement.setBoolean(4, showResults);
                pollStatement.setObject(5, deadline);
                pollStatement.setLong(6, creatorId);
                pollStatement.executeUpdate();
                try (ResultSet keys = pollStatement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("Creating poll did not return an ID.");
                    }
                    long pollId = keys.getLong(1);
                    insertOptions(connection, pollId, options);
                    connection.commit();
                    return pollId;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void insertOptions(Connection connection, long pollId, List<String> options) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO voting_options(poll_id, option_text) VALUES (?, ?)")) {
            for (String option : options) {
                statement.setLong(1, pollId);
                statement.setString(2, option);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public void updateStatus(long pollId, String status) throws SQLException {
        if ("ACTIVE".equals(status)) {
            String validationSql = "SELECT p.deadline, (SELECT COUNT(*) FROM voting_options o WHERE o.poll_id = p.id) "
                    + "option_count FROM polls p WHERE p.id = ?";
            try (Connection connection = Database.getConnection();
                 PreparedStatement validation = connection.prepareStatement(validationSql)) {
                validation.setLong(1, pollId);
                try (ResultSet result = validation.executeQuery()) {
                    if (!result.next()) {
                        throw new SQLException("Poll not found.");
                    }
                    LocalDateTime deadline = result.getObject("deadline", LocalDateTime.class);
                    if (result.getLong("option_count") < 2) {
                        throw new IllegalArgumentException("A poll needs at least two options before activation.");
                    }
                    if (deadline != null && !deadline.isAfter(LocalDateTime.now())) {
                        throw new IllegalArgumentException("A poll with an expired deadline cannot be activated.");
                    }
                }
            }
        }
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE polls SET status = ? WHERE id = ?")) {
            statement.setString(1, status);
            statement.setLong(2, pollId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Poll not found.");
            }
        }
    }

    public void updateDetails(long pollId, String title, String description, String status,
                              boolean showResults, LocalDateTime deadline, List<String> options) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement lock = connection.prepareStatement("SELECT id FROM polls WHERE id = ? FOR UPDATE")) {
                    lock.setLong(1, pollId);
                    try (ResultSet result = lock.executeQuery()) {
                        if (!result.next()) {
                            throw new SQLException("Poll not found.");
                        }
                    }
                }

                if (options != null) {
                    try (PreparedStatement votes = connection.prepareStatement(
                            "SELECT COUNT(*) FROM votes WHERE poll_id = ?")) {
                        votes.setLong(1, pollId);
                        try (ResultSet result = votes.executeQuery()) {
                            result.next();
                            if (result.getLong(1) > 0) {
                                throw new IllegalArgumentException("Voting options cannot be changed after votes are recorded.");
                            }
                        }
                    }
                    try (PreparedStatement deleteOptions = connection.prepareStatement(
                            "DELETE FROM voting_options WHERE poll_id = ?")) {
                        deleteOptions.setLong(1, pollId);
                        deleteOptions.executeUpdate();
                    }
                    insertOptions(connection, pollId, options);
                }

                String sql = "UPDATE polls SET title = ?, description = ?, status = ?, show_results = ?, deadline = ? "
                        + "WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, title);
                    statement.setString(2, description);
                    statement.setString(3, status);
                    statement.setBoolean(4, showResults);
                    statement.setObject(5, deadline);
                    statement.setLong(6, pollId);
                    statement.executeUpdate();
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

    public void delete(long pollId) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM polls WHERE id = ?")) {
            statement.setLong(1, pollId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Poll not found.");
            }
        }
    }

    public long count() throws SQLException {
        return countWhere("");
    }

    public long countActive() throws SQLException {
        String sql = "SELECT COUNT(*) FROM polls WHERE status = 'ACTIVE' AND (deadline IS NULL OR deadline > ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, LocalDateTime.now());
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getLong(1);
            }
        }
    }

    private long countWhere(String suffix) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM polls" + suffix);
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getLong(1);
        }
    }
}
