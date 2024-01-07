package com.nikolastolvanen.todolistapp;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvTaskList);

        adapter.setTaskListClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {

                Bundle bundle = new Bundle();
                bundle.putInt("taskId", task.getId());
                bundle.putString("taskName", task.getTaskName());
                bundle.putBoolean("taskCompleted", task.isCompleted());
                bundle.putBoolean("taskImportant", task.isImportant());

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.taskDetailsFragment, bundle);
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
        editTextTitle.requestFocus();

        CheckBox checkBoxImportant = dialog.findViewById(R.id.cb_important);
        ImageButton buttonSaveTask = dialog.findViewById(R.id.button_save_task);

        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                boolean important = checkBoxImportant.isChecked();

                if (title.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please insert task title", Toast.LENGTH_SHORT).show();
                    return;
                }

                taskViewModel.insert(new Task(title, important));
                //dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}