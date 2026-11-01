package com.votesphere.model;

import java.time.LocalDateTime;

public record VoteRecord(long pollId, String pollTitle, String optionText, String pollStatus,
                         LocalDateTime votedAt, boolean showResults) {
    public long getPollId() {
        return pollId;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public String getOptionText() {
        return optionText;
    }

    public String getPollStatus() {
        return pollStatus;
    }

    public LocalDateTime getVotedAt() {
        return votedAt;
    }

    public boolean isShowResults() {
        return showResults;
    }
}
