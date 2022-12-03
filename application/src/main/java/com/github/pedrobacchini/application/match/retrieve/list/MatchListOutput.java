package com.github.pedrobacchini.application.match.retrieve.list;

import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchID;
import com.github.pedrobacchini.domain.match.MatchOptions;

import java.time.Instant;

public record MatchListOutput(
    MatchID id,
    int points,
    int fails,
    MatchOptions currentMatchOptions,
    Match.MatchStatus status,
    Instant createdAt
) {
    public static MatchListOutput from(final Match aMatch) {
        return new MatchListOutput(
            aMatch.getId(),
            aMatch.getPoints(),
            aMatch.getFails(),
            aMatch.getCurrentMatchOptions(),
            aMatch.getStatus(),
            aMatch.getCreatedAt()
        );
    }

}
