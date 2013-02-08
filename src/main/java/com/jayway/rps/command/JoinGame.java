package com.jayway.rps.command;

import java.util.UUID;

public class JoinGame implements Command {

    private final String playerId;
    private final UUID gameId;

    public JoinGame(String playerId, UUID gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }

    @Override
    public UUID entityId() {
        return gameId;
    }
}
