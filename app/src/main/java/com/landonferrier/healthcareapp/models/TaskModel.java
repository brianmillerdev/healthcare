package com.landonferrier.healthcareapp.models;

import java.util.Date;

public class TaskModel {

    private String name;
    private boolean completed;
    private Date date;
    private String id;
    private boolean isReminder;
    private int index;

    public TaskModel() {
        this.name = "";
        this.completed = false;
        this.date = new Date();
        this.id = "";
        this.isReminder = false;
        this.index = 0;
    }

    public TaskModel(String name, boolean completed, Date date, String id, boolean isReminder, int index) {
        this.name = name;
        this.completed = completed;
        this.date = date;
        this.id = id;
        this.isReminder = isReminder;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
