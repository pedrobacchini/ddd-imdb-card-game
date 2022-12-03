package com.github.pedrobacchini.application.match.retrieve.list;

import com.github.pedrobacchini.application.UseCase;
import com.github.pedrobacchini.domain.match.MatchSearchQuery;
import com.github.pedrobacchini.domain.pagination.Pagination;

public abstract class ListMatchesUseCase extends UseCase<MatchSearchQuery, Pagination<MatchListOutput>> {

}
