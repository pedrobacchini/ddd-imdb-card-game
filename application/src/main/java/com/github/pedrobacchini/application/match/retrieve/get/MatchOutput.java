package com.github.pedrobacchini.application.match.retrieve.get;

import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchID;
import com.github.pedrobacchini.domain.match.MatchOptions;

import java.time.Instant;

public record MatchOutput(
    MatchID id,
    int points,
    int fails,
    MatchOptions currentMatchOptions,
    Match.MatchStatus status,
    Instant createdAt
) {

    public static MatchOutput from(Match match) {
        return new MatchOutput(
            match.getId(),
            match.getPoints(),
            match.getFails(),
            match.getCurrentMatchOptions(),
            match.getStatus(),
            match.getCreatedAt()
        );
    }

}
