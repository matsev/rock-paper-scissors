package com.jayway.rps.command;

import java.util.UUID;

public class MakeChoiceCommand implements Command {

    private final Choice choice;
    private final UUID gameId;

    public MakeChoiceCommand(Choice choice, UUID gameId) {
        this.choice = choice;
        this.gameId = gameId;
    }

    @Override
    public UUID entityId() {
        return gameId;
    }
}
