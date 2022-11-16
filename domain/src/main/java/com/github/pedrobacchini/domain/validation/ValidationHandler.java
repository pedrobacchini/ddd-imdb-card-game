package com.github.pedrobacchini.domain.validation;

import java.util.List;
import java.util.Optional;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anError);

    ValidationHandler validate(Validation aValidation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Optional<Error> firstError() {
        if (hasError()) return Optional.of(getErrors().get(0));
        return Optional.empty();
    }

    interface Validation {

        void validate();

    }

}
