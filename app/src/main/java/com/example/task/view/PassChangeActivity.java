package com.example.task.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.R;
import com.example.task.model.User;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.task.helpers.Common.PASSWORD_PATTERN;

public class PassChangeActivity extends AppCompatActivity {

    @BindView(R.id.old_pass_edit_text) TextInputLayout oldPassLayout;
    @BindView(R.id.new_pass_edit_text) TextInputLayout newPassLayout;
    @BindView(R.id.save_button) MaterialRippleLayout saveButton;



    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_change);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                String oldPass = oldPassLayout.getEditText().getText().toString();
                String newPass = newPassLayout.getEditText().getText().toString();

                sendNetworkRequest(user.getApi_token(),oldPass,newPass);

            }
        });
    }

    private void sendNetworkRequest(final String apiToken, String oldPass , String newPass) {
        if (!validateOldPassword(oldPass) | !validateNewPassword(newPass))
            return;
        dialog.show();
        APIClient client = ServiceGenerator.createService(APIClient.class);
        Call<ResponseBody> call = client.changePass(apiToken,oldPass,newPass);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(PassChangeActivity.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(PassChangeActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(PassChangeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

        private boolean validateOldPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            oldPassLayout.setError("Field can't be empty");
            return false;
        } else {
            oldPassLayout.setError(null);
            return true;
        }
    }


    private boolean validateNewPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            newPassLayout.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            newPassLayout.setError("Password must be 6 characters and include small and capital letters");
            return false;
        } else {
            newPassLayout.setError(null);
            return true;
        }
    }
}
