package com.example.task.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.ApiResponses.SignResponse;
import com.example.task.R;
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

public class SigninActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_text) TextInputLayout emailTextInputLayout;
    @BindView(R.id.pass_edit_text) TextInputLayout passTextInputLayout;
    @BindView(R.id.signin_button) MaterialRippleLayout signinButton;

    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPhone = emailTextInputLayout.getEditText().getText().toString();
                String pass = passTextInputLayout.getEditText().getText().toString();

                sendNetworkRequest(emailPhone,pass);
            }
        });
    }


    private void sendNetworkRequest(String emailPhone , String pass) {

        if (!validateEmailPhone(emailPhone) | !validatePassword(pass))
            return;
        dialog.show();

        APIClient client = ServiceGenerator.createService(APIClient.class);

        Call<SignResponse> call = client.signin(emailPhone,pass);
        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    SignResponse signResponse= response.body();
                    User user = signResponse.getUser();
                    SharedPrefManager.getInstance(SigninActivity.this)
                            .saveUser(user);
                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(SigninActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {
                Toast.makeText(SigninActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    private boolean validateEmailPhone(String email)
    {
        if(TextUtils.isEmpty(email))
        {
            emailTextInputLayout.setError("Field can't be empty");
            return false;
        }
        else
        {
            emailTextInputLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password)
    {
        if(TextUtils.isEmpty(password))
        {
            passTextInputLayout.setError("Field can't be empty");
            return false;
        }
        else
        {
            passTextInputLayout.setError(null);
            return true;
        }
    }
}
