package com.example.task.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.ApiResponses.UpdatePhoneResponse;
import com.example.task.R;
import com.example.task.model.IUSer;
import com.example.task.model.User;
import com.example.task.presenter.PhoneChangePresenter;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.IPhoneChangeView;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneChangeActivity extends AppCompatActivity implements IPhoneChangeView {

    @BindView(R.id.phone_edit_text)
    TextInputLayout phoneLayout;
    @BindView(R.id.save_button)
    MaterialRippleLayout saveButton;


    private AlertDialog dialog;
    private PhoneChangePresenter phoneChangePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_change);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        phoneChangePresenter = new PhoneChangePresenter(this, this);
        phoneChangePresenter.initPhone();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = phoneLayout.getEditText().getText().toString();
                phoneChangePresenter.changePhone(phone);

            }
        });

    }


    @Override
    public void populatePhoneErrorLayout(String error) {
    phoneLayout.setError(error);
    }

    @Override
    public void finishView() {
        finish();

    }

    @Override
    public void populatePhoneLayout(String data) {
        phoneLayout.getEditText().setText(data);

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
