package com.github.pedrobacchini.application.match.create;

import com.github.pedrobacchini.domain.exception.DomainException;
import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMatchUseCaseTest {

    @InjectMocks
    private DefaultCreateMatchUseCase defaultCreateMatchUseCase;

    @Mock
    private MatchGateway matchGateway;

    @Test
    void givenAValidCommand_whenCallsCreateMatch_shouldReturnMatchId() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
//        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();
        final var expectedPoints = 0;
        final var expectedFails = 0;
        final var expectedStatus = Match.MatchStatus.PLAYING_GAME;

        final var aCommand = CreateMatchCommand.with(expectedPlayerId, expectedMatchId);

        when(matchGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOuput = defaultCreateMatchUseCase.execute(aCommand);

        assertNotNull(actualOuput);
        assertNotNull(actualOuput.id());

        verify(matchGateway, times(1))
            .create(argThat(aMatch ->
                Objects.nonNull(aMatch.getId()) &&
                    Objects.equals(expectedPlayerId, aMatch.getId().getPlayerId()) &&
                    Objects.equals(expectedMatchId, aMatch.getId().getMatchId()) &&
//                    Objects.equals(expectedMatchOptionsGenerationStrategy, aMatch.getMatchOptionsGenerationStrategy()) &&
                    Objects.equals(expectedPoints, aMatch.getPoints()) &&
                    Objects.equals(expectedFails, aMatch.getFails()) &&
                    Objects.equals(expectedStatus, aMatch.getStatus())));
    }

    @Test
    void givenAInvalidPlayerId_whenCallsCreateMatch_shouldReturnDomainException() {
        final UUID expectedPlayerId = null;
        final var expectedMatchId = UUID.randomUUID();
        final var expectedErrorMessage = "'playerId' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateMatchCommand.with(expectedPlayerId, expectedMatchId);

        final var actualException = assertThrows(DomainException.class, () -> defaultCreateMatchUseCase.execute(aCommand));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(matchGateway, never()).create(any());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
//        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();
        final var expectedPoints = 0;
        final var expectedFails = 0;
        final var expectedStatus = Match.MatchStatus.PLAYING_GAME;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateMatchCommand.with(expectedPlayerId, expectedMatchId);

        when(matchGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = assertThrows(IllegalStateException.class, () -> defaultCreateMatchUseCase.execute(aCommand));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(matchGateway, times(1))
            .create(argThat(aMatch ->
                Objects.nonNull(aMatch.getId()) &&
                    Objects.equals(expectedPlayerId, aMatch.getId().getPlayerId()) &&
                    Objects.equals(expectedMatchId, aMatch.getId().getMatchId()) &&
//                    Objects.equals(expectedMatchOptionsGenerationStrategy, aMatch.getMatchOptionsGenerationStrategy()) &&
                    Objects.equals(expectedPoints, aMatch.getPoints()) &&
                    Objects.equals(expectedFails, aMatch.getFails()) &&
                    Objects.equals(expectedStatus, aMatch.getStatus())));
    }

}
