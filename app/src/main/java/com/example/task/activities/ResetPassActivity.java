package com.example.task.activities;

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
import com.example.task.presenter.ResetPassPresenter;
import com.example.task.service.APIClient;
import com.example.task.service.ServiceGenerator;
import com.example.task.view.IResetPassView;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassActivity extends AppCompatActivity implements IResetPassView {

    @BindView(R.id.new_pass_edit_text) TextInputLayout passLayout;
    @BindView(R.id.code_edit_text) TextInputLayout codeLayout;
    @BindView(R.id.apply_button) MaterialRippleLayout applyButton;

    private AlertDialog dialog;
    private String code;
    private ResetPassPresenter resetPassPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        ButterKnife.bind(this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        Bundle bundle = getIntent().getExtras();
        code = bundle.getString("code");
        resetPassPresenter = new ResetPassPresenter(this,code);


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passLayout.getEditText().getText().toString();
                String enteredCode = codeLayout.getEditText().getText().toString();
                String name = bundle.getString("name");
                String resetMethod = bundle.getString("reset_method");

               resetPassPresenter.resetPass(enteredCode,name,pass,resetMethod);



            }
        });

    }


    @Override
    public void populatePassErrorLayout(String error) {
        passLayout.setError(error);

    }

    @Override
    public void populateCodeErrorLayout(String error) {
        codeLayout.setError(error);
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
