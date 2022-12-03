package com.github.pedrobacchini.application.match.retrieve.get;

import com.github.pedrobacchini.domain.exception.DomainException;
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

import java.util.Optional;

import static com.github.pedrobacchini.application.match.DummyUtil.dummyObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetMatchByIdUseCaseTest {

    @InjectMocks
    private DefaultGetMatchByIdUseCase defaultGetMatchByIdUseCase;

    @Mock
    private MatchGateway matchGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(matchGateway);
    }

    @Test
    void givenAValidId_whenCallsGetMatch_shouldReturnMatch() {
        final var expectedMatch = dummyObject(Match.class);
        final var expectedID = expectedMatch.getId();

        when(matchGateway.findById(expectedID)).thenReturn(Optional.of(expectedMatch));

        final var actualMatch = defaultGetMatchByIdUseCase.execute(expectedID);

        verify(matchGateway, times(1)).findById(expectedID);

        assertEquals(expectedMatch.getId(), actualMatch.id());
        assertEquals(expectedMatch.getPoints(), actualMatch.points());
        assertEquals(expectedMatch.getFails(), actualMatch.fails());
        assertEquals(expectedMatch.getCurrentMatchOptions(), actualMatch.currentMatchOptions());
        assertEquals(expectedMatch.getCreatedAt(), actualMatch.createdAt());
        assertEquals(expectedMatch.getStatus(), actualMatch.status());
    }

    @Test
    void givenAInvalidId_whenCallsGetMatch_shouldReturnNotFound() {
        final var expectedMatchID = dummyObject(MatchID.class);
        final var expectedErrorMessage = "Match with ID [%s, %s] was not found".formatted(expectedMatchID.getPlayerId(), expectedMatchID.getMatchId());

        when(matchGateway.findById(expectedMatchID)).thenReturn(Optional.empty());

        final var actualException = assertThrows(
            DomainException.class,
            () -> defaultGetMatchByIdUseCase.execute(expectedMatchID));

        verify(matchGateway, times(1)).findById(expectedMatchID);

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAInvalidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedMatchID = dummyObject(MatchID.class);

        doThrow(new IllegalStateException(expectedErrorMessage))
            .when(matchGateway).findById(expectedMatchID);

        final var actualException = assertThrows(
            IllegalStateException.class,
            () -> defaultGetMatchByIdUseCase.execute(expectedMatchID));

        verify(matchGateway, times(1)).findById(expectedMatchID);

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
