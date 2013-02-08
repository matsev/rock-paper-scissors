package com.jayway.rps.event;

import com.jayway.rps.PlayerChoice;

public class ChoiceMadeEvent implements Event {

    private final PlayerChoice playerChoice;

    public ChoiceMadeEvent(PlayerChoice playerChoice) {

        this.playerChoice = playerChoice;
    }

    public PlayerChoice getPlayerChoice() {
        return playerChoice;
    }
}
