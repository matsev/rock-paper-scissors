package com.jayway.rps.event;

public class GameStartedEvent implements Event {


    private final String playerId;

    public GameStartedEvent(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}
