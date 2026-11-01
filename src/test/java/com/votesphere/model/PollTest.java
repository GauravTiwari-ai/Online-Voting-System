package com.votesphere.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PollTest {
    @Test
    void onlyActivePollsBeforeTheirDeadlineAcceptVotes() {
        LocalDateTime now = LocalDateTime.now();
        Poll open = new Poll(1, "Poll", "Description", "ACTIVE", true, now.plusMinutes(5),
                now, 0, 2, List.of(), false);
        Poll closed = new Poll(2, "Poll", "Description", "CLOSED", true, null,
                now, 0, 2, List.of(), false);
        Poll expired = new Poll(3, "Poll", "Description", "ACTIVE", true, now.minusMinutes(1),
                now, 0, 2, List.of(), false);

        assertTrue(open.isActive());
        assertFalse(closed.isActive());
        assertFalse(expired.isActive());
    }
}
