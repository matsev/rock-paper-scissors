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
        gameId = UUID.randomUUID();
    }

    @Test
    public void createGame() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 1));
        EventStream events = eventStore.loadEventStream(gameId);

        Iterator<Event> iterator = events.iterator();
        GameCreatedEvent gameCreatedEvent = (GameCreatedEvent) iterator.next();
        assertThat(gameCreatedEvent.getPlayerId(), is(playerA));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void joinGame() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 1),
                new JoinGameCommand(playerB, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        GameStartedEvent gameStartedEvent = (GameStartedEvent) events.getLastEvent();
        assertThat(gameStartedEvent.getPlayerId(), is(playerB));
    }

    @Test
    public void firstChoice() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 1),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        assertThat(events.getLastEvent(), instanceOf(ChoiceMadeEvent.class));
    }

    @Test
    public void secondChoiceDraw() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 1),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, PAPER, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        assertThat(events.getLastEvent(), instanceOf(RoundTiedEvent.class));
    }

    @Test
    public void secondChoiceRoundWinner() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 2),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        RoundWonEvent roundWonEvent = (RoundWonEvent) events.getLastEvent();
        assertThat(roundWonEvent.getWinner(), is(playerB));
    }

    @Test
    public void gameEndWinner() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 1),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        GameWonEvent gameWonEvent = (GameWonEvent) events.getLastEvent();
        assertThat(gameWonEvent.getWinner(), is(playerB));
    }

    @Test
    public void oneAndAHalfRound() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 2),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        assertThat(events.getLastEvent(), instanceOf(ChoiceMadeEvent.class));
    }

    @Test
    public void winnerForTwoRounds() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 2),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        GameWonEvent gameWonEvent = (GameWonEvent) events.getLastEvent();
        assertThat(gameWonEvent.getWinner(), is(playerB));
    }

    @Test
    public void oneAndAHalfRoundDraw() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 2),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, ROCK, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        assertThat(events.getLastEvent(), instanceOf(ChoiceMadeEvent.class));
    }

    @Test
    public void oneDrawOneWonRound() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 2),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, ROCK, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        RoundWonEvent roundWonEvent = (RoundWonEvent) events.getLastEvent();
        assertThat(roundWonEvent.getWinner(), is(playerB));

    }

    @Test
    public void oneDrawOneWonRoundEndsGame() {
        applicationService.handle(new CreateGameCommand(playerA, gameId, 1),
                new JoinGameCommand(playerB, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, ROCK, gameId),
                new MakeChoiceCommand(playerA, ROCK, gameId),
                new MakeChoiceCommand(playerB, PAPER, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        GameWonEvent gameWonEvent = (GameWonEvent) events.getLastEvent();
        assertThat(gameWonEvent.getWinner(), is(playerB));
    }

}
