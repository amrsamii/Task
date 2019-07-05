package com.example.task.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.R;
import com.example.task.model.IUSer;
import com.example.task.model.User;
import com.example.task.presenter.PassChangePresenter;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.IPassChangeView;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.task.helpers.Common.PASSWORD_PATTERN;

public class PassChangeActivity extends AppCompatActivity implements IPassChangeView {

    @BindView(R.id.old_pass_edit_text) TextInputLayout oldPassLayout;
    @BindView(R.id.new_pass_edit_text) TextInputLayout newPassLayout;
    @BindView(R.id.save_button) MaterialRippleLayout saveButton;



    private AlertDialog dialog;
    private PassChangePresenter passChangePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_change);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();
        passChangePresenter = new PassChangePresenter(this,this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPass = oldPassLayout.getEditText().getText().toString();
                String newPass = newPassLayout.getEditText().getText().toString();
                passChangePresenter.changePassword(oldPass,newPass);


            }
        });
    }


    @Override
    public void populateOldPassErrorLayout(String error) {
        oldPassLayout.setError(error);

    }

    @Override
    public void populateNewPassErrorLayout(String error) {
        newPassLayout.setError(error);
    }

    @Override
    public void finishView() {
        finish();
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
