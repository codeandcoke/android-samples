package com.svalero.todolist.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.todolist.R;
import com.svalero.todolist.view.TaskDetailsView;
import com.svalero.todolist.db.AppDatabase;
import com.svalero.todolist.domain.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks;

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position) {
        Task task = tasks.get(position);

        holder.tvName.setText(task.getName());
        holder.tvDescription.setText(task.getDescription());
        if (task.isDone()) {
            holder.doButton.setText(R.string.done);
            holder.parentView.setBackgroundColor(holder.parentView.getContext().getResources().getColor(android.R.color.holo_red_light));
        }
        else
            holder.doButton.setText(R.string.pending);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvDescription;
        public Button doButton;
        public Button detailsButton;
        public View parentView;

        public TaskHolder(@NonNull View view) {
            super(view);
            parentView = view;

            tvName = view.findViewById(R.id.task_item_name);
            tvDescription = view.findViewById(R.id.task_item_description);
            doButton = view.findViewById(R.id.do_item_button);
            detailsButton = view.findViewById(R.id.details_item_button);

            doButton.setOnClickListener(v -> doTask(view));
            detailsButton.setOnClickListener(v -> goToTaskDetails(view));
        }

        private void doTask(View itemView) {
            int currentPosition = getAdapterPosition();
            Task task = tasks.get(currentPosition);
            task.setDone(!task.isDone());
            AppDatabase db = Room.databaseBuilder(itemView.getContext(), AppDatabase.class, "tasks").allowMainThreadQueries().build();
            db.taskDao().update(task);
            if (task.isDone()) {
                doButton.setText(R.string.done);
                parentView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
            }
            else {
                doButton.setText(R.string.pending);
                parentView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white));
            }
        }

        private void goToTaskDetails(View itemView) {
            Intent intent = new Intent(itemView.getContext(), TaskDetailsView.class);
            Task task = tasks.get(getAdapterPosition());
            intent.putExtra("task_name", task.getName());
            itemView.getContext().startActivity(intent);
        }
    }
}
