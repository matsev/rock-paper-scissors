package com.jayway.rps;

import com.jayway.rps.command.CreateGame;
import com.jayway.rps.event.GameCreated;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TheApplicationServiceTest {

    private TheEventStore eventStore;
    private TheApplicationService applicationService;

    @Before
    public void setUp() {
        eventStore = new TheEventStore();
        applicationService = new TheApplicationService(eventStore, GameAggregate.class);
    }

    @Test
    public void handle() {
        UUID id = UUID.randomUUID();
        applicationService.handle(new CreateGame("morot", id));
        EventStream events = eventStore.loadEventStream(id);

        assertThat(events.iterator().next(), instanceOf(GameCreated.class));
        assertThat(events.iterator().hasNext(), is(false));
    }
}
