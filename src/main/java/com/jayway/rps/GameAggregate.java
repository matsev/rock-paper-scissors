package com.jayway.rps;

import com.jayway.rps.command.CommandHandler;
import com.jayway.rps.command.CreateGameCommand;
import com.jayway.rps.command.JoinGameCommand;
import com.jayway.rps.command.MakeChoiceCommand;
import com.jayway.rps.event.*;

import java.util.List;
import java.util.UUID;

import static com.jayway.rps.State.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * Created by Anders Eriksson.
 */
public class GameAggregate  {

    private UUID gameId;
    private State state;
    private String firstPlayerId;
    private String secondPlayerId;
    private PlayerChoice firstChoice;


    @CommandHandler
    public List<? extends Event> handle(CreateGameCommand createGameCommand) {
        if (state == null) {
            return asList(new GameCreatedEvent(createGameCommand.entityId(), createGameCommand.getPlayerId()));
        }
        return emptyList();
    }

    @CommandHandler
    public List<? extends Event> handle(JoinGameCommand joinGameCommand) {
        //TODO check same player
        if (state.equals(STARTED)) {
            return asList(new GameStartedEvent(joinGameCommand.getPlayerId()));
        }
        return emptyList();
    }

    @CommandHandler
    public List<? extends Event> handle(MakeChoiceCommand makeChoiceCommand) {
        switch (state) {
            case WAITING_FOR_ROUND :
                //TODO if player have already made choice
                return asList(new ChoiceMadeEvent(makeChoiceCommand.getPlayerChoice()));
            case ROUND_WAITING_FOR_PLAYER:
                //TODO if player have already made choice
                return asList(new RoundTiedEvent());
            default:
                return emptyList();
        }
    }

    @EventHandler
    public void handle(GameCreatedEvent gameCreatedEvent) {
        gameId = gameCreatedEvent.getEntityId();
        state = STARTED;
        firstPlayerId = gameCreatedEvent.getPlayerId();
    }

    @EventHandler
    public void handle(GameStartedEvent gameStartedEvent) {
        state = WAITING_FOR_ROUND;
        secondPlayerId = gameStartedEvent.getPlayerId();
    }

    @EventHandler
    public void handle(ChoiceMadeEvent choiceMadeEvent) {
        switch (state) {
            case WAITING_FOR_ROUND :
                state = ROUND_WAITING_FOR_PLAYER;
                firstChoice = choiceMadeEvent.getPlayerChoice();
                break;
            case ROUND_WAITING_FOR_PLAYER:
                state = ROUND_TIE;
                break;
            default:
        }
    }
}
