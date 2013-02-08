package com.jayway.rps;

import java.lang.reflect.Method;

/**
 * Created by Anders Eriksson.
 */
public class TheHandler<A> implements Handler<A> {

    @Override
    public <R> R handle(Object target, A argument) throws Exception {
        Method method = target.getClass().getMethod("handle", argument.getClass());

        return (R) method.invoke(target, argument);
    }





}
