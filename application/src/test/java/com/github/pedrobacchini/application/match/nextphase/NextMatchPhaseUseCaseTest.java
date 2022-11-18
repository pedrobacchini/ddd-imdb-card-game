package com.github.pedrobacchini.application.match.nextphase;

import com.github.pedrobacchini.domain.exception.DomainException;
import com.github.pedrobacchini.domain.match.AlphabetMatchOptionsGenerationStrategy;
import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchGateway;
import com.github.pedrobacchini.domain.match.MatchID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;
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
class NextMatchPhaseUseCaseTest {

    @InjectMocks
    private DefaultNextMatchPhaseUseCase defaultNextMatchPhaseUseCase;

    @Mock
    private MatchGateway matchGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(matchGateway);
    }

    @Test
    void givenAValidCommand_whenCallsNextMatchPhase_shouldReturnMatchId() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();
        final var expectedPoints = 1;
        final var expectedFails = 0;
        final var expectedStatus = Match.MatchStatus.PLAYING_GAME;

        final var aMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);
        final var playerMove = aMatch.getCurrentMatchOptions().rightOption().value();

        final var aCommand = NextMatchPhaseCommand.with(
            aMatch.getId().getPlayerId(),
            aMatch.getId().getMatchId(),
            playerMove);

        when(matchGateway.findById(aMatch.getId())).thenReturn(Optional.of(aMatch));

        when(matchGateway.update(aMatch)).thenAnswer(returnsFirstArg());

        final var actualOutput = defaultNextMatchPhaseUseCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(matchGateway, times(1)).findById(aMatch.getId());
        verify(matchGateway, times(1)).update(argThat(
            aUpdatedMatch ->
                Objects.nonNull(aUpdatedMatch.getId()) &&
                    Objects.equals(expectedPlayerId, aUpdatedMatch.getId().getPlayerId()) &&
                    Objects.equals(expectedMatchId, aUpdatedMatch.getId().getMatchId()) &&
                    Objects.equals(expectedMatchOptionsGenerationStrategy, aUpdatedMatch.getMatchOptionsGenerationStrategy()) &&
                    Objects.equals(expectedPoints, aUpdatedMatch.getPoints()) &&
                    Objects.equals(expectedFails, aUpdatedMatch.getFails()) &&
                    Objects.equals(expectedStatus, aUpdatedMatch.getStatus())));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final var expectedMatchOptionsGenerationStrategy = new AlphabetMatchOptionsGenerationStrategy();
        final var expectedPoints = 1;
        final var expectedFails = 0;
        final var expectedStatus = Match.MatchStatus.PLAYING_GAME;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aMatch = Match.start(MatchID.from(expectedPlayerId, expectedMatchId), expectedMatchOptionsGenerationStrategy);
        final var playerMove = aMatch.getCurrentMatchOptions().rightOption().value();

        final var aCommand = NextMatchPhaseCommand.with(
            aMatch.getId().getPlayerId(),
            aMatch.getId().getMatchId(),
            playerMove);

        when(matchGateway.findById(aMatch.getId())).thenReturn(Optional.of(aMatch));

        when(matchGateway.update(aMatch)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = defaultNextMatchPhaseUseCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        notification.firstError().ifPresent(error -> assertEquals(expectedErrorMessage, error.message()));

        verify(matchGateway, times(1)).findById(aMatch.getId());
        verify(matchGateway, times(1))
            .update(argThat(aUpdatedMatch ->
                Objects.nonNull(aUpdatedMatch.getId()) &&
                    Objects.equals(expectedPlayerId, aUpdatedMatch.getId().getPlayerId()) &&
                    Objects.equals(expectedMatchId, aUpdatedMatch.getId().getMatchId()) &&
                    Objects.equals(expectedMatchOptionsGenerationStrategy, aUpdatedMatch.getMatchOptionsGenerationStrategy()) &&
                    Objects.equals(expectedPoints, aUpdatedMatch.getPoints()) &&
                    Objects.equals(expectedFails, aUpdatedMatch.getFails()) &&
                    Objects.equals(expectedStatus, aUpdatedMatch.getStatus())));
    }

    @Test
    void givenACommandWithInvalidId_whenCallsNextMatchPhase_shouldReturnNotFoundException() {
        final var expectedPlayerId = UUID.randomUUID();
        final var expectedMatchId = UUID.randomUUID();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Match with ID [%s, %s] was not found".formatted(expectedPlayerId, expectedMatchId);

        final var aCommand = NextMatchPhaseCommand.with(
            expectedPlayerId,
            expectedMatchId,
            "A");

        when(matchGateway.findById(MatchID.from(expectedPlayerId, expectedMatchId))).thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () -> defaultNextMatchPhaseUseCase.execute(aCommand));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(matchGateway, times(1)).findById(MatchID.from(expectedPlayerId, expectedMatchId));
        verify(matchGateway, never()).update(any());
    }

}
