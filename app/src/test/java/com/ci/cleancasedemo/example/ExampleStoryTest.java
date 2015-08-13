package com.ci.cleancasedemo.example;

import com.ci.cleancasedemo.examplefeature.IWebApiInputPort;
import com.ci.cleancasedemo.examplefeature.api.IExampleWebApi;
import com.ci.cleancasedemo.examplefeature.interactor.ExampleWebApiUseCaseInteractor;
import com.ci.cleancasedemo.examplefeature.resources.Todo;
import com.ci.ibus.IBus;
import com.ci.ibus.events.InProgressEvent;
import com.ci.ibus.events.ProgressFinishedEvent;

import org.junit.Before;
import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.VerificationsInOrder;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * Created by fshamim on 22.09.14.
 */
public class ExampleStoryTest {

    private ExampleWebApiUseCaseInteractor interactor;
    @Mocked
    IBus bus;
    @Mocked
    IExampleWebApi mockedApi;

    @Before
    public void setUp() {
        interactor = new ExampleWebApiUseCaseInteractor(bus, Schedulers.immediate(), Schedulers.immediate(), mockedApi);
    }

    /**
     * Successful case for id 1 and 2
     */
    @Test
    public void testCaseSuccessful() {
        new Expectations() {
            {
                mockedApi.getTodoById("1");
                result = Observable.create(new Observable.OnSubscribe<Todo>() {
                    @Override
                    public void call(Subscriber<? super Todo> subscriber) {
                        subscriber.onNext(ExampleTestData.getTodoById(1));
                        subscriber.onCompleted();
                    }
                });
                mockedApi.getTodoById("2");
                result = Observable.create(new Observable.OnSubscribe<Todo>() {
                    @Override
                    public void call(Subscriber<? super Todo> subscriber) {
                        subscriber.onNext(ExampleTestData.getTodoById(2));
                        subscriber.onCompleted();
                    }
                });
            }
        };

        interactor.onEvent(new IWebApiInputPort.GetTodoByIdEvent("1"));
        interactor.onEvent(new IWebApiInputPort.GetTodoByIdEvent("2"));

        new VerificationsInOrder() {
            {
                bus.post(new InProgressEvent(null));
                bus.post(new IWebApiInputPort.TodoFoundEvent(ExampleTestData.getTodoById(1)));
                bus.post(new ProgressFinishedEvent());

                bus.post(new InProgressEvent(null));
                bus.post(new IWebApiInputPort.TodoFoundEvent(ExampleTestData.getTodoById(2)));
                bus.post(new ProgressFinishedEvent());
            }
        };
    }

    /**
     * Unsuccessful case for id 4343 followed by success with id 2
     */
    @Test
    public void testCaseUnSuccessful() {
        new Expectations() {
            {
                mockedApi.getTodoById("4343");
                result = Observable.create(new Observable.OnSubscribe<Todo>() {
                    @Override
                    public void call(Subscriber<? super Todo> subscriber) {
                        subscriber.onError(Utils.createResponseWithCodeAndJson(500, "Error"));
                    }
                });
                mockedApi.getTodoById("2");
                result = Observable.create(new Observable.OnSubscribe<Todo>() {
                    @Override
                    public void call(Subscriber<? super Todo> subscriber) {
                        subscriber.onNext(ExampleTestData.getTodoById(2));
                        subscriber.onCompleted();
                    }
                });
            }
        };

        interactor.onEvent(new IWebApiInputPort.GetTodoByIdEvent("4343"));
        interactor.onEvent(new IWebApiInputPort.GetTodoByIdEvent("2"));

        new VerificationsInOrder() {
            {
                bus.post(new InProgressEvent(null));
                bus.post(new ProgressFinishedEvent());
                bus.post(withAny(IWebApiInputPort.TodoNotFoundEvent.class));

                bus.post(new InProgressEvent(null));
                bus.post(new IWebApiInputPort.TodoFoundEvent(ExampleTestData.getTodoById(2)));
                bus.post(new ProgressFinishedEvent());
            }
        };
    }
}