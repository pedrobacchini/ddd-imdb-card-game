package com.github.pedrobacchini.application.match.create;

import com.github.pedrobacchini.domain.match.AlphabetMatchOptionsGenerationStrategy;
import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchGateway;
import com.github.pedrobacchini.domain.match.MatchID;
import com.github.pedrobacchini.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateMatchUseCase extends CreateMatchUseCase {

    private final MatchGateway matchGateway;

    public DefaultCreateMatchUseCase(final MatchGateway matchGateway) {
        this.matchGateway = Objects.requireNonNull(matchGateway);
    }

    @Override
    public Either<Notification, CreateMatchOutput> execute(final CreateMatchCommand aCommand) {
        final var aPlayerId = aCommand.playerId();
        final var aMatchId = aCommand.matchId();

        final var notification = Notification.create();
        final var aMatch = Match.start(MatchID.from(aPlayerId, aMatchId), new AlphabetMatchOptionsGenerationStrategy());
        aMatch.validate(notification);

        return notification.hasError() ? Left(notification) : create(aMatch);
    }

    private Either<Notification, CreateMatchOutput> create(final Match aMatch) {
        return Try(() -> this.matchGateway.create(aMatch))
            .toEither()
            .bimap(Notification::create, CreateMatchOutput::from);
    }

}
