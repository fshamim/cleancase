package com.ci.cleancase.framework;

import com.ci.ibus.IBus;
import com.ci.ibus.events.CancelEvent;
import com.ci.ibus.events.InProgressEvent;
import com.ci.ibus.events.ProgressFinishedEvent;
import com.ci.ibus.events.ShowToastEvent;
import com.ci.ibus.events.UnauthorizedErrorEvent;
import com.ci.ibus.events.UnknownErrorEvent;

import rx.Scheduler;
import rx.subscriptions.CompositeSubscription;

/**
 * Abstract Use Case that handles registration process of the output ports.
 * Created by fshamim on 18.09.14.
 */
public abstract class UseCaseInteractor implements IUseCaseInputPort {

    protected IBus mBus;

    protected Scheduler uiSchedular;
    protected Scheduler ioSchedular;

    protected CompositeSubscription subscriptions;

    public UseCaseInteractor(IBus bus, Scheduler uiSchedular, Scheduler ioSchedular) {
        this.mBus = bus;
        this.uiSchedular = uiSchedular;
        this.ioSchedular = ioSchedular;
        subscriptions = new CompositeSubscription();
        this.mBus.register(this);
    }

    protected void sendInProgressEvent(String loadingMsg) {
        mBus.post(new InProgressEvent(loadingMsg));
    }

    protected void sendProgressFinishedEvent() {
        mBus.post(new ProgressFinishedEvent());
    }

    protected void sendUnknownErrorEvent(int errorCode, String description) {
        mBus.post(new UnknownErrorEvent(errorCode, description));
    }

    protected void sendUnauthorizedErrorEvent() {
        mBus.post(new UnauthorizedErrorEvent());
    }

    protected void networkError() {
        String noInternetMessage = "Please check your WiFi connection: Host unreachable timeout";
        mBus.post(new ShowToastEvent(noInternetMessage, ShowToastEvent.ToastDuration.LONG).setColor(ShowToastEvent.ToastColor.RED));
    }

    @Override
    public void onEvent(CancelEvent event) {
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
            sendProgressFinishedEvent();
        }
    }
}
