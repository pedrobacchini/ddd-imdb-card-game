package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.ValueObject;

public class MatchOption extends ValueObject {

    private final String value;
    private final Float score;

    private MatchOption(final String aValue, final Float aScore) {
        this.value = aValue;
        this.score = aScore;
    }

    public static MatchOption with(final String aValue, final Float aScore) {
        return new MatchOption(aValue, aScore);
    }

    public String value() {
        return value;
    }

    public Float score() {
        return score;
    }

}
