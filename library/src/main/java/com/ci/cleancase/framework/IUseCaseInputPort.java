package com.ci.cleancase.framework;

import com.ci.ibus.events.CancelEvent;

/**
 * Interface for registering an output port to the interactors
 * Created by fshamim on 18.09.14.
 */
public interface IUseCaseInputPort {

    /**
     * onEvent a request if its running
     */
    void onEvent(CancelEvent event);
}
