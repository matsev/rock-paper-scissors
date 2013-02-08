package com.jayway.rps;

public enum State {

    STARTED,
    ACCEPTED,
    WAITING_FOR_ROUND,
    COMPLETED,

    ROUND_STARTED,
    ROUND_TIMED_OUT,
    ROUND_WAITING_FOR_PLAYER,
    ROUND_TIE,
    ROUND_WINNER
}
