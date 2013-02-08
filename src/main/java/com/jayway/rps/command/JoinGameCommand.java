package com.jayway.rps.command;

import java.util.UUID;

public class JoinGameCommand implements Command {

    private final String playerId;
    private final UUID gameId;

    public JoinGameCommand(String playerId, UUID gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }

    @Override
    public UUID entityId() {
        return gameId;
    }

    public String getPlayerId() {
        return playerId;
    }
}
