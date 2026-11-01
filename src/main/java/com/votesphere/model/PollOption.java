package com.votesphere.model;

public record PollOption(long id, long pollId, String text, String description, long votes) {
    public long getId() {
        return id;
    }

    public long getPollId() {
        return pollId;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    public long getVotes() {
        return votes;
    }
}
