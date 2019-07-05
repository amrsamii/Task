package com.example.task.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.R;
import com.example.task.presenter.SigninPresenter;
import com.example.task.view.ISigninView;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class SigninActivity extends AppCompatActivity implements ISigninView {

    @BindView(R.id.email_edit_text)
    TextInputLayout emailTextInputLayout;
    @BindView(R.id.pass_edit_text)
    TextInputLayout passTextInputLayout;
    @BindView(R.id.signin_button)
    MaterialRippleLayout signinButton;
    @BindView(R.id.forget_tv)
    TextView forgetTextView;
    private AlertDialog dialog;

    private SigninPresenter signinPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        signinPresenter = new SigninPresenter(this, this);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPhone = emailTextInputLayout.getEditText().getText().toString();
                String pass = passTextInputLayout.getEditText().getText().toString();

                signinPresenter.signin(emailPhone,pass);


            }
        });

        forgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, ResetPassRequestActivity.class));

            }
        });
    }


    @Override
    public void onResult(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
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
