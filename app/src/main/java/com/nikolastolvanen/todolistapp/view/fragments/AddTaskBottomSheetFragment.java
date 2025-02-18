package com.nikolastolvanen.todolistapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.nikolastolvanen.todolistapp.R;
import com.nikolastolvanen.todolistapp.TaskAdapter;
import com.nikolastolvanen.todolistapp.Utils;
import com.nikolastolvanen.todolistapp.model.Task;
import com.nikolastolvanen.todolistapp.viewmodel.TaskViewModel;

import java.util.Calendar;
import java.util.Date;


public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {

    TaskViewModel taskViewModel;
    TaskAdapter adapter;

    private EditText editTextTitle;
    private CheckBox checkBoxImportant;
    private ImageButton buttonSaveTask;
    private ImageButton buttonOpenCalendar;
    private CalendarView calendarView;
    private Group calendarGroup;
    private TextView setDueDateTextView;
    private Chip todayChip;
    private Chip tomorrowChip;
    private Date dueDate;

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

        buttonOpenCalendar = view.findViewById(R.id.calendar_button);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);

        setDueDateTextView = view.findViewById(R.id.tv_set_due_date);

        todayChip = view.findViewById(R.id.today_chip);
        tomorrowChip = view.findViewById(R.id.tomorrow_chip);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle.requestFocus();
        editTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarGroup.setVisibility(View.GONE);
            }
        });

        taskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        adapter = new TaskAdapter();

        buttonOpenCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Close keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        todayChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar today = Calendar.getInstance();
                dueDate = today.getTime();
                setDueDateTextView.setVisibility(View.VISIBLE);
                String formatted = Utils.formatDate(dueDate);
                setDueDateTextView.setText(formatted);
            }
        });

        tomorrowChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.add(Calendar.DAY_OF_YEAR, 1);

                if (dueDate == null || !isSameDay(dueDate, tomorrow.getTime())) {
                    dueDate = tomorrow.getTime();
                    setDueDateTextView.setVisibility(View.VISIBLE);
                    String formatted = Utils.formatDate(dueDate);
                    setDueDateTextView.setText(formatted);
                }
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                dueDate = selectedDate.getTime();
                setDueDateTextView.setVisibility(View.VISIBLE);
                String formatted = Utils.formatDate(dueDate);
                setDueDateTextView.setText(formatted);
            }
        });

        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                boolean important = checkBoxImportant.isChecked();

                if (title.trim().isEmpty()) {
                    Toast.makeText(getContext(), R.string.please_insert_task_title, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dueDate == null) {
                    Toast.makeText(getContext(), R.string.please_insert_due_date, Toast.LENGTH_SHORT).show();
                    return;
                }

                Task task = new Task(title, important, dueDate);
                task.setCompleted(false);
                taskViewModel.insert(task);

                editTextTitle.getText().clear();
                checkBoxImportant.setChecked(false);
                dueDate = null;

                dismiss();
            }
        });

    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}