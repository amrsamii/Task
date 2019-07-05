package com.example.task.view;

public interface ISigninView extends BaseView{

    void navigateToMainActivity();

    void populateEmailErrorLayout(String error);

    void populatePassErrorLayout(String error);
}

