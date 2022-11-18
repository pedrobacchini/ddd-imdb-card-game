package com.github.pedrobacchini.application.match.nextphase;

import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchID;

public record NextMatchPhaseOutput(
    MatchID id
) {

    public static NextMatchPhaseOutput from(final Match match) {
        return new NextMatchPhaseOutput(match.getId());
    }

}
