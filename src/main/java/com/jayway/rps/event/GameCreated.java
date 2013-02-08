package com.jayway.rps.event;

import java.util.UUID;

public class GameCreated implements Event {


    private final UUID entityId;

    public GameCreated(UUID entityId) {
        this.entityId = entityId;
    }


    public UUID getEntityId() {
        return entityId;
    }
}
