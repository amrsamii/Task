package com.example.task.model;

public class Error implements IError{


    String name;
    String message;

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
