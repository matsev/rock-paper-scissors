package com.jayway.rps;

import com.jayway.rps.command.Command;

/**
 * Created by Anders Eriksson.
 */
public interface ApplicationService {

    void handle(Command... commands);
}
