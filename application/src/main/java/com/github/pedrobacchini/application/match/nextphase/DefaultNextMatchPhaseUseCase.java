package com.github.pedrobacchini.application.match.nextphase;

import com.github.pedrobacchini.domain.exception.DomainException;
import com.github.pedrobacchini.domain.match.Match;
import com.github.pedrobacchini.domain.match.MatchGateway;
import com.github.pedrobacchini.domain.match.MatchID;
import com.github.pedrobacchini.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import com.github.pedrobacchini.domain.validation.Error;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultNextMatchPhaseUseCase extends NextMatchPhaseUseCase {

    private final MatchGateway matchGateway;

    public DefaultNextMatchPhaseUseCase(final MatchGateway matchGateway) {
        this.matchGateway = Objects.requireNonNull(matchGateway);
    }

    @Override
    public Either<Notification, NextMatchPhaseOutput> execute(final NextMatchPhaseCommand aCommand) {
        final var anId = MatchID.from(aCommand.playerId(), aCommand.matchId());
        final var aPlayerMove = aCommand.playerMove();

        final var aMatch = this.matchGateway.findById(anId)
            .orElseThrow(notFound(anId));
        final var notification = Notification.create();
        aMatch.nextPhase(aPlayerMove).validate(notification);

        return notification.hasError() ? Left(notification) : update(aMatch);
    }

    private Supplier<DomainException> notFound(final MatchID anId) {
        return () -> DomainException.with(new Error("Match with ID [%s, %s] was not found".formatted(anId.getPlayerId(), anId.getMatchId())));
    }

    private Either<Notification, NextMatchPhaseOutput> update(final Match aMatch) {
        return Try(() -> this.matchGateway.update(aMatch))
            .toEither()
            .bimap(Notification::create, NextMatchPhaseOutput::from);
    }
}
