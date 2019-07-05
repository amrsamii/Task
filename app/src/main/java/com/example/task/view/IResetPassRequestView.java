package com.example.task.view;

import android.os.Bundle;

public interface IResetPassRequestView extends BaseView{


    void populateEmailErrorLayout(String error);

    void navigateToResetPassActivity(Bundle bundle);
}
