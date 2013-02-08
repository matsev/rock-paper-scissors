package com.jayway.rps.event;

import java.util.UUID;

public class GameCreatedEvent implements Event {


    private final UUID entityId;
    private String playerId;

    public GameCreatedEvent(UUID entityId, String playerId) {
        this.entityId = entityId;
        this.playerId = playerId;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public String getPlayerId() {
        return playerId;
    }

}
