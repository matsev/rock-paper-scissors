package com.jayway.rps;

import com.jayway.rps.event.Event;

public interface EventStream extends Iterable<Event> {

    long version();

    Event getLastEvent();
}
