package com.example.task.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.ApiResponses.SignResponse;
import com.example.task.R;
import com.example.task.helpers.Common;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassActivity extends AppCompatActivity {

    @BindView(R.id.new_pass_edit_text) TextInputLayout passLayout;
    @BindView(R.id.code_edit_text) TextInputLayout codeLayout;
    @BindView(R.id.apply_button) MaterialRippleLayout applyButton;

    private AlertDialog dialog;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        Bundle bundle = getIntent().getExtras();
        code = bundle.getString("code");


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passLayout.getEditText().getText().toString();
                String enteredCode = codeLayout.getEditText().getText().toString();
                String name = bundle.getString("name");
                String resetMethod = bundle.getString("reset_method");

                sendNetworkRequest(enteredCode,name,pass,resetMethod);



            }
        });

    }

    private void sendNetworkRequest(String enteredCode, String name, String pass, String resetMethod){
        if(!validatePassword(pass) | !validateCode(enteredCode))
            return;

        dialog.show();
        APIClient client = ServiceGenerator.createService(APIClient.class);
        Call<SignResponse> call = client.resetPass(enteredCode,name,pass,resetMethod);
        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                dialog.dismiss();
                Toast.makeText(ResetPassActivity.this, "The password has been changed successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ResetPassActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passLayout.setError("Field can't be empty");
            return false;
        } else if (!Common.PASSWORD_PATTERN.matcher(password).matches()) {
            passLayout.setError("Password must be 6 characters and include small and capital letters");
            return false;
        } else {
            passLayout.setError(null);
            return true;
        }
    }

    private boolean validateCode(String enteredCode){
        if (TextUtils.isEmpty(enteredCode)) {
            codeLayout.setError("Field can't be empty");
            return false;
        }else if(!code.equals(enteredCode)){
            codeLayout.setError("Code you entered is wrong");
            return false;
        }else {
            codeLayout.setError(null);
            return true;
        }
    }
}
