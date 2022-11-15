package com.github.pedrobacchini.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anError);

    ValidationHandler validate(Validation aValidation);
    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && getErrors().isEmpty();
    }

    interface Validation {

        void validate();

    }

}
