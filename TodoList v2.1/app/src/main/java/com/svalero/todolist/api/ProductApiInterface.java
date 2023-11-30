package com.svalero.todolist.api;

import com.svalero.todolist.domain.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductApiInterface {

    @GET("tasks")
    Call<List<Task>> getTasks();

    @POST("tasks")
    Call<Task> addTask(@Body Task task);

    @DELETE("task/{taskId}")
    Call<Void> deleteTask(@Path("taskId") long taskId);
}
