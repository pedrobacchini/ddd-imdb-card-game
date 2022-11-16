package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.AggregateRoot;
import com.github.pedrobacchini.domain.validation.ValidationHandler;

import java.time.Instant;

public class Match extends AggregateRoot<MatchID> {

    private static final int FAILS_TO_OVER = 3;
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

    public Match nextPhase(final String playerMove) {
        applyPlayerMovement(playerMove);
        final var matchStatusAfterMovement = analysisMatchAlreadyOverByFails(this.fails);
        switch (matchStatusAfterMovement) {
            case GAME_OVER -> gameOver();
            case PLAYING_GAME -> matchOptionsGenerationStrategy.generateNextMatchOptions()
                .ifPresentOrElse(matchOptions -> this.currentMatchOptions = matchOptions, this::gameOver);
            default -> throw new IllegalStateException("Unexpected value: " + matchStatusAfterMovement);
        }
        return this;
    }

    public void gameOver() {
        this.status = MatchStatus.GAME_OVER;
        this.currentMatchOptions = null;
    }

    private void applyPlayerMovement(final String playerMove) {
        if (currentMatchOptions.isRightOption(playerMove)) points++;
        else fails++;
    }

    private static MatchStatus analysisMatchAlreadyOverByFails(int fails) {
        if (fails >= FAILS_TO_OVER) return MatchStatus.GAME_OVER;
        else return MatchStatus.PLAYING_GAME;
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
