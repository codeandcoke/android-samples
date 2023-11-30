package com.svalero.todolist.model;

import android.util.Log;

import com.svalero.todolist.api.ProductApi;
import com.svalero.todolist.api.ProductApiInterface;
import com.svalero.todolist.contract.TaskListContract;
import com.svalero.todolist.domain.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskListModel implements TaskListContract.Model {

    @Override
    public void loadAllTasks(OnLoadTasksListener listener, boolean seeDoneTasks) {
        ProductApiInterface api = ProductApi.buildInstance();
        Call<List<Task>> getTasksCall = api.getTasks();
        getTasksCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                Log.e("getTasks", response.message());
                List<Task> tasks = response.body();
                if (!seeDoneTasks)
                    tasks.removeIf(task -> task.isDone());
                listener.onLoadTaskSuccess(tasks);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("getTasks", t.getMessage());
                listener.onLoadTasksError("Se ha producido un error al conectar con el servidor");
            }
        });
    }
}
