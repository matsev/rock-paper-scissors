package com.jayway.rps;

import com.jayway.rps.command.Choice;

public class PlayerChoice {

    private final String playerId;
    private final Choice choice;


    public PlayerChoice(String playerId, Choice choice) {
        this.playerId = playerId;
        this.choice = choice;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Choice getChoice() {
        return choice;
    }
}
