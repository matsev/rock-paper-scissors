package com.jayway.rps;

import com.jayway.rps.event.Event;

import java.util.List;
import java.util.UUID;

public interface EventStore {

    EventStream loadEventStream(UUID streamId);

    void store(UUID streamId, long version, List<Event> events);
}

