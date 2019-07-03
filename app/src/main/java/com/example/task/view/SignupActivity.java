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

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_text) TextInputLayout emailTextInputLayout;
    @BindView(R.id.user_edit_text) TextInputLayout userTextInputLayout;
    @BindView(R.id.phone_edit_text) TextInputLayout phoneTextInputLayout;
    @BindView(R.id.pass_edit_text) TextInputLayout passTextInputLayout;
    @BindView(R.id.signup_button) MaterialRippleLayout signupButton;

    private AlertDialog dialog;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //  "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters  // after comma is upper limit e.g 20 char
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextInputLayout.getEditText().getText().toString();
                String phone = phoneTextInputLayout.getEditText().getText().toString();
                String name = userTextInputLayout.getEditText().getText().toString();
                String pass = passTextInputLayout.getEditText().getText().toString();
                sendNetworkRequest(name, phone, pass, email);
            }
        });

    }

    public void sendNetworkRequest(String name, String phone, String password, String email) {

        if (!validateEmail(email) | !validatePassword(password) | !validateUserName(name) | !validatePhone(phone)) {
            return;
        }

        dialog.show();

        APIClient client = ServiceGenerator.createService(APIClient.class);

        Call<SignResponse> call = client.signup(name,phone,password,email);
        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    SignResponse signResponse= response.body();
                    User user = signResponse.getUser();
                    SharedPrefManager.getInstance(SignupActivity.this)
                            .saveUser(user);
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else{
                    ErrorResponse apiError = ErrorUtils.parseError(response);
                    Error[] errors = apiError.getErrors();
                    String text="";

                    for(int i=0;i<errors.length;i++) {
                        text+= errors[i].getMessage()+"\n";
                    }
                    Toast.makeText(SignupActivity.this, text, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            emailTextInputLayout.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInputLayout.setError("Please enter a valid email address");
            return false;
        } else {
            emailTextInputLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passTextInputLayout.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passTextInputLayout.setError("Password must be 6 charachters and include small and capital letters");
            return false;
        } else {
            passTextInputLayout.setError(null);
            return true;
        }
    }

    private boolean validateUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            userTextInputLayout.setError("Field can't be empty");
            return false;
        } else if (userName.length() < 3) {
            userTextInputLayout.setError("name must be at least 3 characters");
            return false;
        } else {
            userTextInputLayout.setError(null);
            return true;
        }

    }

    private boolean validatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            phoneTextInputLayout.setError("Field can't be empty");
            return false;
        } else {
            phoneTextInputLayout.setError(null);
            return true;
        }
    }
}
