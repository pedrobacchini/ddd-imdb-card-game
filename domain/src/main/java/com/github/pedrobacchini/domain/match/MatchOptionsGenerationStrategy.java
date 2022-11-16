package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.movie.Movie;
import com.google.common.collect.Sets;
import org.apache.commons.lang.math.RandomUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MatchOptionsGenerationStrategy {

    private static final int NUMBER_OF_CHOICES = 2;
    private final Set<MatchOptions> matchOptionsAlreadyGenerate = new HashSet<>();
    private final Set<Movie> moviesToChoose;

    public MatchOptionsGenerationStrategy(final Set<Movie> moviesToChoose) {
        this.moviesToChoose = moviesToChoose;
    }

    public MatchOptions generateInitialMatchOptions() {
        Set<MatchOptions> matchOptionsCombinations = generateMatchOptionsCombinations();
        final MatchOptions matchOptions = getRandomMatchOptions(matchOptionsCombinations);
        matchOptionsAlreadyGenerate.add(matchOptions);
        return matchOptions;
    }

    public Optional<MatchOptions> generateNextMatchOptions() {
        Set<MatchOptions> matchOptionsCombinations = generateMatchOptionsCombinations();
        matchOptionsCombinations.removeAll(matchOptionsAlreadyGenerate);
        if (matchOptionsCombinations.isEmpty()) return Optional.empty();
        final MatchOptions matchOptions = getRandomMatchOptions(matchOptionsCombinations);
        matchOptionsAlreadyGenerate.add(matchOptions);
        return Optional.of(matchOptions);
    }

    private MatchOptions getRandomMatchOptions(final Set<MatchOptions> matchOptionsCombinations) {
        return matchOptionsCombinations.stream()
            .toList()
            .get(ThreadLocalRandom.current().nextInt(0, matchOptionsCombinations.size()));
    }

    private Set<MatchOptions> generateMatchOptionsCombinations() {
        return Sets.combinations(moviesToChoose, NUMBER_OF_CHOICES).stream()
            .map(this::toMatchOptions)
            .collect(Collectors.toSet());
    }

    private MatchOptions toMatchOptions(final Set<Movie> movies) {
        final var combination = movies.stream().toList();
        final var randomOption = RandomUtils.nextBoolean();
        final var fisrtOption = combination.get(randomOption ? 0 : 1);
        final var secondOption = combination.get(randomOption ? 1 : 0);
        return MatchOptions.with(
            MatchOption.with(fisrtOption.name(), fisrtOption.voteAverage()),
            MatchOption.with(secondOption.name(), secondOption.voteAverage()));
    }

}
