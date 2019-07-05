package com.example.task.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.ApiResponses.ResetPassResponse;
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

public class ResetPassRequestActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_text) TextInputLayout emailLayout;
    @BindView(R.id.send_button) MaterialRippleLayout sendButton;

    private AlertDialog dialog; // dialog for loading



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_request);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

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
                sendNetworkRequest(emailOrPhone,resetMethod);
            }
        });



    }

    private void sendNetworkRequest(String name, String resetMethod){

        if(!validateEmail(name))
            return;

        dialog.show();

        APIClient client = ServiceGenerator.createService(APIClient.class);
        Call<ResetPassResponse> call = client.requestPassReset(name,resetMethod);
        call.enqueue(new Callback<ResetPassResponse>() {
            @Override
            public void onResponse(Call<ResetPassResponse> call, Response<ResetPassResponse> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    String code = response.body().getCode();

                    Toast.makeText(ResetPassRequestActivity.this, "Code is sent successfully to your "+resetMethod, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("code",code);
                    bundle.putString("name",name);
                    bundle.putString("reset_method",resetMethod);
                    Intent intent = new Intent(ResetPassRequestActivity.this,ResetPassActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);


                }else{
                        Toast.makeText(ResetPassRequestActivity.this, "this "+ resetMethod+ " doesn't exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResetPassResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ResetPassRequestActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Field can't be empty");
            return false;
        }  else {
            emailLayout.setError(null);
            return true;
        }
    }

}
