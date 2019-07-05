package com.example.task.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.ApiResponses.UpdatePhoneResponse;
import com.example.task.R;
import com.example.task.model.User;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class PhoneChangeActivity extends AppCompatActivity {

    @BindView(R.id.phone_edit_text)
    TextInputLayout phoneLayout;
    @BindView(R.id.save_button) MaterialRippleLayout saveButton;


    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
    User user = sharedPrefManager.getUser();

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_change);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        phoneLayout.getEditText().setText(user.getPhone());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Phone = phoneLayout.getEditText().getText().toString();

                sendNetworkRequest(user.getApi_token(), Phone);
            }
        });

    }

    private void sendNetworkRequest(final String apiToken, final String phone) {
        if (!validatePhone(phone))
            return;
        dialog.show();
        final APIClient client = ServiceGenerator.createService(APIClient.class);

        Call<UpdatePhoneResponse> call = client.requestUpdatePhone(apiToken, phone);
        call.enqueue(new Callback<UpdatePhoneResponse>() {
            @Override
            public void onResponse(Call<UpdatePhoneResponse> call, Response<UpdatePhoneResponse> response) {
                if (response.isSuccessful()) {
                    String code = response.body().getCode();
                    Call<ResponseBody> call2 = client.updatePhone(apiToken, phone, code);
                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()){
                                Toast.makeText(PhoneChangeActivity.this, "Phone updated successfully", Toast.LENGTH_SHORT).show();
                                user.setPhone(phone);
                               sharedPrefManager.saveUser(user);

                                finish();
                            }else{
                                Toast.makeText(PhoneChangeActivity.this, "The Phone has already been taken", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(PhoneChangeActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    dialog.dismiss();
                    Toast.makeText(PhoneChangeActivity.this, "The Phone has already been taken", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdatePhoneResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(PhoneChangeActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            phoneLayout.setError("Field can't be empty");
            return false;
        } else {
            phoneLayout.setError(null);
            return true;
        }
    }
}
