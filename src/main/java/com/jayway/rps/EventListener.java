package com.jayway.rps;

import com.jayway.rps.event.Event;

public interface EventListener {

    void receive(Iterable<Event> events);

}