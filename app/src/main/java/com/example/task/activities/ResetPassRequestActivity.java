package com.example.task.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.ApiResponses.ResetPassResponse;
import com.example.task.R;
import com.example.task.presenter.ResetPassRequestPresenter;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.view.IResetPassRequestView;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassRequestActivity extends AppCompatActivity implements IResetPassRequestView {

    @BindView(R.id.email_edit_text) TextInputLayout emailLayout;
    @BindView(R.id.send_button) MaterialRippleLayout sendButton;

    private AlertDialog dialog; // dialog for loading
    private ResetPassRequestPresenter resetPassRequestPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_request);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();
        resetPassRequestPresenter = new ResetPassRequestPresenter(this,this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resetMethod;
                String emailOrPhone = emailLayout.getEditText().getText().toString();
                if(TextUtils.isDigitsOnly(emailOrPhone)){ // user entered his phone number
                     resetMethod = "phone";

                }else{       // user entered his email
                    resetMethod = "email";
                }

                resetPassRequestPresenter.sendResetRequest(emailOrPhone,resetMethod);
            }
        });



    }


    @Override
    public void populateEmailErrorLayout(String error) {
        emailLayout.setError(error);

    }

    @Override
    public void navigateToResetPassActivity(Bundle bundle) {
        Intent intent = new Intent(ResetPassRequestActivity.this, ResetPassActivity.class);
        intent.putExtras(bundle);
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
    public void onResult(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
