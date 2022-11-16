package com.github.pedrobacchini.application.match.create;

import com.github.pedrobacchini.domain.match.AlphabetMatchOptionsGenerationStrategy;
import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchGateway;
import com.github.pedrobacchini.domain.match.MatchID;
import com.github.pedrobacchini.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateMatchUseCase extends CreateMatchUseCase {

    private final MatchGateway matchGateway;

    public DefaultCreateMatchUseCase(final MatchGateway matchGateway) {
        this.matchGateway = Objects.requireNonNull(matchGateway);
    }

    @Override
    public CreateMatchOutput execute(final CreateMatchCommand aCommand) {
        final var aPlayerId = aCommand.playerId();
        final var aMatchId = aCommand.matchId();

        final var aMatch = Match.start(MatchID.with(aPlayerId, aMatchId), new AlphabetMatchOptionsGenerationStrategy());
        aMatch.validate(new ThrowsValidationHandler());

        this.matchGateway.create(aMatch);
        return CreateMatchOutput.from(aMatch);
    }

}
