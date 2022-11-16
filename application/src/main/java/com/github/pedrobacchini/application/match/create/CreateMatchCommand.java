package com.github.pedrobacchini.application.match.create;

import java.util.UUID;

public record CreateMatchCommand(
    UUID playerId,
    UUID matchId
) {

    public static CreateMatchCommand with(final UUID aPlayerId, final UUID aMatchId) {
        return new CreateMatchCommand(aPlayerId, aMatchId);
    }

}
