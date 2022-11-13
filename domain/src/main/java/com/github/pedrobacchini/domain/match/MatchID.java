package com.github.pedrobacchini.domain.match;

import com.github.pedrobacchini.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class MatchID extends Identifier {

    private final UUID playerId;
    private final UUID matchId;

    private MatchID(final UUID playerId, final UUID matchId) {
        Objects.requireNonNull(playerId);
        Objects.requireNonNull(matchId);
        this.playerId = playerId;
        this.matchId = matchId;
    }

    public static MatchID unique() {
        return new MatchID(UUID.randomUUID(), UUID.randomUUID());
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public UUID getMatchId() {
        return matchId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MatchID matchID = (MatchID) o;
        return playerId.equals(matchID.playerId) && matchId.equals(matchID.matchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, matchId);
    }

}
