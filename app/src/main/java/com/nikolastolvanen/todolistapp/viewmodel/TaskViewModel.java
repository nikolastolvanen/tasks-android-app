package com.nikolastolvanen.todolistapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nikolastolvanen.todolistapp.model.Task;
import com.nikolastolvanen.todolistapp.model.TaskRepository;

import java.util.List;


public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final LiveData<List<Task>> allTasks;
    private final LiveData<List<Task>> importantTasks;
    private final LiveData<List<Task>> todayTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
        importantTasks = repository.getImportantTasks();
        todayTasks = repository.getTodayTasks();
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }

    public void deleteCompletedTasks() {
        repository.deleteCompletedTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getImportantTasks() {
        return importantTasks;
    }

    public LiveData<List<Task>> getTodayTasks() {
        return todayTasks;
    }

}
