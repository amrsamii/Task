package com.example.task.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.task.R;
import com.example.task.model.User;
import com.example.task.storage.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {


    @BindView(R.id.name_tv) TextView nameTextView;
    @BindView(R.id.email_tv) TextView emailTextView;
    @BindView(R.id.mobile_tv) TextView mobileTextView;
    @BindView(R.id.update_button) MaterialRippleLayout updateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        mobileTextView.setText(user.getPhone());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),UpdateProfile.class));
            }
        });

    }
}
