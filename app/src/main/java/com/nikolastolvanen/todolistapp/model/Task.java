package com.nikolastolvanen.todolistapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "task_name")
    private String taskName;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @ColumnInfo(name = "is_important")
    private boolean isImportant;

    @ColumnInfo(name = "due_date")
    private Date dueDate;

    public Task(String taskName, boolean isImportant, Date dueDate) {
        this.taskName = taskName;
        this.isCompleted = false;
        this.isImportant = isImportant;
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

}
