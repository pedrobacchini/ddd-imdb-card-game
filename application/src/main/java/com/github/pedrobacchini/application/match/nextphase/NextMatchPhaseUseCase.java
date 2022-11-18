package com.github.pedrobacchini.application.match.nextphase;

import com.github.pedrobacchini.application.UseCase;
import com.github.pedrobacchini.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class NextMatchPhaseUseCase extends UseCase<NextMatchPhaseCommand, Either<Notification, NextMatchPhaseOutput>> {

}
