package com.example.task.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.task.ApiResponses.SignResponse;
import com.example.task.helpers.Common;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.view.IResetPassRequestView;
import com.example.task.view.IResetPassView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassPresenter implements IResetPassPresenter {

    private IResetPassView resetPassView;
    private String code;

    public ResetPassPresenter( IResetPassView resetPassView, String code) {
        this.resetPassView = resetPassView;
        this.code = code;
    }
    @Override
    public void resetPass(String enteredCode, String name, String pass, String resetMethod) {

        if(!validatePassword(pass) | !validateCode(enteredCode))
            return;

        resetPassView.showLoadingDialog();

        APIClient client = ServiceGenerator.createService(APIClient.class);
        Call<SignResponse> call = client.resetPass(enteredCode,name,pass,resetMethod);
        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                resetPassView.dismissLoadingDialog();
                resetPassView.onResult("The password has been changed successfully");
                resetPassView.finishView();
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {
                resetPassView.dismissLoadingDialog();
                resetPassView.onResult("check internet connection");

            }
        });

    }




    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            resetPassView.populatePassErrorLayout("Field can't be empty");
            return false;
        } else if (!Common.PASSWORD_PATTERN.matcher(password).matches()) {
            resetPassView.populatePassErrorLayout("Password must be 6 characters and include small and capital letters");
            return false;
        } else {
            resetPassView.populatePassErrorLayout(null);
            return true;
        }
    }

    private boolean validateCode(String enteredCode){
        if (TextUtils.isEmpty(enteredCode)) {
            resetPassView.populateCodeErrorLayout("Field can't be empty");
            return false;
        }else if(!code.equals(enteredCode)){
            resetPassView.populateCodeErrorLayout("Code you entered is wrong");
            return false;
        }else {
            resetPassView.populateCodeErrorLayout(null);
            return true;
        }
    }
}
