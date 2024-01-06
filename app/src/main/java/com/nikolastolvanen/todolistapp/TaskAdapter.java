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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    private OnTaskClickListener listener;

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task_list_item, parent, false);

        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.textViewTitle.setText(currentTask.getTaskName());
        holder.checkBoxCompleted.setChecked(currentTask.isCompleted());
        holder.checkBoxImportant.setChecked(currentTask.isImportant());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private CheckBox checkBoxCompleted;
        private CheckBox checkBoxImportant;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            checkBoxCompleted = itemView.findViewById(R.id.check_box_completed);
            checkBoxImportant = itemView.findViewById(R.id.check_box_important);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onTaskClick(tasks.get(position));
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
