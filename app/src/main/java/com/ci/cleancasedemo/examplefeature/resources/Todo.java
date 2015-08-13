package com.ci.cleancasedemo.examplefeature.resources;

import lombok.Data;

/**
 * Pojo representing TodoResource
 * Created by fshamim on 17.09.14.
 */
@Data
public class Todo {
    private Integer userId;
    private Integer id;
    private String title;
    private Boolean completed;

    /**
     * Empty Constructor
     */
    public Todo() {
    }

    /**
     * Param Constructor for fast initilization
     *
     * @param id        id
     * @param userId    userId
     * @param title     title
     * @param completed completion status
     */
    public Todo(Integer id, Integer userId, String title, Boolean completed) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }
}
