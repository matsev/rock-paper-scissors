package com.jayway.rps;

import com.jayway.rps.command.Command;
import com.jayway.rps.event.Event;

import java.util.List;

/**
 * Created by Anders Eriksson.
 */
public class TheApplicationService implements ApplicationService {


    private final EventStore eventStore;
    private final Class<?> aggregateType;
    private final Handler<Command> commandHandler;
    private final Handler<Event> eventHandler;
    private HighScoreProjection highScoreProjection;

    public TheApplicationService(EventStore eventStore, Class<?> aggregateType) {
        this.highScoreProjection = new HighScoreProjection();
        this.eventStore = new DelegatingEventStore(eventStore, highScoreProjection);
        this.aggregateType = aggregateType;
        this.commandHandler = new TheHandler<Command>();
        this.eventHandler = new TheHandler<Event>();
    }

    @Override
    public void handle(Command... commands) {
        for (Command command : commands) {
            EventStream stream = eventStore.loadEventStream(command.entityId());
            try {
                Object aggregate = aggregateType.newInstance();
                for (Event event : stream) {
                    eventHandler.handle(aggregate,event);
                }
                List<Event> newEvents = commandHandler.handle(aggregate, command);
                eventStore.store(command.entityId(), stream.version(), newEvents);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
