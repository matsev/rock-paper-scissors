package com.jayway.rps;

import com.jayway.rps.command.CreateGameCommand;
import com.jayway.rps.command.JoinGameCommand;
import com.jayway.rps.command.MakeChoiceCommand;
import com.jayway.rps.event.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.UUID;

import static com.jayway.rps.command.Choice.PAPER;
import static com.jayway.rps.command.Choice.ROCK;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TheApplicationServiceTest {

    private TheEventStore eventStore;
    private TheApplicationService applicationService;
    private UUID gameId;
    String playerA = "a";
    String playerB = "b";


    @Before
    public void setUp() {
        eventStore = new TheEventStore();
        applicationService = new TheApplicationService(eventStore, GameAggregate.class);
    }

    @Test
    public void createGame() {
        gameId = UUID.randomUUID();
        applicationService.handle(new CreateGameCommand(playerA, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        Iterator<Event> iterator = events.iterator();
        GameCreatedEvent gameCreatedEvent = (GameCreatedEvent) iterator.next();
        assertThat(gameCreatedEvent.getPlayerId(), is(playerA));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void joinGame() {
        gameId = UUID.randomUUID();
        applicationService.handle(new CreateGameCommand(playerA, gameId),
                new JoinGameCommand(playerB, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        GameStartedEvent gameStartedEvent = (GameStartedEvent) events.getLastEvent();
        assertThat(gameStartedEvent.getPlayerId(), is(playerB));
    }

    @Test
    public void firstChoice() {
        gameId = UUID.randomUUID();
        applicationService.handle(new CreateGameCommand(playerA, gameId),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        assertThat(events.getLastEvent(), instanceOf(ChoiceMadeEvent.class));
    }

    @Test
    public void secondChoiceDraw() {
        gameId = UUID.randomUUID();
        applicationService.handle(new CreateGameCommand(playerA, gameId),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, PAPER, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        assertThat(events.getLastEvent(), instanceOf(RoundTiedEvent.class));
    }

    @Test
    public void secondChoiceWinner() {
        gameId = UUID.randomUUID();
        applicationService.handle(new CreateGameCommand(playerA, gameId),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        RoundWonEvent roundWonEvent = (RoundWonEvent) events.getLastEvent();
        assertThat(roundWonEvent.getWinner(), is(playerB));
    }

}
