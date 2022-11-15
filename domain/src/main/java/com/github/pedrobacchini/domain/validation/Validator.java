package com.github.pedrobacchini.domain.validation;

public abstract class Validator {

    private final ValidationHandler validationHandler;

    protected Validator(final ValidationHandler aHandler) {
        this.validationHandler = aHandler;
    }

    public abstract void validate();

    protected ValidationHandler validationHandler() {
        return validationHandler;
    }

}
