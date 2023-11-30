package com.svalero.todolist.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.todolist.R;
import com.svalero.todolist.adapter.TaskAdapter;
import com.svalero.todolist.contract.TaskListContract;
import com.svalero.todolist.domain.Task;
import com.svalero.todolist.preferences.PreferencesActivity;
import com.svalero.todolist.presenter.TaskListPresenter;

import java.util.ArrayList;
import java.util.List;

public class TaskListView extends AppCompatActivity implements TaskListContract.View {

    private List<Task> tasks;
    private TaskAdapter adapter;
    private TaskListContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        presenter = new TaskListPresenter(this);

        tasks = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.task_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.loadAllTasks();
    }

    public void addTask(View view) {
        goToAddTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_task) {
            goToAddTask();
            return true;
        }

        if (item.getItemId() == R.id.action_preferences) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToAddTask() {
        Intent intent = new Intent(this, RegisterTaskView.class);
        startActivity(intent);
    }

    @Override
    public void listTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}