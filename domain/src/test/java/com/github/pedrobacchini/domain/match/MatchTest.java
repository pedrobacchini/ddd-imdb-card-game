package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.exception.DomainException;
import com.github.pedrobacchini.domain.movie.Movie;
import com.github.pedrobacchini.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatchTest {

    @Test
    void givenAValidParams_whenCallStarMatchAndValidate_thenInstantiateAMatch() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();
        final var expectedPoints = 0;
        final var expectedFails = 0;
        final var expectedStatus = Match.MatchStatus.PLAYING_GAME;

        final var actualMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);
        actualMatch.validate(new ThrowsValidationHandler());

        assertNotNull(actualMatch);
        assertNotNull(actualMatch.getId());
        assertEquals(expectedPlayerId, actualMatch.getId().getPlayerId());
        assertEquals(expectedMatchId, actualMatch.getId().getMatchId());
        assertEquals(expectedMatchOptionsGenerationStrategy, actualMatch.getMatchOptionsGenerationStrategy());
        assertEquals(expectedPoints, actualMatch.getPoints());
        assertEquals(expectedFails, actualMatch.getFails());
        assertNotNull(actualMatch.getCurrentMatchOptions());
        assertNotNull(actualMatch.getCurrentMatchOptions().firstOption());
        assertNotNull(actualMatch.getCurrentMatchOptions().firstOption().value());
        assertNotNull(actualMatch.getCurrentMatchOptions().firstOption().score());
        assertNotNull(actualMatch.getCurrentMatchOptions().secondOption());
        assertNotNull(actualMatch.getCurrentMatchOptions().secondOption().value());
        assertNotNull(actualMatch.getCurrentMatchOptions().secondOption().score());
        assertEquals(expectedStatus, actualMatch.getStatus());
    }

    @Test
    void givenAnInvalidNullIdentification_whenCallStarMatchAndValidate_thenShouldReceiveError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be null";
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();

        final var actualMatch = Match.start(null, expectedMatchOptionsGenerationStrategy);
        final var actualException = assertThrows(DomainException.class, () -> actualMatch.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNullPlayerId_whenCallStarMatchAndValidate_thenShouldReceiveError() {
        final UUID expectedPlayerId = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'playerId' should not be null";
        final var expectedMatchId = UUID.randomUUID();
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();

        final var actualMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);
        final var actualException = assertThrows(DomainException.class, () -> actualMatch.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNullMatchId_whenCallStarMatchAndValidate_thenShouldReceiveError() {
        final var expectedPlayerId = UUID.randomUUID();
        final UUID expectedMatchId = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'matchId' should not be null";
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();

        final var actualMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);
        final var actualException = assertThrows(DomainException.class, () -> actualMatch.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNullStrategy_whenCallStarMatchAndValidate_thenShouldReceiveError() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final MatchOptionsGenerationStrategy expectedMatchOptionsGenerationStrategy = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'strategy' should not be null";

        final var actualMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);
        final var actualException = assertThrows(DomainException.class, () -> actualMatch.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertNull(actualMatch.getCurrentMatchOptions());
    }

    @Test
    void givenAnPlayerMovementRightOption_whenCallNextPlayerMovement_thenShouldScoreCorrectly() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();
        final var expectedPoints = 1;
        final var expectedFails = 0;
        final var expectedStatus = Match.MatchStatus.PLAYING_GAME;

        final var actualMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);

        assertDoesNotThrow(() -> actualMatch.validate(new ThrowsValidationHandler()));
        final var previousMatchOptions = actualMatch.getCurrentMatchOptions();

        actualMatch.nextPhase(previousMatchOptions.rightOption().value());
        assertDoesNotThrow(() -> actualMatch.validate(new ThrowsValidationHandler()));

        assertEquals(expectedPlayerId, actualMatch.getId().getPlayerId());
        assertEquals(expectedMatchId, actualMatch.getId().getMatchId());
        assertEquals(expectedStatus, actualMatch.getStatus());
        assertNotEquals(previousMatchOptions, actualMatch.getCurrentMatchOptions());
        assertEquals(expectedPoints, actualMatch.getPoints());
        assertEquals(expectedFails, actualMatch.getFails());
    }

    @Test
    void givenAnPlayerMovementWrongOption_whenCallNextPlayerMovement_thenShouldScoreCorrectly() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();
        final var expectedPoints = 0;
        final var expectedFails = 1;
        final var expectedStatus = Match.MatchStatus.PLAYING_GAME;

        final var actualMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);

        assertDoesNotThrow(() -> actualMatch.validate(new ThrowsValidationHandler()));
        final var previousMatchOptions = actualMatch.getCurrentMatchOptions();

        actualMatch.nextPhase(previousMatchOptions.wrongOption().value());
        assertDoesNotThrow(() -> actualMatch.validate(new ThrowsValidationHandler()));

        assertEquals(expectedPlayerId, actualMatch.getId().getPlayerId());
        assertEquals(expectedMatchId, actualMatch.getId().getMatchId());
        assertEquals(expectedStatus, actualMatch.getStatus());
        assertNotEquals(previousMatchOptions, actualMatch.getCurrentMatchOptions());
        assertEquals(expectedPoints, actualMatch.getPoints());
        assertEquals(expectedFails, actualMatch.getFails());
    }

    @Test
    void givenAnPlayerMovementManyWrongOptions_whenCallNextPlayerMovement_thenShouldOverGame() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();
        final var expectedPoints = 0;
        final var expectedFails = 3;
        final var expectedStatus = Match.MatchStatus.GAME_OVER;

        final var actualMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);
        assertDoesNotThrow(() -> actualMatch.validate(new ThrowsValidationHandler()));

        while (actualMatch.getStatus() == Match.MatchStatus.PLAYING_GAME) {
            final var wrongOption = actualMatch.getCurrentMatchOptions().wrongOption();
            actualMatch.nextPhase(wrongOption.value());
        }
        assertDoesNotThrow(() -> actualMatch.validate(new ThrowsValidationHandler()));

        assertEquals(expectedPlayerId, actualMatch.getId().getPlayerId());
        assertEquals(expectedMatchId, actualMatch.getId().getMatchId());
        assertEquals(expectedStatus, actualMatch.getStatus());
        assertEquals(expectedPoints, actualMatch.getPoints());
        assertEquals(expectedFails, actualMatch.getFails());
    }

    @Test
    void givenAnPlayerMovementManyRightOptions_whenCallNextPlayerMovement_thenShouldOverGame() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final var expectedMatchOptionsGenerationStrategy = new TestMatchOptionsGenerationStrategy();
        final var expectedPoints = 3;
        final var expectedFails = 0;
        final var expectedStatus = Match.MatchStatus.GAME_OVER;

        final var actualMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);
        assertDoesNotThrow(() -> actualMatch.validate(new ThrowsValidationHandler()));

        while (actualMatch.getStatus() == Match.MatchStatus.PLAYING_GAME) {
            final var rightOption = actualMatch.getCurrentMatchOptions().rightOption();
            actualMatch.nextPhase(rightOption.value());
        }
        assertDoesNotThrow(() -> actualMatch.validate(new ThrowsValidationHandler()));

        assertEquals(expectedPlayerId, actualMatch.getId().getPlayerId());
        assertEquals(expectedMatchId, actualMatch.getId().getMatchId());
        assertEquals(expectedStatus, actualMatch.getStatus());
        assertEquals(expectedPoints, actualMatch.getPoints());
        assertEquals(expectedFails, actualMatch.getFails());
    }

    static class TestMatchOptionsGenerationStrategy extends MatchOptionsGenerationStrategy {

        public TestMatchOptionsGenerationStrategy() {
            super(Set.of(
                new Movie("A", 1F),
                new Movie("B", 2F),
                new Movie("C", 3F)));
        }

    }

}