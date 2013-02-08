package com.jayway.rps;

import com.jayway.rps.event.Event;

class HighScoreProjection implements EventListener {

    @Override
    public void receive(Iterable<Event> events) {
        System.out.println("Hjälp, jag får en massa events och jag vet inte vad jag ska göra...");
    }
}