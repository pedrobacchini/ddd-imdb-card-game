package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.AggregateRoot;

public class Match extends AggregateRoot<MatchID> {

    private final MatchOptionsGenerationStrategy matchOptionsGenerationStrategy;

    private Match(
        final MatchID aMatchID,
        final MatchOptionsGenerationStrategy aMatchOptionsGenerationStrategy) {
        super(aMatchID);
        this.matchOptionsGenerationStrategy = aMatchOptionsGenerationStrategy;
    }

    public static Match start(
        final MatchID aMatchId,
        final MatchOptionsGenerationStrategy aMatchOptionsGenerationStrategy) {
        return new Match(aMatchId, aMatchOptionsGenerationStrategy);
    }

    public MatchOptionsGenerationStrategy getMatchOptionsGenerationStrategy() {
        return matchOptionsGenerationStrategy;
    }

}
