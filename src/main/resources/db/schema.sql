CREATE DATABASE IF NOT EXISTS votesphere
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE votesphere;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(120) NOT NULL,
    username VARCHAR(40) NOT NULL UNIQUE,
    email VARCHAR(254) NOT NULL UNIQUE,
    password_hash VARCHAR(60) NOT NULL,
    role ENUM('VOTER', 'ADMIN') NOT NULL DEFAULT 'VOTER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS polls (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(180) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('UPCOMING', 'ACTIVE', 'CLOSED') NOT NULL DEFAULT 'UPCOMING',
    show_results BOOLEAN NOT NULL DEFAULT TRUE,
    deadline DATETIME NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_polls_creator FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS voting_options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    poll_id BIGINT NOT NULL,
    option_text VARCHAR(180) NOT NULL,
    description VARCHAR(500) NOT NULL DEFAULT '',
    CONSTRAINT fk_options_poll FOREIGN KEY (poll_id) REFERENCES polls(id) ON DELETE CASCADE,
    CONSTRAINT uq_poll_option UNIQUE (poll_id, option_text)
);

CREATE TABLE IF NOT EXISTS votes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    poll_id BIGINT NOT NULL,
    option_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    voted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_votes_poll FOREIGN KEY (poll_id) REFERENCES polls(id) ON DELETE CASCADE,
    CONSTRAINT fk_votes_option FOREIGN KEY (option_id) REFERENCES voting_options(id),
    CONSTRAINT fk_votes_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uq_one_vote_per_user UNIQUE (poll_id, user_id)
);

CREATE INDEX idx_polls_status_deadline ON polls(status, deadline);
CREATE INDEX idx_votes_poll ON votes(poll_id);
CREATE INDEX idx_votes_user ON votes(user_id);
