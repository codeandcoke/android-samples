package com.svalero.todolist.model;

import android.util.Log;

import com.svalero.todolist.api.ProductApi;
import com.svalero.todolist.api.ProductApiInterface;
import com.svalero.todolist.contract.RegisterTaskContract;
import com.svalero.todolist.domain.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterTaskModel implements RegisterTaskContract.Model {

    @Override
    public void registerTask(OnRegisterTasksListener listener, Task task) {
        ProductApiInterface api = ProductApi.buildInstance();
        Call<Task> addTaskCall = api.addTask(task);
        addTaskCall.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                // TODO Faltaría validar casos de respuesta con errores (400, 500, . . .)
                listener.onRegisterTaskSuccess();
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("addTask", t.getMessage());
                listener.onRegisterTasksError("No se ha podido conectar con el servidor");
            }
        });
    }
}
