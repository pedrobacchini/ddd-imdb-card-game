package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.validation.Error;
import com.github.pedrobacchini.domain.validation.ValidationHandler;
import com.github.pedrobacchini.domain.validation.Validator;

public class MatchValidator extends Validator {

    private final Match match;

    public MatchValidator(
        final Match aMatch,
        final ValidationHandler aHandler) {
        super(aHandler);
        this.match = aMatch;
    }

    @Override
    public void validate() {
        checkMatchIdentificationConstraints();
        checkMatchOptionsGenerationStrategyConstraints();
    }

    private void checkMatchIdentificationConstraints() {
        if (this.match.getId() == null) this.validationHandler().append(new Error("'id' should not be null"));
        if (this.match.getId().getPlayerId() == null) this.validationHandler().append(new Error("'playerId' should not be null"));
        if (this.match.getId().getMatchId() == null) this.validationHandler().append(new Error("'matchId' should not be null"));
    }

    private void checkMatchOptionsGenerationStrategyConstraints() {
        if (this.match.getMatchOptionsGenerationStrategy() == null) this.validationHandler().append(new Error("'strategy' should not be null"));
    }

}
