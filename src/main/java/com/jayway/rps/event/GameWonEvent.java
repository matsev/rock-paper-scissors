package com.jayway.rps.event;

public class GameWonEvent implements Event {

    private final String playerId;

    public GameWonEvent(String playerId) {
        this.playerId = playerId;
    }

    public String getWinner() {
        return playerId;
    }
}
