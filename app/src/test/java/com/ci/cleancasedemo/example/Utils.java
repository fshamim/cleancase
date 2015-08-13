package com.ci.cleancasedemo.example;

import com.ci.cleancasedemo.examplefeature.resources.Todo;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

/**
 * Created by fshamim on 05.05.15.
 */
public class Utils {
    public static RetrofitError createResponseWithCodeAndJson(int responseCode, String json) {
        String url = "http://example.org";
        Response response = new Response(url, responseCode,
                json,
                new ArrayList<Header>(),
                new TypedString(new Gson().toJson(ExampleTestData.getTodoById(1))));
        return RetrofitError.httpError("http://example.org", response,
                new GsonConverter(new Gson()),
                Todo.class);
    }

    public static RetrofitError createResponseWithCodeAndJson(int responseCode, String reason, String jsonBody, Class clazz) {
        String url = "http://example.org";
        Response response = new Response(url, responseCode,
                reason,
                new ArrayList<Header>(),
                new TypedString(jsonBody));
        return RetrofitError.httpError("http://example.org", response,
                new GsonConverter(new Gson()),
                clazz);
    }
}
