package com.github.pedrobacchini.domain.match;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchOptionsTest {

    @Test
    void givenAValidMatchOptions_whenCallRightOption_thenFirstOptionAsRightOption() {
        final var firstOption = MatchOption.with("option1", 10F);
        final var secondOption = MatchOption.with("option2", 1F);
        final var actualMatchOptions = MatchOptions.with(firstOption, secondOption);
        final var rightOption = actualMatchOptions.isRightOption(firstOption.value());
        assertTrue(rightOption);
    }

    @Test
    void givenAValidMatchOptions_whenCallRightOption_thenSecondOptionAsRightOption() {
        final var firstOption = MatchOption.with("option1", 1F);
        final var secondOption = MatchOption.with("option2", 10F);
        final var actualMatchOptions = MatchOptions.with(firstOption, secondOption);
        final var rightOption = actualMatchOptions.isRightOption(secondOption.value());
        assertTrue(rightOption);
    }

    @Test
    void givenAInvalidMatchOptions_whenCallRightOption_thenShouldReceiveError() {
        final var firstOption = MatchOption.with("option1", 1F);
        final var secondOption = MatchOption.with("option2", 10F);
        final var actualMatchOptions = MatchOptions.with(firstOption, secondOption);
        assertThrows(IllegalArgumentException.class, () -> actualMatchOptions.isRightOption("invaid option"));
    }

    @Test
    void givenAValidMatchOptionsEquals_whenCallEquals_thenShouldReturnEquals() {
        final var option1 = MatchOption.with("option1", 1F);
        final var option2 = MatchOption.with("option2", 1F);
        final var actualMatchOptions1 = MatchOptions.with(option1, option2);
        final var actualMatchOptions2 = MatchOptions.with(option1, option2);
        assertEquals(actualMatchOptions1, actualMatchOptions2);
        final var actualMatchOptions3 = MatchOptions.with(option2, option1);
        assertEquals(actualMatchOptions1, actualMatchOptions3);

        final var actualMatchOptions = new HashSet<>(Set.of(actualMatchOptions1));
        actualMatchOptions.remove(MatchOptions.with(option2, option1));
        assertEquals(0, actualMatchOptions.size());
    }

}