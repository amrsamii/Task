package com.example.task.view;

public interface IPhoneChangeView extends BaseView {

    void populatePhoneErrorLayout(String error);

    void finishView();

    void populatePhoneLayout(String data);
}
