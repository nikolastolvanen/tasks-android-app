package com.nikolastolvanen.todolistapp.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikolastolvanen.todolistapp.R;
import com.nikolastolvanen.todolistapp.view.fragments.AddTaskBottomSheetFragment;
import com.nikolastolvanen.todolistapp.viewmodel.TaskViewModel;


public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    AddTaskBottomSheetFragment addTaskBottomSheetFragment;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        addTaskBottomSheetFragment = new AddTaskBottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.add_task_bottom_sheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.isHideable();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabAddTask = findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskBottomSheetDialog();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setMax(3);
        progressBar.setProgress(2);
    }

    private void showAddTaskBottomSheetDialog() {
        addTaskBottomSheetFragment.show(getSupportFragmentManager(), addTaskBottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

}
