package com.jayway.rps;

import com.jayway.rps.event.Event;

import java.util.List;
import java.util.UUID;

public class DelegatingEventStore implements EventStore {


    private final EventStore realEventStore;
    private final EventListener[] listeners;

    public DelegatingEventStore(EventStore realEventStore, EventListener... listeners) {
        this.realEventStore = realEventStore;
        this.listeners = listeners;
    }

    @Override
    public EventStream loadEventStream(UUID streamId) {
        return realEventStore.loadEventStream(streamId);
    }

    @Override
    public void store(UUID streamId, long version, List<Event> events) {
        realEventStore.store(streamId,version,events);
        for (EventListener listener : listeners) {
            listener.receive(events);
        }
    }
}
