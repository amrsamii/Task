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
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


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


    private static final int PICKER_REQUEST_CODE = 431;

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
                startActivity(new Intent(getContext(), UpdateProfile.class));
            }
        });

        //tried to uploag user image after choosing it but response gives internal server error

    /*    profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "complete my action using"), PICKER_REQUEST_CODE);

            }
        });*/

    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            File originalFile = FileUtils.getFile(getContext(),imageUri);
            RequestBody filePart = RequestBody.create(
                    MediaType.parse("image/*"),
                    originalFile);

            MultipartBody.Part file = MultipartBody.Part.createFormData("file",originalFile.getName(),filePart);
            APIClient client = ServiceGenerator.createService(APIClient.class);
            Call<ResponseBody> call = client.uploadFile(file);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }

    }*/
}
