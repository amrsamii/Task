package com.example.task.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.ApiResponses.ErrorResponse;
import com.example.task.ApiResponses.SignResponse;
import com.example.task.R;
import com.example.task.helpers.ErrorUtils;
import com.example.task.model.Error;
import com.example.task.model.User;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class UpdateProfile extends AppCompatActivity {

    @BindView(R.id.email_edit_text) TextInputLayout emailLayout;
    @BindView(R.id.user_edit_text) TextInputLayout nameLayout;
    @BindView(R.id.save_button) MaterialRippleLayout saveButton;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        User user = SharedPrefManager.getInstance(this).getUser();
        emailLayout.getEditText().setText(user.getEmail());
        nameLayout.getEditText().setText(user.getName());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String token = user.getApi_token();
                String name = nameLayout.getEditText().getText().toString();
                String email = emailLayout.getEditText().getText().toString();

                sendNetworkRequest(name,email,token);

            }
        });
    }


    private void sendNetworkRequest(String name,String email,String token)
    {

        if(!validateEmail(email) | !validateUserName(name))
            return;

        dialog.show();
        APIClient client = ServiceGenerator.createService(APIClient.class);
        Call<SignResponse> call = client.updateProfile(token,name,email,"");
        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    User user = response.body().getUser();
                    SharedPrefManager.getInstance(UpdateProfile.this).saveUser(user);
                    Toast.makeText(UpdateProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                   finish();
                }else{
                    ErrorResponse apiError = ErrorUtils.parseError(response);
                    Error[] errors = apiError.getErrors();
                    String text="";

                    for(int i=0;i<errors.length;i++) {
                        text+= errors[i].getMessage()+"\n";
                    }
                    Toast.makeText(UpdateProfile.this, text, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {
                Toast.makeText(UpdateProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email address");
            return false;
        } else {
            emailLayout.setError(null);
            return true;
        }
    }

    private boolean validateUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            nameLayout.setError("Field can't be empty");
            return false;
        } else if (userName.length() < 3) {
            nameLayout.setError("name must be at least 3 characters");
            return false;
        } else {
            nameLayout.setError(null);
            return true;
        }

    }
}
