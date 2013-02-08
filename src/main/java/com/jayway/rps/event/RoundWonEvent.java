package com.jayway.rps.event;

public class RoundWonEvent implements Event {
    private final String playerId;

    public RoundWonEvent(String playerId) {
        this.playerId = playerId;
    }

    public String getWinner() {
        return playerId;
    }
}
