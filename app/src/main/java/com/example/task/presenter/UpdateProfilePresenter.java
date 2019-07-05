package com.example.task.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.task.ApiResponses.ErrorResponse;
import com.example.task.ApiResponses.SignResponse;
import com.example.task.helpers.ErrorUtils;
import com.example.task.model.Error;
import com.example.task.model.IError;
import com.example.task.model.IUSer;
import com.example.task.model.User;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.ISigninView;
import com.example.task.view.ISignupView;
import com.example.task.view.IUpdateProfileView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfilePresenter implements IUpdateProfilePresenter {

    private IUpdateProfileView updateProfileView;
    private Context context;
    private IUSer user = SharedPrefManager.getInstance(context).getUser();  // interface between presenter and model

    public UpdateProfilePresenter(Context context, IUpdateProfileView updateProfileView) {
        this.updateProfileView = updateProfileView;
        this.context = context;
    }
    @Override
    public void updateProfile(String name, String email) {

        if(!validateEmail(email) | !validateUserName(name))
            return;

        updateProfileView.showLoadingDialog();



        APIClient client = ServiceGenerator.createService(APIClient.class);
        Call<SignResponse> call = client.updateProfile(user.getApi_token(),name,email,user.getImage());
        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                updateProfileView.dismissLoadingDialog();
                if(response.isSuccessful()){
                    User updatedUser = response.body().getUser();  // updated user
                    SharedPrefManager.getInstance(context).saveUser(updatedUser);
                    updateProfileView.onResult("Updated successfully");
                    updateProfileView.finishView();
                }else{

                    ErrorResponse apiError = ErrorUtils.parseError(response);
                    IError[] errors = apiError.getErrors();
                    String text="";

                    for(int i=0;i<errors.length;i++) {
                        text+= errors[i].getMessage()+"\n";
                    }
                    updateProfileView.onResult(text);
                }
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {
                updateProfileView.onResult("check internet connection");
                updateProfileView.finishView();

            }
        });





    }

    @Override
    public void initProfile() {

        updateProfileView.populateEmailLayout(user.getEmail());
        updateProfileView.populateNameLayout(user.getName());

    }


    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            updateProfileView.populateEmailErrorLayout("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            updateProfileView.populateEmailErrorLayout("Please enter a valid email address");
            return false;
        } else {
            updateProfileView.populateEmailErrorLayout(null);
            return true;
        }
    }

    private boolean validateUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            updateProfileView.populateNameErrorLayout("Field can't be empty");
            return false;
        } else if (userName.length() < 3) {
            updateProfileView.populateNameErrorLayout("name must be at least 3 characters");
            return false;
        } else {
            updateProfileView.populateNameErrorLayout(null);
            return true;
        }

    }
}
