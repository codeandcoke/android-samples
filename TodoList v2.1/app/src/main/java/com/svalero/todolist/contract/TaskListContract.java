package com.svalero.todolist.contract;

import com.svalero.todolist.domain.Task;

import java.util.List;

public interface TaskListContract {

    interface Model {
        interface OnLoadTasksListener {
            void onLoadTaskSuccess(List<Task> tasks);
            void onLoadTasksError(String message);
        }
        void loadAllTasks(OnLoadTasksListener listener, boolean seeDoneTasks);
    }

    interface View {
        void listTasks(List<Task> tasks);
        void showMessage(String message);
    }

    interface Presenter {
        void loadAllTasks();
    }
}
