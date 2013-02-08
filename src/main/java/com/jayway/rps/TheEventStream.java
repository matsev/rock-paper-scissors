package com.jayway.rps;

import com.jayway.rps.event.Event;

import java.util.Iterator;
import java.util.List;

public class TheEventStream implements EventStream {

    private final long version;
    private final List<Event> events;

    public TheEventStream(long version, List<Event> events) {
        this.version = version;
        this.events = events;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Event getLastEvent() {
        return events.get(events.size() -1);
    }


    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
