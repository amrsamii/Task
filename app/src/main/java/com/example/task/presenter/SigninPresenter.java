package com.example.task.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.task.ApiResponses.SignResponse;
import com.example.task.model.IUSer;
import com.example.task.model.User;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.ISigninView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninPresenter implements ISigninPresenter {


   private ISigninView signinView;
   private IUSer user;
   private Context context;
    public  SigninPresenter(Context context,ISigninView signinView){
        this.signinView = signinView;
        this.context = context;
    }


    @Override
    public void signin(String emailPhone, String pass) {

        if (!validateEmailPhone(emailPhone) | !validatePassword(pass))
            return;

        signinView.showLoadingDialog();

        APIClient client = ServiceGenerator.createService(APIClient.class);

        Call<SignResponse> call = client.signin(emailPhone,pass);

        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {

                signinView.dismissLoadingDialog();

                if(response.isSuccessful()){
                    SignResponse signResponse= response.body();
                     user = signResponse.getUser();
                    SharedPrefManager.getInstance(context).saveUser(user);
                    signinView.navigateToMainActivity();
                }else {
                    signinView.onResult("Wrong email or password");
                }
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {
                signinView.dismissLoadingDialog();
                signinView.onResult("check internet connection");


            }
        });

    }






    private boolean validateEmailPhone(String email) {
        if(TextUtils.isEmpty(email)) {
            signinView.populateEmailErrorLayout("Field can't be empty");
            return false;
        }
        else {
            signinView.populateEmailErrorLayout(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if(TextUtils.isEmpty(password)) {
            signinView.populatePassErrorLayout("Field can't be empty");
            return false;
        }
        else {
            signinView.populatePassErrorLayout(null);
            return true;
        }
    }
}
