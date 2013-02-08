package com.jayway.rps;

import com.jayway.rps.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TheEventStore implements EventStore {

    private Map<UUID, EventStream> map;

    @Override
    public EventStream loadEventStream(UUID streamId) {
        return map.get(streamId);
    }

    @Override
    public void store(UUID streamId, long version, List<Event> events) {
        EventStream eventStream = map.get(streamId);
        if (eventStream == null) {
            eventStream = new TheEventStream(0L, events);
            map.put(streamId, eventStream);
        } else {
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
}
