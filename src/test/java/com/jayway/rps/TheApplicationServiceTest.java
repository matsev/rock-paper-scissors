package com.jayway.rps;

import com.jayway.rps.command.CreateGameCommand;
import com.jayway.rps.command.JoinGameCommand;
import com.jayway.rps.event.Event;
import com.jayway.rps.event.GameCreatedEvent;
import com.jayway.rps.event.GameStartedEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.UUID;

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
        assertThat(iterator.next(), instanceOf(GameCreatedEvent.class));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void joinGame() {
        gameId = UUID.randomUUID();
        applicationService.handle(new CreateGameCommand(playerA, gameId));
        applicationService.handle(new JoinGameCommand(playerB, gameId));
        EventStream events = eventStore.loadEventStream(gameId);

        assertThat(events.getLastEvent(), instanceOf(GameStartedEvent.class));
    }

}
