package com.example.task.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.task.ApiResponses.ErrorResponse;
import com.example.task.ApiResponses.SignResponse;
import com.example.task.helpers.Common;
import com.example.task.helpers.ErrorUtils;
import com.example.task.model.Error;
import com.example.task.model.IError;
import com.example.task.model.IUSer;
import com.example.task.model.User;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.ISignupView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupPresenter implements ISignupPresenter {

    private ISignupView signupView;
    private IUSer user;
    private Context context;

    public SignupPresenter(Context context, ISignupView signupView) {
        this.signupView = signupView;
        this.context = context;
    }


    @Override
    public void signup(String name, String phone, String password, String email) {

        if (!validateEmail(email) | !validatePassword(password) | !validateUserName(name) | !validatePhone(phone)) {
            return;
        }
        signupView.showLoadingDialog();

        APIClient client = ServiceGenerator.createService(APIClient.class);

        Call<SignResponse> call = client.signup(name, phone, password, email);
        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                signupView.dismissLoadingDialog();

                if (response.isSuccessful()) {
                    SignResponse signResponse = response.body();
                     user = signResponse.getUser();
                     SharedPrefManager.getInstance(context).saveUser(user);
                    signupView.navigateToMainActivity();

                } else {

                    ErrorResponse apiError = ErrorUtils.parseError(response);
                    IError[] errors = apiError.getErrors();
                    String text = "";

                    for (int i = 0; i < errors.length; i++) {
                        text += errors[i].getMessage() + "\n";
                    }
                    signupView.onResult(text);

                }
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {
                signupView.dismissLoadingDialog();
                signupView.onResult("check internet connection");
            }
        });


    }


    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            signupView.populateEmailErrorLayout("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupView.populateEmailErrorLayout("Please enter a valid email address");
            return false;
        } else {
            signupView.populateEmailErrorLayout(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            signupView.populatePassErrorLayout("Field can't be empty");
            return false;
        } else if (!Common.PASSWORD_PATTERN.matcher(password).matches()) {
            signupView.populatePassErrorLayout("Password must be 6 characters and include small and capital letters");
            return false;
        } else {
            signupView.populatePassErrorLayout(null);
            return true;
        }
    }

    private boolean validateUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            signupView.populateNameErrorLayout("Field can't be empty");
            return false;
        } else if (userName.length() < 3) {
            signupView.populateNameErrorLayout("name must be at least 3 characters");
            return false;
        } else {
            signupView.populateNameErrorLayout(null);
            return true;
        }

    }

    private boolean validatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            signupView.populatePhoneErrorLayout("Field can't be empty");
            return false;
        } else {
            signupView.populatePhoneErrorLayout(null);
            return true;
        }
    }
}
