package com.jayway.rps.command;

import com.jayway.rps.PlayerChoice;

import java.util.UUID;

public class MakeChoiceCommand implements Command {

    private final String playerId;
    private final Choice choice;
    private final UUID gameId;

    public MakeChoiceCommand(String playerId, Choice choice, UUID gameId) {
        this.playerId = playerId;
        this.choice = choice;
        this.gameId = gameId;
    }

    @Override
    public UUID entityId() {
        return gameId;
    }

    public PlayerChoice getPlayerChoice() {
        return new PlayerChoice(playerId, choice);
    }
}
