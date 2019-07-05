package com.example.task.view;

public interface IPassChangeView extends BaseView {

    void populateOldPassErrorLayout(String error);

    void populateNewPassErrorLayout(String error);

    void finishView();
}
