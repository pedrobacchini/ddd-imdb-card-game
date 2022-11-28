package com.github.pedrobacchini.application.match.retrieve;

import com.github.pedrobacchini.domain.exception.DomainException;
import com.github.pedrobacchini.domain.match.MatchGateway;
import com.github.pedrobacchini.domain.match.MatchID;
import com.github.pedrobacchini.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetMatchByIdUseCase extends GetMatchByIdUseCase {

    private final MatchGateway matchGateway;

    public DefaultGetMatchByIdUseCase(final MatchGateway matchGateway) {
        this.matchGateway = Objects.requireNonNull(matchGateway);
    }

    @Override
    public MatchOutput execute(final MatchID anMatchId) {
        return matchGateway.findById(anMatchId)
            .map(MatchOutput::from)
            .orElseThrow(notFound(anMatchId));
    }

    private Supplier<DomainException> notFound(final MatchID anId) {
        return () -> DomainException.with(new Error("Match with ID [%s, %s] was not found".formatted(anId.getPlayerId(), anId.getMatchId())));
    }

}
