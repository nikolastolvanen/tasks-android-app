package com.nikolastolvanen.todolistapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class TaskListFragment extends Fragment {

    FloatingActionButton fabAddTask;

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

        fabAddTask = view.findViewById(R.id.fab_add_new_task);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void showAddTaskDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_add_task);

        EditText editTextTitle = dialog.findViewById(R.id.edit_text_add_title);
        Button buttonSaveTask = dialog.findViewById(R.id.button_save_task);

        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();

                if (title.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please insert task title", Toast.LENGTH_SHORT).show();
                    return;
                }

                taskViewModel.insert(new Task(title));
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}