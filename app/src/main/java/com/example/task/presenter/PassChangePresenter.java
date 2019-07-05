package com.example.task.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.task.model.IUSer;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.IPassChangeView;
import com.example.task.view.IPhoneChangeView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.task.helpers.Common.PASSWORD_PATTERN;

public class PassChangePresenter implements IPassChangePresenter {

    private IPassChangeView passChangeView;
    private Context context;
    private IUSer user = SharedPrefManager.getInstance(context).getUser();  // interface between presenter and model

    public PassChangePresenter(Context context, IPassChangeView passChangeView) {
        this.passChangeView = passChangeView;
        this.context = context;
    }
    @Override
    public void changePassword(String oldPass, String newPass) {
        if (!validateOldPassword(oldPass) | !validateNewPassword(newPass))
            return;
        passChangeView.showLoadingDialog();

        APIClient client = ServiceGenerator.createService(APIClient.class);
        Call<ResponseBody> call = client.changePass(user.getApi_token(),oldPass,newPass);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                passChangeView.dismissLoadingDialog();
                if(response.isSuccessful()){
                    passChangeView.onResult("Password Updated Successfully");
                    passChangeView.finishView();
                }else{
                    passChangeView.onResult("Incorrect Password");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                passChangeView.dismissLoadingDialog();
                passChangeView.onResult("Check internet connection");

            }
        });

    }







    private boolean validateOldPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passChangeView.populateOldPassErrorLayout("Field can't be empty");
            return false;
        } else {
            passChangeView.populateOldPassErrorLayout(null);
            return true;
        }
    }


    private boolean validateNewPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passChangeView.populateNewPassErrorLayout("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passChangeView.populateNewPassErrorLayout("Password must be 6 characters and include small and capital letters");
            return false;
        } else {
            passChangeView.populateNewPassErrorLayout(null);
            return true;
        }
    }
}
