package com.github.pedrobacchini.domain.match;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MatchTest {

    @Test
    void givenAValidParams_whenCallNewMatch_thenInstantiateAMatch() {
        final var expectedMatchId = MatchID.unique();
        final var matchOptionsGenerationStrategy = new MatchOptionsGenerationStrategy();

        final var actualMatch = Match.start(expectedMatchId, matchOptionsGenerationStrategy);

        assertNotNull(actualMatch);
        assertNotNull(actualMatch.getId());
        assertNotNull(actualMatch.getId().getPlayerId());
        assertNotNull(actualMatch.getId().getMatchId());
        assertNotNull(actualMatch.getMatchOptionsGenerationStrategy());
    }

}