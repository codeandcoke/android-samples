package com.svalero.todolist.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.svalero.todolist.contract.TaskListContract;
import com.svalero.todolist.domain.Task;
import com.svalero.todolist.model.TaskListModel;

import java.util.List;

public class TaskListPresenter implements TaskListContract.Presenter, TaskListContract.Model.OnLoadTasksListener {

    private TaskListContract.View view;
    private TaskListContract.Model model;

    public TaskListPresenter(TaskListContract.View view) {
        this.view = view;
        model = new TaskListModel();
    }

    @Override
    public void loadAllTasks() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences((Context) view);
        boolean seeDoneTasks = preferences.getBoolean("see_done_tasks", false);
        model.loadAllTasks(this, seeDoneTasks);
    }

    @Override
    public void onLoadTaskSuccess(List<Task> tasks) {
        view.listTasks(tasks);
    }

    @Override
    public void onLoadTasksError(String message) {
        view.showMessage(message);
    }
}
