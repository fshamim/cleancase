package com.ci.cleancasedemo.examplefeature.api;


import com.ci.cleancasedemo.examplefeature.resources.Todo;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Interface endpoint needed for retrofit
 * Created by fshamim on 17.09.14.
 */
public interface IExampleWebApi {

    /**
     * Todos Endpoint
     * @param todoId id
     * @return RxJava Observable with Todos detail.
     */
    @GET("/todos/{todoId}")
    Observable<Todo> getTodoById(@Path("todoId") String todoId);

}
