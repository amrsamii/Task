package com.example.task.view;

public interface IResetPassView extends BaseView {


    void populatePassErrorLayout(String error);
    void populateCodeErrorLayout(String error);
    void finishView();
}
