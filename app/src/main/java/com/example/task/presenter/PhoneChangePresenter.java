package com.example.task.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.task.ApiResponses.UpdatePhoneResponse;
import com.example.task.model.IUSer;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.IPhoneChangeView;
import com.example.task.view.IUpdateProfileView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneChangePresenter implements IPhoneChangePresenter {

    private IPhoneChangeView phoneChangeView;
    private Context context;
    private IUSer user = SharedPrefManager.getInstance(context).getUser();  // interface between presenter and model

    public PhoneChangePresenter(Context context, IPhoneChangeView phoneChangeView) {
        this.phoneChangeView = phoneChangeView;
        this.context = context;
    }
    @Override
    public void changePhone(String phone) {

        if (!validatePhone(phone))
            return;
        phoneChangeView.showLoadingDialog();

        final APIClient client = ServiceGenerator.createService(APIClient.class);

        Call<UpdatePhoneResponse> call = client.requestUpdatePhone(user.getApi_token(), phone);

        call.enqueue(new Callback<UpdatePhoneResponse>() {
            @Override
            public void onResponse(Call<UpdatePhoneResponse> call, Response<UpdatePhoneResponse> response) {
                if (response.isSuccessful()) {
                    String code = response.body().getCode();
                    Call<ResponseBody> call2 = client.updatePhone(user.getApi_token(), phone, code);
                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            phoneChangeView.dismissLoadingDialog();
                            if (response.isSuccessful()) {
                                phoneChangeView.onResult("Phone updated successfully");
                                user.setPhone(phone);
                                SharedPrefManager.getInstance(context).saveUser(user);
                                phoneChangeView.finishView();
                            } else {
                                phoneChangeView.onResult("The Phone has already been taken");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            phoneChangeView.dismissLoadingDialog();
                            phoneChangeView.onResult("Check internet connection");

                        }
                    });
                }else{
                    phoneChangeView.dismissLoadingDialog();
                    phoneChangeView.onResult("The Phone has already been taken");
                }
            }
            @Override
            public void onFailure(Call<UpdatePhoneResponse> call, Throwable t) {
                phoneChangeView.dismissLoadingDialog();
                phoneChangeView.onResult("Check internet connection");
            }
        });

    }

    @Override
    public void initPhone() {
        phoneChangeView.populatePhoneLayout(user.getPhone());

    }



    private boolean validatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            phoneChangeView.populatePhoneErrorLayout("Field can't be empty");
            return false;
        } else {
            phoneChangeView.populatePhoneErrorLayout(null);
            return true;
        }
    }
}
