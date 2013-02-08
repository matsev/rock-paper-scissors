package com.jayway.rps;

import com.jayway.rps.command.CommandHandler;
import com.jayway.rps.command.CreateGame;
import com.jayway.rps.event.Event;
import com.jayway.rps.event.EventHandler;
import com.jayway.rps.event.GameCreated;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Anders Eriksson.
 */
public class GameAggregate  {

    private UUID gameId;


    @EventHandler
    public void handle(GameCreated gameCreated) {
        this.gameId = gameCreated.getEntityId();
    }

    @CommandHandler
    public List<? extends Event> handle(CreateGame createGame) {
        return Arrays.asList(new GameCreated(createGame.entityId()));
    }
}
