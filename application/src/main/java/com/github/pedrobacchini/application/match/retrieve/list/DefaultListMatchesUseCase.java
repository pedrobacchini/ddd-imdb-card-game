package com.github.pedrobacchini.application.match.retrieve.list;

import com.github.pedrobacchini.domain.match.MatchGateway;
import com.github.pedrobacchini.domain.match.MatchSearchQuery;
import com.github.pedrobacchini.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListMatchesUseCase extends ListMatchesUseCase {

    private final MatchGateway matchGateway;

    public DefaultListMatchesUseCase(final MatchGateway matchGateway) {
        this.matchGateway = Objects.requireNonNull(matchGateway);
    }

    @Override
    public Pagination<MatchListOutput> execute(final MatchSearchQuery aQuery) {
        return matchGateway.findAll(aQuery)
            .map(MatchListOutput::from);
    }

}
