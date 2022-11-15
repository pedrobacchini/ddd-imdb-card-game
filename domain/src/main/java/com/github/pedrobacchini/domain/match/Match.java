package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.AggregateRoot;
import com.github.pedrobacchini.domain.validation.ValidationHandler;

import java.time.Instant;

public class Match extends AggregateRoot<MatchID> {

    private final MatchOptionsGenerationStrategy matchOptionsGenerationStrategy;

    private int points = 0;
    private int fails = 0;
    private MatchOptions currentMatchOptions;
    private MatchStatus status = MatchStatus.PLAYING_GAME;

    private final Instant createdAt = Instant.now();

    public enum MatchStatus {
        PLAYING_GAME,
        GAME_OVER
    }

    private Match(
        final MatchID aMatchID,
        final MatchOptionsGenerationStrategy aMatchOptionsGenerationStrategy) {
        super(aMatchID);
        this.matchOptionsGenerationStrategy = aMatchOptionsGenerationStrategy;
        if (matchOptionsGenerationStrategy != null) this.currentMatchOptions = matchOptionsGenerationStrategy.generateInitialMatchOptions();
    }

    public static Match start(
        final MatchID aMatchId,
        final MatchOptionsGenerationStrategy aMatchOptionsGenerationStrategy) {
        return new Match(aMatchId, aMatchOptionsGenerationStrategy);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new MatchValidator(this, aHandler).validate();
    }

    public MatchOptionsGenerationStrategy getMatchOptionsGenerationStrategy() {
        return matchOptionsGenerationStrategy;
    }

    public int getPoints() {
        return points;
    }

    public int getFails() {
        return fails;
    }

    public MatchOptions getCurrentMatchOptions() {
        return currentMatchOptions;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
