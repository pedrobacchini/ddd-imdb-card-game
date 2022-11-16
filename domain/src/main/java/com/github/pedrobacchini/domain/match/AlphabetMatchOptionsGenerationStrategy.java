package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.movie.Movie;

import java.util.Set;
import java.util.stream.Collectors;

public class AlphabetMatchOptionsGenerationStrategy extends MatchOptionsGenerationStrategy {

    private static final Set<Character> typesToChoose = Set.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');

    public AlphabetMatchOptionsGenerationStrategy() {
        super(typesToChoose.stream().map(type -> new Movie(type.toString(), (float) type)).collect(Collectors.toSet()));
    }

}
