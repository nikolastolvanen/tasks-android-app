package com.nikolastolvanen.todolistapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;


public class TaskListFragment extends Fragment {

    private TaskViewModel taskViewModel;

    public TaskListFragment() {
        super(R.layout.fragment_task_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView rvTaskList = view.findViewById(R.id.recycler_view_tasks);
        rvTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTaskList.setHasFixedSize(true);

        TaskAdapter adapter = new TaskAdapter();
        rvTaskList.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

}