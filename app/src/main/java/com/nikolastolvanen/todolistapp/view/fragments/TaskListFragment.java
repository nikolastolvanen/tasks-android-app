package com.nikolastolvanen.todolistapp.view.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.media.MediaPlayer;
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

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.nikolastolvanen.todolistapp.R;
import com.nikolastolvanen.todolistapp.TaskAdapter;
import com.nikolastolvanen.todolistapp.viewmodel.TaskViewModel;
import com.nikolastolvanen.todolistapp.model.Task;

import java.util.Calendar;
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

        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        taskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.submitList(tasks);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Task deletedTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();

                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                showSnackBarDelete(deletedTask, position);
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

            @Override
            public void onCheckBoxCompletedClick(Task task, boolean isChecked) {
                Task updatedTask = new Task(task.getTaskName(), task.isImportant(), Calendar.getInstance().getTime());
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.taskcompletedsound);
                final VibrationEffect vibrationEffect1;

                if (!task.isCompleted()) {
                    mediaPlayer.start();

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrationEffect1 = VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE);
                        vibrator.cancel();
                        vibrator.vibrate(vibrationEffect1);
                    }
                }

                updatedTask.setCompleted(!task.isCompleted());
                updatedTask.setId(task.getId());

                taskViewModel.update(updatedTask);
            }

        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void showSnackBarDelete(Task task, int position) {
        Snackbar snackbar = Snackbar.make(getView(), "Task deleted", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        taskViewModel.insert(task);
                    }
                });
        snackbar.show();
    }

}