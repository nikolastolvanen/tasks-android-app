package com.nikolastolvanen.todolistapp.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nikolastolvanen.todolistapp.R;


public class TaskListItemFragment extends Fragment {

    CheckBox checkBoxCompleted;
    CheckBox checkBoxImportant;
    TextView textViewTaskName;

    public TaskListItemFragment() {
        super(R.layout.fragment_task_list_item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkBoxCompleted = view.findViewById(R.id.check_box_completed);
        checkBoxImportant = view.findViewById(R.id.check_box_important);
        textViewTaskName = view.findViewById(R.id.text_view_title);

        if (checkBoxCompleted.isChecked()) {
            textViewTaskName.setText("AKJGHJFHGJHGFJHfk");
        }





    }

}