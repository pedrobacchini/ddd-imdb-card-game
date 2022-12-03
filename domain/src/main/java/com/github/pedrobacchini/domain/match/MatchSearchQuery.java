package com.github.pedrobacchini.domain.match;

public record MatchSearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction
) {

}
