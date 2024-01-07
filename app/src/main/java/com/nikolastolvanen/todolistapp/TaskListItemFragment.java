package com.nikolastolvanen.todolistapp;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


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