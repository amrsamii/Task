package com.example.task.view;

public interface IUpdateProfileView extends BaseView{

    void populateNameErrorLayout(String error);

    void populateEmailErrorLayout(String error);

    void populateEmailLayout(String data);

    void populateNameLayout(String data);

    void finishView();
}
