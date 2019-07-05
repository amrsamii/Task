package com.example.task.fragments;

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
import com.example.task.model.IUSer;
import com.example.task.model.User;
import com.example.task.presenter.ProfilePresenter;
import com.example.task.storage.SharedPrefManager;
import com.example.task.activities.UpdateProfileActivity;
import com.example.task.view.IProfileView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements IProfileView {


    @BindView(R.id.name_tv)
    TextView nameTextView;
    @BindView(R.id.email_tv)
    TextView emailTextView;
    @BindView(R.id.mobile_tv)
    TextView mobileTextView;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.update_button)
    MaterialRippleLayout updateButton;


    private ProfilePresenter profilePresenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        profilePresenter = new ProfilePresenter(getContext(),this);
        profilePresenter.initProfile();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UpdateProfileActivity.class));
            }
        });


    }

    @Override
    public void populateEmailLayout(String data) {
        emailTextView.setText(data);

    }

    @Override
    public void populateNameLayout(String data) {
        nameTextView.setText(data);

    }

    @Override
    public void populateMobileLayout(String data) {
        mobileTextView.setText(data);

    }
}
