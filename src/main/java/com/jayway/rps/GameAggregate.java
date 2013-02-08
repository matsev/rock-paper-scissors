package com.jayway.rps;

import com.jayway.rps.command.CommandHandler;
import com.jayway.rps.command.CreateGameCommand;
import com.jayway.rps.command.JoinGameCommand;
import com.jayway.rps.command.MakeChoiceCommand;
import com.jayway.rps.event.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.rps.State.*;
import static com.jayway.rps.command.Choice.PAPER;
import static com.jayway.rps.command.Choice.ROCK;
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
    private int firstTo;
    private Map<String, AtomicInteger> wins;

    @CommandHandler
    public List<? extends Event> handle(CreateGameCommand createGameCommand) {
        if (state == null) {
            return asList(new GameCreatedEvent(createGameCommand.entityId(), createGameCommand.getPlayerId(), createGameCommand.getFirstTo()));
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
                if (firstChoice != null) {
                    throw new IllegalStateException("did not expect firstChoice to be set when " + WAITING_FOR_ROUND);
                }
                //TODO if player have already made choice
                //TODO check that player names have not changed since previous round
                return asList(new ChoiceMadeEvent(makeChoiceCommand.getPlayerChoice()));
            case ROUND_WAITING_FOR_PLAYER:
                //TODO if player have already made choice
                //TODO check that player names have not changed since previous round
                PlayerChoice secondChoice = makeChoiceCommand.getPlayerChoice();
                // TODO implement game rules
                if (firstChoice.getChoice() == secondChoice.getChoice()) {
                    return asList(new ChoiceMadeEvent(makeChoiceCommand.getPlayerChoice()), new RoundTiedEvent());
                }
                if (firstChoice.getChoice() == ROCK && secondChoice.getChoice() == PAPER) {
                    List<Event> events = new ArrayList<Event>();
                    String winner = secondChoice.getPlayerId();
                    events.add(new ChoiceMadeEvent(makeChoiceCommand.getPlayerChoice()));
                    events.add(new RoundWonEvent(winner));
                    int numberOfWins = wins.get(winner).get() + 1;
                    if (numberOfWins == firstTo) {
                        events.add(new GameWonEvent(winner));
                    }
                    return events;
                }
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
        firstTo = gameCreatedEvent.getFirstTo();
        wins = new HashMap<String, AtomicInteger>();
        wins.put(firstPlayerId, new AtomicInteger());
    }

    @EventHandler
    public void handle(GameStartedEvent gameStartedEvent) {
        state = WAITING_FOR_ROUND;
        secondPlayerId = gameStartedEvent.getPlayerId();
        wins.put(gameStartedEvent.getPlayerId(), new AtomicInteger());
    }

    @EventHandler
    public void handle(ChoiceMadeEvent choiceMadeEvent) {
        switch (state) {
            case WAITING_FOR_ROUND :
                state = ROUND_WAITING_FOR_PLAYER;
                firstChoice = choiceMadeEvent.getPlayerChoice();
                break;
            case ROUND_WAITING_FOR_PLAYER:
                PlayerChoice secondChoice = choiceMadeEvent.getPlayerChoice();
                if (secondChoice.getChoice() == firstChoice.getChoice()) {
                    state = ROUND_TIE;
                } else {
                    state = ROUND_WINNER;
                }
                break;
            default:
                throw new IllegalStateException(state.toString());
        }
    }


    @EventHandler
    public void handle(RoundWonEvent roundWonEvent) {
        if (state == ROUND_WINNER) {
            wins.get(roundWonEvent.getWinner()).incrementAndGet();
            state = WAITING_FOR_ROUND;
            firstChoice = null;
        } else {
            throw new IllegalStateException(state.toString());
        }
    }


    @EventHandler
    public void handle(RoundTiedEvent roundTiedEvent) {
        if (state == ROUND_TIE) {
            state = WAITING_FOR_ROUND;
            firstChoice = null;
        } else {
            throw new IllegalStateException(state.toString());
        }
    }


    @EventHandler
    public void handle(GameWonEvent gameWonEvent) {
        state = COMPLETED;
    }
}
