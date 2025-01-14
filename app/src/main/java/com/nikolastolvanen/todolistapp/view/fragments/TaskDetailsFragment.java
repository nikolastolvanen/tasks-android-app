package com.nikolastolvanen.todolistapp.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.nikolastolvanen.todolistapp.R;
import com.nikolastolvanen.todolistapp.TaskAdapter;
import com.nikolastolvanen.todolistapp.viewmodel.TaskViewModel;
import com.nikolastolvanen.todolistapp.model.Task;

import java.util.Calendar;
import java.util.Date;


public class TaskDetailsFragment extends Fragment {

    int taskId;
    String taskName;
    boolean taskCompleted;
    boolean taskImportant;
    Date taskDueDate;

    TaskViewModel taskViewModel;
    TaskAdapter adapter;

    public TaskDetailsFragment() {
        super(R.layout.fragment_task_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskId = getArguments().getInt("taskId");
        taskName = getArguments().getString("taskName");
        taskCompleted = getArguments().getBoolean("taskCompleted");
        taskImportant = getArguments().getBoolean("taskImportant");
        //taskDueDate = getArguments().getDate("taskDueDate");

        taskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        adapter = new TaskAdapter();

        CheckBox cbTaskDetailsCompleted = view.findViewById(R.id.check_box_task_details_completed);
        EditText etTaskName = view.findViewById(R.id.edit_text_task_details_title);
        CheckBox cbTaskDetailsImportant = view.findViewById(R.id.check_box_task_details_important);

        Button btnUpdate = view.findViewById(R.id.button_update_task);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = etTaskName.getText().toString();
                boolean important = cbTaskDetailsImportant.isChecked();
                boolean completed = cbTaskDetailsCompleted.isChecked();

                if (title.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please insert task title", Toast.LENGTH_SHORT).show();
                    return;
                }

                Task task = new Task(title, important, Calendar.getInstance().getTime());
                task.setCompleted(completed);
                task.setId(taskId);
                taskViewModel.update(task);

                getParentFragmentManager().popBackStackImmediate();

            }
        });

        etTaskName.setText(taskName);
        cbTaskDetailsCompleted.setChecked(taskCompleted);
        cbTaskDetailsImportant.setChecked(taskImportant);

    }

}