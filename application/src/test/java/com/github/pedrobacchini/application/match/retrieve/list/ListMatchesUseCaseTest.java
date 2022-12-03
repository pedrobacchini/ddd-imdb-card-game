package com.github.pedrobacchini.application.match.retrieve.list;

import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchGateway;
import com.github.pedrobacchini.domain.match.MatchSearchQuery;
import com.github.pedrobacchini.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.github.pedrobacchini.application.match.DummyUtil.dummyObject;
import static com.github.pedrobacchini.application.match.DummyUtil.dummyObjects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListMatchesUseCaseTest {

    @InjectMocks
    private DefaultListMatchesUseCase defaultListMatchesUseCase;

    @Mock
    private MatchGateway matchGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(matchGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListMatches_thenShouldReturnMatches() {
        final var aQuery = dummyObject(MatchSearchQuery.class);
        final var categories = dummyObjects(Match.class, 10);
        final var expectedPagination = new Pagination<>(aQuery.page(), aQuery.perPage(), categories.size(), categories);
        final var expectedResult = expectedPagination.map(MatchListOutput::from);

        when(matchGateway.findAll(aQuery)).thenReturn(expectedPagination);

        final var actualResult = defaultListMatchesUseCase.execute(aQuery);

        verify(matchGateway, times(1)).findAll(aQuery);

        assertNotNull(actualResult);
        assertEquals(expectedPagination.items().size(), actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(aQuery.page(), actualResult.currentPage());
        assertEquals(aQuery.perPage(), actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    void givenAInvalidQuery_whenHasNoResults_thenShouldReturnEmptyMatches() {
        final var aQuery = dummyObject(MatchSearchQuery.class);
        final var pagination = new Pagination<Match>(aQuery.page(), aQuery.perPage(), 0, List.of());

        when(matchGateway.findAll(aQuery)).thenReturn(pagination);

        final var actualResult = defaultListMatchesUseCase.execute(aQuery);

        verify(matchGateway, times(1)).findAll(aQuery);

        assertNotNull(actualResult);
        assertEquals(0, actualResult.items().size());
        assertEquals(aQuery.page(), actualResult.currentPage());
        assertEquals(aQuery.perPage(), actualResult.perPage());
        assertEquals(0, actualResult.total());
    }

    @Test
    void givenAInvalidQuery_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var aQuery = dummyObject(MatchSearchQuery.class);

        when(matchGateway.findAll(aQuery)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = assertThrows(
            IllegalStateException.class,
            () -> defaultListMatchesUseCase.execute(aQuery));

        verify(matchGateway, times(1)).findAll(aQuery);

        assertEquals(expectedErrorMessage, actualException.getMessage());

    }

}
