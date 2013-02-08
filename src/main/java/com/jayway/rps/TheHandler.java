package com.jayway.rps;

import com.jayway.rps.command.Command;
import com.jayway.rps.command.CreateGame;
import com.jayway.rps.event.Event;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Anders Eriksson.
 */
public class TheHandler<A> implements Handler<A> {

    @Override
    public <R> R handle(Object target, A argument) throws Exception {
        Method method = target.getClass().getMethod("handle", argument.getClass());

        return (R) method.invoke(target, argument);
    }


    public static void main(String[] args) throws Exception {
        TheHandler<Command> commandHandler = new TheHandler<Command>();
        TheHandler<Event> eventHandler = new TheHandler<Event>();

        GameAggregate game = new GameAggregate();
        UUID uuid = UUID.randomUUID();
        Object events = commandHandler.handle(game, new CreateGame("p1", uuid));
        System.out.println(events);
    }


}
