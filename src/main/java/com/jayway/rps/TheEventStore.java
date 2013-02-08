package com.jayway.rps;

import com.jayway.rps.event.Event;

import java.util.*;

public class TheEventStore implements EventStore {

    private Map<UUID, EventStream> map;

    public TheEventStore() {
        map = new HashMap<UUID, EventStream>();
    }

    @Override
    public EventStream loadEventStream(UUID streamId) {
        EventStream events = map.get(streamId);
        if (events == null) {
            events = new TheEventStream(0L, Collections.<Event>emptyList());
            map.put(streamId, events);
        }
        return events;
    }

    @Override
    public void store(UUID streamId, long version, List<Event> events) {
        EventStream eventStream = map.get(streamId);
        if (eventStream.version() != version) {
            throw new IllegalStateException("Illegal version!!! " + version);
        }
        List<Event> allEvents = new ArrayList<Event>();
        for (Event event : eventStream) {
            allEvents.add(event);
        }
        allEvents.addAll(events);
        map.put(streamId, new TheEventStream(version + 1, allEvents));
    }
}
