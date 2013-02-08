package com.jayway.rps.event;

import java.util.UUID;

public class GameCreatedEvent implements Event {


    private final UUID entityId;

    public GameCreatedEvent(UUID entityId) {
        this.entityId = entityId;
    }


    public UUID getEntityId() {
        return entityId;
    }
}
