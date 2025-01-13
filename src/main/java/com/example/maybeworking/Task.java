package com.example.maybeworking;

import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Task {
    private final SimpleStringProperty name;
    private final SimpleStringProperty worker;
    private final SimpleBooleanProperty completed;

    public Task(String name, String worker, boolean completed) {
        this.name = new SimpleStringProperty(name);
        this.worker = new SimpleStringProperty(worker);
        this.completed = new SimpleBooleanProperty(completed);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getWorker() {
        return worker.get();
    }

    public void setWorker(String worker) {
        this.worker.set(worker);
    }

    public SimpleStringProperty workerProperty() {
        return worker;
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public SimpleBooleanProperty completedProperty() {
        return completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name.get() + '\'' +
                ", worker='" + worker.get() + '\'' +
                ", completed=" + completed.get() +
                '}';
    }


    public SimpleBooleanProperty getCompletedProperty() {
        return completed;
    }
}