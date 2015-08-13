package com.ci.cleancasedemo.example;

import com.ci.cleancasedemo.examplefeature.resources.Todo;

import java.util.HashMap;


/**
 * Created by fshamim on 23.09.14.
 */
public class ExampleTestData {

    static HashMap<Integer, Todo> todoMap = new HashMap<Integer, Todo>();

    static {
        Todo todo1 = new Todo(1, 1, "delectus aut autem", false);
        Todo todo2 = new Todo(2, 1, "quis ut nam facilis et officia qui", false);
        todoMap.put(1, todo1);
        todoMap.put(2, todo2);
    }

    public static Todo getTodoById(Integer id) {
        return todoMap.get(id);
    }
}
