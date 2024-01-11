package com.nikolastolvanen.todolistapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
                    oldItem.isImportant() == newItem.isImportant();
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
        holder.textViewTitle.setText(currentTask.getTaskName());
        holder.checkBoxCompleted.setChecked(currentTask.isCompleted());
        holder.checkBoxImportant.setChecked(currentTask.isImportant());
        //holder.textViewDueDate.setText(currentTask.isDueDate());
    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private CheckBox checkBoxCompleted;
        private CheckBox checkBoxImportant;
        //private TextView textViewDueDate;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            checkBoxCompleted = itemView.findViewById(R.id.check_box_completed);
            checkBoxImportant = itemView.findViewById(R.id.check_box_important);
            //textViewDueDate = itemView.findViewById(R.id.text_view_due_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onTaskClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public void setTaskListClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }

}
