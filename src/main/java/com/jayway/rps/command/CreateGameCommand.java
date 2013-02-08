package com.jayway.rps.command;

import java.util.UUID;

public class CreateGameCommand implements Command {

    private final String playerId;
    private final UUID gameId;
    private final int firstTo;

    public CreateGameCommand(String playerId, UUID gameId, int firstTo) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.firstTo = firstTo;
    }

    @Override
    public UUID entityId() {
        return gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getFirstTo() {
        return firstTo;
    }
}
