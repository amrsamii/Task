package com.example.task.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.R;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;

public class SigninActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_text) TextInputLayout emailTextInputLayout;
    @BindView(R.id.pass_edit_text) TextInputLayout passTextInputLayout;
    @BindView(R.id.signin_button) MaterialRippleLayout SigninButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }
}
