package com.github.pedrobacchini.application.match.create;

import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchID;

public record CreateMatchOutput(
    MatchID id
) {

    public static CreateMatchOutput from(final Match match) {
        return new CreateMatchOutput(match.getId());
    }
}
