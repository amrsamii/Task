package com.example.task.view;

public interface BaseView {

    void dismissLoadingDialog();

    void showLoadingDialog();

    void onResult(String message);
}
