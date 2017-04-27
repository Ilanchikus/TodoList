package com.example.todolist;

import java.io.Serializable;

/**
 * Created by Илана on 01.03.2017.
 */

public class Record implements Serializable {

    private String taskName;
    private String createdDate;
    private boolean status = false;

    public Record(String taskName, String createdDate) {
        setTaskName(taskName);
        setCreatedDate(createdDate);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean getStatus() {return status;}

    public void setStatus(boolean status) {this.status = status;}
}

