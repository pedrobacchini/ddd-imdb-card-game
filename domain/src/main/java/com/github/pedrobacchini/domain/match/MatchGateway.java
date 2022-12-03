package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.pagination.Pagination;

import java.util.Optional;

public interface MatchGateway {

    Match create(Match match);

    Match update(Match match);

    Optional<Match> findById(MatchID matchID);

    Pagination<Match> findAll(MatchSearchQuery aQuery);

}
