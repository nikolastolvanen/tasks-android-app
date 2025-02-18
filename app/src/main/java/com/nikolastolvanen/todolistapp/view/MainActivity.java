package com.nikolastolvanen.todolistapp.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikolastolvanen.todolistapp.R;
import com.nikolastolvanen.todolistapp.databinding.ActivityMainBinding;
import com.nikolastolvanen.todolistapp.view.fragments.AddTaskBottomSheetFragment;
import com.nikolastolvanen.todolistapp.viewmodel.TaskViewModel;


public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        ConstraintLayout constraintLayout = findViewById(R.id.add_task_bottom_sheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all) {
            taskViewModel.deleteAllTasks();
            Toast.makeText(this, R.string.deleted_all_tasks, Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.delete_completed) {
            taskViewModel.deleteCompletedTasks();
            Toast.makeText(this, R.string.deleted_completed_tasks, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}
