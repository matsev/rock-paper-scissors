package com.jayway.rps.command;

import java.util.UUID;

public class CreateGame implements Command {

    private final String playerId;
    private final UUID gameId;

    public CreateGame(String playerId, UUID gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }

    @Override
    public UUID entityId() {
        return gameId;
    }
}