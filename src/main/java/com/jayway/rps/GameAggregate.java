package com.jayway.rps;

import com.jayway.rps.command.CommandHandler;
import com.jayway.rps.command.CreateGameCommand;
import com.jayway.rps.command.JoinGameCommand;
import com.jayway.rps.command.MakeChoiceCommand;
import com.jayway.rps.event.Event;
import com.jayway.rps.event.EventHandler;
import com.jayway.rps.event.GameCreatedEvent;
import com.jayway.rps.event.GameStartedEvent;

import java.util.List;
import java.util.UUID;

import static com.jayway.rps.State.STARTED;
import static com.jayway.rps.State.WAITING_FOR_ROUND;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * Created by Anders Eriksson.
 */
public class GameAggregate  {

    private UUID gameId;
    private State state;


    @CommandHandler
    public List<? extends Event> handle(CreateGameCommand createGameCommand) {
        if (state == null) {
            return asList(new GameCreatedEvent(createGameCommand.entityId()));
        }
        return emptyList();
    }

    @CommandHandler
    public List<? extends Event> handle(JoinGameCommand joinGameCommand) {
        if (state.equals(STARTED)) {
            return asList(new GameStartedEvent());
        }
        return emptyList();
    }

    @CommandHandler
    public List<? extends Event> handle(MakeChoiceCommand makeChoiceCommand) {
        return asList();
    }

    @EventHandler
    public void handle(GameCreatedEvent gameCreatedEvent) {
        gameId = gameCreatedEvent.getEntityId();
        state = STARTED;
    }

    @EventHandler
    public void handle(GameStartedEvent gameStartedEvent) {
        state = WAITING_FOR_ROUND;
    }
}
