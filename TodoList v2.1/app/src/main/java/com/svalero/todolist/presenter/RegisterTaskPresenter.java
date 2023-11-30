package com.svalero.todolist.presenter;

import com.svalero.todolist.R;
import com.svalero.todolist.contract.RegisterTaskContract;
import com.svalero.todolist.domain.Task;
import com.svalero.todolist.model.RegisterTaskModel;

public class RegisterTaskPresenter implements RegisterTaskContract.Presenter, RegisterTaskContract.Model.OnRegisterTasksListener {

    private RegisterTaskContract.Model model;
    private RegisterTaskContract.View view;

    public RegisterTaskPresenter(RegisterTaskContract.View view) {
        this.view = view;
        model = new RegisterTaskModel();
    }

    @Override
    public void registerTask(Task task) {
        model.registerTask(this, task);
    }

    @Override
    public void onRegisterTaskSuccess() {
        view.showMessage(R.string.la_tarea_se_ha_registrado_correctamente);
    }

    @Override
    public void onRegisterTasksError(String message) {
        view.showMessage(message);
    }
}
