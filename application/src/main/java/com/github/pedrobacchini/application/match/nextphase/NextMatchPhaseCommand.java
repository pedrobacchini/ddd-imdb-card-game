package com.github.pedrobacchini.application.match.nextphase;

import java.util.UUID;

public record NextMatchPhaseCommand(
    UUID playerId,
    UUID matchId,
    String playerMove
) {

    public static NextMatchPhaseCommand with(final UUID playerId, final UUID matchId, final String aPlayerMove) {
        return new NextMatchPhaseCommand(playerId, matchId, aPlayerMove);
    }

}
