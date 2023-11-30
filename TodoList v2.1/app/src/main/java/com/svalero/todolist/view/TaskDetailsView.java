package com.svalero.todolist.view;

import static com.svalero.todolist.util.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.svalero.todolist.R;
import com.svalero.todolist.db.AppDatabase;
import com.svalero.todolist.domain.Task;

public class TaskDetailsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent intent = getIntent();
        String taskName = intent.getStringExtra("task_name");
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        Task task = db.taskDao().findByName(taskName);
        loadTask(task);
    }

    private void loadTask(Task task) {
        TextView tvName = findViewById(R.id.details_task_name);
        TextView tvDescription = findViewById(R.id.details_task_description);
        CheckBox cbUrgent = findViewById(R.id.details_task_urgent);
        TextView tvDone = findViewById(R.id.details_task_done);

        tvName.setText(task.getName());
        tvDescription.setText(task.getDescription());
        cbUrgent.setChecked(task.isUrgent());
        if (task.isDone())
            tvDone.setText(R.string.done);
        else
            tvDone.setText(R.string.pending);
    }
}