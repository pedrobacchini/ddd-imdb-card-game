package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.ValueObject;

import java.util.Objects;

public class MatchOptions extends ValueObject {

    private final MatchOption firstOption;
    private final MatchOption secondOption;

    private MatchOptions(final MatchOption aFirstOption, final MatchOption aSecondOption) {
        this.firstOption = aFirstOption;
        this.secondOption = aSecondOption;
    }

    public static MatchOptions with(final MatchOption aFirstOption, final MatchOption aSecondOption) {
        return new MatchOptions(aFirstOption, aSecondOption);
    }

    public boolean isRightOption(final String option) {
        if (option.equals(firstOption.value())) {
            return isRightOption(firstOption);
        } else if (option.equals(secondOption.value())) {
            return isRightOption(secondOption);
        } else {
            throw new IllegalArgumentException("invalid option");
        }
    }

    public boolean isRightOption(final MatchOption matchOption) {
        return matchOption.equals(rightOption());
    }

    public MatchOption rightOption() {
        if (firstOption.score() > secondOption.score()) return firstOption;
        else return secondOption;
    }

    public MatchOption wrongOption() {
        return firstOption.equals(rightOption()) ? secondOption : firstOption;
    }

    public MatchOption firstOption() {
        return firstOption;
    }

    public MatchOption secondOption() {
        return secondOption;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MatchOptions that = (MatchOptions) o;
        return (Objects.equals(firstOption.value(), that.firstOption.value()) && Objects.equals(secondOption.value(), that.secondOption.value())) ||
            (Objects.equals(firstOption.value(), that.secondOption.value()) && Objects.equals(secondOption.value(), that.firstOption.value()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstOption.value(), secondOption.value()) + Objects.hash(secondOption.value(), firstOption.value());
    }

}
