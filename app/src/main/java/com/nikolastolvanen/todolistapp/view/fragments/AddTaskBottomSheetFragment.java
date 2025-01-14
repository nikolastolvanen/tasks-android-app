package com.nikolastolvanen.todolistapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nikolastolvanen.todolistapp.R;
import com.nikolastolvanen.todolistapp.TaskAdapter;
import com.nikolastolvanen.todolistapp.model.Task;
import com.nikolastolvanen.todolistapp.viewmodel.TaskViewModel;

import java.util.Calendar;


public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {

    TaskViewModel taskViewModel;
    TaskAdapter adapter;

    private EditText editTextTitle;
    private CheckBox checkBoxImportant;
    private ImageButton buttonSaveTask;

    public AddTaskBottomSheetFragment() {

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_add_task_bottom_sheet, container, false);

        editTextTitle = view.findViewById(R.id.edit_text_add_title);
        checkBoxImportant = view.findViewById(R.id.cb_important);
        buttonSaveTask = view.findViewById(R.id.button_save_task);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle.requestFocus();

        taskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        adapter = new TaskAdapter();

        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                boolean important = checkBoxImportant.isChecked();

                if (title.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please insert task title", Toast.LENGTH_SHORT).show();
                    return;
                }

                Task task = new Task(title, important, Calendar.getInstance().getTime());
                task.setCompleted(false);
                taskViewModel.insert(task);

                editTextTitle.getText().clear();

                dismiss();
            }
        });

    }

}