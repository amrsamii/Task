package com.example.task.activities;

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
import com.example.task.helpers.Common;
import com.example.task.helpers.ErrorUtils;
import com.example.task.model.Error;
import com.example.task.model.User;
import com.example.task.presenter.SigninPresenter;
import com.example.task.presenter.SignupPresenter;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.ISignupView;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements ISignupView {

    @BindView(R.id.email_edit_text)
    TextInputLayout emailTextInputLayout;
    @BindView(R.id.user_edit_text)
    TextInputLayout userTextInputLayout;
    @BindView(R.id.phone_edit_text)
    TextInputLayout phoneTextInputLayout;
    @BindView(R.id.pass_edit_text)
    TextInputLayout passTextInputLayout;
    @BindView(R.id.signup_button)
    MaterialRippleLayout signupButton;

    private AlertDialog dialog;
    private SignupPresenter signupPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();
        signupPresenter = new SignupPresenter(this, this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextInputLayout.getEditText().getText().toString();
                String phone = phoneTextInputLayout.getEditText().getText().toString();
                String name = userTextInputLayout.getEditText().getText().toString();
                String pass = passTextInputLayout.getEditText().getText().toString();
                signupPresenter.signup(name, phone, pass, email);
            }
        });

    }


    @Override
    public void populatePhoneErrorLayout(String error) {
        phoneTextInputLayout.setError(error);
    }

    @Override
    public void populateNameErrorLayout(String error) {
        userTextInputLayout.setError(error);

    }

    @Override
    public void onResult(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void dismissLoadingDialog() {
        dialog.dismiss();
    }

    @Override
    public void showLoadingDialog() {
        dialog.show();

    }

    @Override
    public void populateEmailErrorLayout(String error) {
        emailTextInputLayout.setError(error);

    }

    @Override
    public void populatePassErrorLayout(String error) {
        passTextInputLayout.setError(error);

    }
}
