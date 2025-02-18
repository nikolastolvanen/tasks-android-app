package com.nikolastolvanen.todolistapp;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.nikolastolvanen.todolistapp.model.Task;

import java.util.Calendar;
import java.util.Date;


public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskHolder> {

    private OnTaskClickListener listener;

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTaskName().equals(newItem.getTaskName()) &&
                    oldItem.isCompleted() == newItem.isCompleted() &&
                    oldItem.isImportant() == newItem.isImportant() &&
                    oldItem.getDueDate().equals(newItem.getDueDate());
        }
    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task_list_item, parent, false);

        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = getItem(position);

        String formatted = Utils.formatDate(currentTask.getDueDate());

        holder.textViewTitle.setText(currentTask.getTaskName());
        holder.checkBoxCompleted.setChecked(currentTask.isCompleted());
        holder.checkBoxImportant.setChecked(currentTask.isImportant());

        int dueDateColor = R.color.black;
        Drawable iconDrawable = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.baseline_today_16);

        if (isToday(currentTask.getDueDate())) {
            holder.textViewDueDate.setText(R.string.today);
            dueDateColor = R.color.due_green;
        } else if (isTomorrow(currentTask.getDueDate())) {
            holder.textViewDueDate.setText(R.string.tomorrow);
            dueDateColor = R.color.due_orange;
        } else if (isPast(currentTask.getDueDate())) {
            holder.textViewDueDate.setText(formatted);
            dueDateColor = R.color.due_red;
        } else {
            holder.textViewDueDate.setText(formatted);
            dueDateColor = R.color.black;
        }

        holder.textViewDueDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), dueDateColor));

        if (iconDrawable != null) {
            iconDrawable = DrawableCompat.wrap(iconDrawable);
            DrawableCompat.setTint(iconDrawable, ContextCompat.getColor(holder.itemView.getContext(), dueDateColor));
            holder.textViewDueDate.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null);
        }

    }

    private boolean isToday(Date date) {
        Calendar taskCalendar = Calendar.getInstance();
        taskCalendar.setTime(date);

        Calendar todayCalendar = Calendar.getInstance();

        return taskCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                taskCalendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isTomorrow(Date date) {
        Calendar taskCalendar = Calendar.getInstance();
        taskCalendar.setTime(date);

        Calendar tomorrowCalendar = Calendar.getInstance();
        tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1);

        return taskCalendar.get(Calendar.YEAR) == tomorrowCalendar.get(Calendar.YEAR) &&
                taskCalendar.get(Calendar.DAY_OF_YEAR) == tomorrowCalendar.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isPast(Date date) {
        Calendar taskCalendar = Calendar.getInstance();
        taskCalendar.setTime(date);

        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        return taskCalendar.before(todayCalendar);
    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final CheckBox checkBoxCompleted;
        private final CheckBox checkBoxImportant;
        private final TextView textViewDueDate;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            checkBoxCompleted = itemView.findViewById(R.id.check_box_completed);
            checkBoxImportant = itemView.findViewById(R.id.check_box_important);
            textViewDueDate = itemView.findViewById(R.id.chip_due_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onTaskClick(getItem(position));
                    }
                }
            });

            checkBoxCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onCheckBoxCompletedClick(getItem(position), getItem(position).isCompleted());
                    }
                }
            });

            checkBoxImportant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onCheckBoxImportantClick(getItem(position), getItem(position).isImportant());
                    }
                }
            });
        }
    }

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onCheckBoxCompletedClick(Task task, boolean isChecked);
        void onCheckBoxImportantClick(Task task, boolean isChecked);
    }

    public void setTaskListClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }

}
