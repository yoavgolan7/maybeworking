package com.example.maybeworking;

public class Task {
    private static int idCounter = 0;
    private int id;
    private String name;
    private String worker;
    private boolean completed;

    public Task(String name, String worker, boolean completed) {
        this.id = ++idCounter;
        this.name = name;
        this.worker = worker;
        this.completed = completed;
    }

    // Getters and setters (if needed)

    @Override
    public String toString() {
        return "Task{id=" + id + ", name='" + name + "', worker='" + worker + "', completed=" + completed + "}";
    }
}