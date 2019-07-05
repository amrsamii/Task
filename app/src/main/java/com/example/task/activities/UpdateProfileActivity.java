package com.example.task.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.example.task.model.IUSer;
import com.example.task.model.User;
import com.example.task.presenter.SigninPresenter;
import com.example.task.presenter.UpdateProfilePresenter;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.IUpdateProfileView;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity implements IUpdateProfileView {

    @BindView(R.id.email_edit_text)
    TextInputLayout emailLayout;
    @BindView(R.id.user_edit_text)
    TextInputLayout nameLayout;
    @BindView(R.id.save_button)
    MaterialRippleLayout saveButton;

    private AlertDialog dialog;
    private UpdateProfilePresenter updateProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        updateProfilePresenter = new UpdateProfilePresenter(this, this);


        updateProfilePresenter.initProfile();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameLayout.getEditText().getText().toString();
                String email = emailLayout.getEditText().getText().toString();
                updateProfilePresenter.updateProfile(name, email);

            }
        });


    }


    @Override
    public void populateNameErrorLayout(String error) {
        nameLayout.setError(error);
    }

    @Override
    public void populateEmailErrorLayout(String error) {
        emailLayout.setError(error);

    }

    @Override
    public void populateEmailLayout(String data) {
        emailLayout.getEditText().setText(data);

    }

    @Override
    public void populateNameLayout(String data) {
        nameLayout.getEditText().setText(data);
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
