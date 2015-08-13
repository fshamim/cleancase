package com.ci.cleancasedemo.examplefeature.interactor;

import com.ci.cleancase.framework.UseCaseInteractor;
import com.ci.cleancasedemo.examplefeature.IWebApiInputPort;
import com.ci.cleancasedemo.examplefeature.api.IExampleWebApi;
import com.ci.cleancasedemo.examplefeature.resources.Todo;
import com.ci.ibus.IBus;

import retrofit.RetrofitError;
import rx.Observer;
import rx.Scheduler;

/**
 * Example interactors for Web Api using RxJava and Retrofit for the web service call
 * Created by fshamim on 17.09.14.
 */
public class ExampleWebApiUseCaseInteractor extends UseCaseInteractor implements IWebApiInputPort {

    private final IExampleWebApi exampleWebApi;

    public ExampleWebApiUseCaseInteractor(IBus bus, Scheduler uiSchedular, Scheduler ioSchedular, IExampleWebApi exampleWebApi) {
        super(bus, uiSchedular, ioSchedular);
        this.exampleWebApi = exampleWebApi;
    }

    @Override
    public void onEvent(GetTodoByIdEvent event) {
        String id = event.todoId;
        sendInProgressEvent(null);
        subscriptions.add(exampleWebApi.getTodoById(id)
                .subscribeOn(ioSchedular)
                .observeOn(uiSchedular)
                .subscribe(new Observer<Todo>() {
                    @Override
                    public void onCompleted() {
                        sendProgressFinishedEvent();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable instanceof RetrofitError) {
                            RetrofitError error = (RetrofitError) throwable;
                            sendProgressFinishedEvent();
                            if (!error.isNetworkError()) {
                                sendTodoNotFoundEvent(error.getResponse().getStatus(), error.getResponse().getReason());
                            }
                        }
                    }

                    @Override
                    public void onNext(Todo todo) {
                        sendTodoDetailsFoundEvent(todo);
                    }
                }));
    }

    private void sendTodoNotFoundEvent(int statusCode, String msg) {
        mBus.post(new TodoNotFoundEvent(statusCode, msg));
    }

    private void sendTodoDetailsFoundEvent(Todo todo) {
        mBus.post(new TodoFoundEvent(todo));
    }

}
