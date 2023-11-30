package com.svalero.todolist.contract;

import com.svalero.todolist.domain.Task;

public interface RegisterTaskContract {

    interface Model {
        interface OnRegisterTasksListener {
            void onRegisterTaskSuccess();
            void onRegisterTasksError(String message);
        }
        void registerTask(OnRegisterTasksListener listener, Task task);
    }

    interface View {
        void showMessage(int stringId);
        void showMessage(String message);
    }

    interface Presenter {
        void registerTask(Task task);
    }
}
