package com.votesphere.model;

import java.time.LocalDateTime;
import java.util.List;

public record Poll(long id, String title, String description, String status, boolean showResults,
                   LocalDateTime deadline, LocalDateTime createdAt, long totalVotes, long optionCount,
                   List<PollOption> options, boolean voted) {
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public boolean isShowResults() {
        return showResults;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public long getOptionCount() {
        return optionCount;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public boolean isVoted() {
        return voted;
    }

    public boolean isActive() {
        return "ACTIVE".equals(status) && (deadline == null || deadline.isAfter(LocalDateTime.now()));
    }

    public boolean active() {
        return isActive();
    }
}
