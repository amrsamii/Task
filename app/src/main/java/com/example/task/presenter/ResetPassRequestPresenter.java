package com.example.task.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.task.ApiResponses.ResetPassResponse;
import com.example.task.activities.ResetPassActivity;
import com.example.task.activities.ResetPassRequestActivity;
import com.example.task.model.IUSer;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.IResetPassRequestView;
import com.example.task.view.IUpdateProfileView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassRequestPresenter implements IResetPassRequestPresenter {

    private IResetPassRequestView resetPassRequestView;
    private Context context;

    public ResetPassRequestPresenter(Context context, IResetPassRequestView resetPassRequestView) {
        this.resetPassRequestView = resetPassRequestView;
        this.context = context;
    }
    @Override
    public void sendResetRequest(String name, String resetMethod) {
        if(!validateEmail(name))
            return;
        resetPassRequestView.showLoadingDialog();
        APIClient client = ServiceGenerator.createService(APIClient.class);
        Call<ResetPassResponse> call = client.requestPassReset(name,resetMethod);

        call.enqueue(new Callback<ResetPassResponse>() {
            @Override
            public void onResponse(Call<ResetPassResponse> call, Response<ResetPassResponse> response) {
                resetPassRequestView.dismissLoadingDialog();
                if(response.isSuccessful()){
                    String code = response.body().getCode();

                    resetPassRequestView.onResult("Code is sent successfully to your "+resetMethod);
                    Bundle bundle = new Bundle();
                    bundle.putString("code",code);
                    bundle.putString("name",name);
                    bundle.putString("reset_method",resetMethod);
                    resetPassRequestView.navigateToResetPassActivity(bundle);

                }else{
                    resetPassRequestView.onResult("this "+ resetMethod+ " doesn't exist");
                }
            }

            @Override
            public void onFailure(Call<ResetPassResponse> call, Throwable t) {
                resetPassRequestView.dismissLoadingDialog();
                resetPassRequestView.onResult("check internet connection");

            }
        });

    }




    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            resetPassRequestView.populateEmailErrorLayout("Field can't be empty");
            return false;
        }  else {
            resetPassRequestView.populateEmailErrorLayout(null);
            return true;
        }
    }
}
