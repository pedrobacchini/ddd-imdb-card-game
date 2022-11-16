package com.github.pedrobacchini.application.match.create;

import com.github.pedrobacchini.application.UseCase;
import com.github.pedrobacchini.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateMatchUseCase extends UseCase<CreateMatchCommand, Either<Notification, CreateMatchOutput>> {

}
