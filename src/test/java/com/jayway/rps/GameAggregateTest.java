package com.jayway.rps;

import com.jayway.rps.command.CreateGameCommand;
import com.jayway.rps.event.Event;
import com.jayway.rps.event.GameCreatedEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class GameAggregateTest {

    private GameAggregate gameAggregate;
    String playerA = "a";
    String playerB = "b";
    UUID gameId = UUID.randomUUID();

    @Before
    public void setUp() {
        gameAggregate = new GameAggregate();
    }

    @Test
    public void createGame() {
        List<? extends Event> events = gameAggregate.handle(new CreateGameCommand(playerA, gameId, 3));

        assertThat(events, hasSize(1));
        assertThat(events.get(0), instanceOf(GameCreatedEvent.class));
    }
}
