package com.nikolastolvanen.todolistapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class TaskDetailsFragment extends Fragment {


    int taskId;
    String taskName;
    boolean taskCompleted;
    boolean taskImportant;
    String taskDueDate;


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
        //taskDueDate = ///////////////////////////////////////////////////////////////////////////////////////

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

                Task task = new Task(title, important);
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