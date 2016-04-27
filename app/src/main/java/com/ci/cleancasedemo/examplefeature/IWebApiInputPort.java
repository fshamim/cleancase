package com.ci.cleancasedemo.examplefeature;

import com.ci.cleancasedemo.examplefeature.resources.Todo;

import lombok.Data;

/**
 * Input Port for Web Api Example Use case
 * Created by fshamim on 17.09.14.
 */
public interface IWebApiInputPort {
    /**
     * Get the TodoResource from the web api by its id.
     *
     * @param id
     */
    void onEvent(GetTodoByIdEvent id);

    @Data
    class GetTodoByIdEvent {
        public final String todoId;

        public GetTodoByIdEvent(String todoId) {
            this.todoId = todoId;
        }
    }

    @Data
    class TodoFoundEvent {
        public Todo todo;

        public TodoFoundEvent(Todo todo) {
            this.todo = todo;
        }
    }

    @Data
    class TodoNotFoundEvent {
        public final int code;
        public final String reason;

        public TodoNotFoundEvent(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }
    }


}
