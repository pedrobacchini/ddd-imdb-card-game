package com.github.pedrobacchini.domain.match;

import java.util.Optional;

public interface MatchGateway {

    Match create(Match match);

    Optional<Match> findById(MatchID matchID);
}
