package com.nikolastolvanen.todolistapp.view.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.nikolastolvanen.todolistapp.R;
import com.nikolastolvanen.todolistapp.TaskAdapter;
import com.nikolastolvanen.todolistapp.viewmodel.TaskViewModel;
import com.nikolastolvanen.todolistapp.model.Task;

import java.util.Calendar;
import java.util.List;


public class TaskListFragment extends Fragment {

    private TaskViewModel taskViewModel;
    TaskAdapter adapter;

    AddTaskBottomSheetFragment addTaskBottomSheetFragment;
    FloatingActionButton fabAddTask;

    int filter = 0;

    public TaskListFragment() {
        super(R.layout.fragment_task_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView rvTaskList = view.findViewById(R.id.recycler_view_tasks);
        rvTaskList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTaskList.setHasFixedSize(true);

        addTaskBottomSheetFragment = new AddTaskBottomSheetFragment();

        adapter = new TaskAdapter();
        rvTaskList.setAdapter(adapter);

        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        taskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);

        if (filter == 0) {
            taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), adapter::submitList);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.all_tasks);
        }
        if (filter == 1) {
            taskViewModel.getTodayTasks().observe(getViewLifecycleOwner(), adapter::submitList);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.today_s_tasks);
        }
        if (filter == 2) {
            taskViewModel.getImportantTasks().observe(getViewLifecycleOwner(), adapter::submitList);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.important_tasks);
        }

        BottomNavigationView btmNavView = getActivity().findViewById(R.id.btm_nav);
        btmNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                LifecycleOwner owner = getViewLifecycleOwner();

                taskViewModel.getAllTasks().removeObservers(owner);
                taskViewModel.getTodayTasks().removeObservers(owner);
                taskViewModel.getImportantTasks().removeObservers(owner);

                if (item.getItemId() == R.id.all_tasks) {
                    taskViewModel.getAllTasks().observe(owner, adapter::submitList);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.all_tasks);
                    filter = 0;
                } else if (item.getItemId() == R.id.today_tasks) {
                    taskViewModel.getTodayTasks().observe(owner, adapter::submitList);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.today_s_tasks);
                    filter = 1;
                } else if (item.getItemId() == R.id.important_tasks) {
                    taskViewModel.getImportantTasks().observe(owner, adapter::submitList);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.important_tasks);
                    filter = 2;
                }
                return true;
            }
        });

        fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskBottomSheetDialog();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            private final ColorDrawable deleteBackground = new ColorDrawable(Color.RED);

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                Task deletedTask = adapter.getTaskAt(position);

                taskViewModel.delete(deletedTask);
                showSnackBarDelete(deletedTask, position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                if (dX > 0) {
                    deleteBackground.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                } else if (dX < 0) {
                    deleteBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    deleteBackground.setBounds(0, 0, 0, 0);
                }

                deleteBackground.draw(canvas);

                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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
                bundle.putLong("taskDueDate", task.getDueDate().getTime());

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.taskDetailsFragment, bundle);
            }

            @Override
            public void onCheckBoxCompletedClick(Task task, boolean isChecked) {
                Task updatedTask = new Task(task.getTaskName(), task.isImportant(), task.getDueDate());
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.taskcompletedsound);
                final VibrationEffect vibrationEffect1;

                if (!task.isCompleted()) {
                    mediaPlayer.start();

                    vibrationEffect1 = VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE);
                    vibrator.cancel();
                    vibrator.vibrate(vibrationEffect1);
                }

                updatedTask.setCompleted(!task.isCompleted());
                updatedTask.setId(task.getId());
                taskViewModel.update(updatedTask);
            }

            public void onCheckBoxImportantClick(Task task, boolean isChecked) {
                Task updatedTask = new Task(task.getTaskName(), task.isImportant(), task.getDueDate());
                updatedTask.setCompleted(task.isCompleted());
                updatedTask.setImportant(!task.isImportant());
                updatedTask.setId(task.getId());
                taskViewModel.update(updatedTask);
            }

        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void showAddTaskBottomSheetDialog() {
        addTaskBottomSheetFragment.show(getParentFragmentManager(), addTaskBottomSheetFragment.getTag());
    }

    private void showSnackBarDelete(Task task, int position) {
        Snackbar snackbar = Snackbar.make(getView(), R.string.task_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        taskViewModel.insert(task);
                    }
                });
        snackbar.show();
    }

}