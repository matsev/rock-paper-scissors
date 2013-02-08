package com.jayway.rps.event;

import java.util.UUID;

public class GameCreatedEvent implements Event {


    private final UUID entityId;
    private final String playerId;
    private final int firstTo;

    public GameCreatedEvent(UUID entityId, String playerId, int firstTo) {
        this.entityId = entityId;
        this.playerId = playerId;
        this.firstTo = firstTo;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getFirstTo() {
        return firstTo;
    }
}
