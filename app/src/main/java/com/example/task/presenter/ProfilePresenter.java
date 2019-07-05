package com.example.task.presenter;

import android.content.Context;

import com.example.task.model.IUSer;
import com.example.task.storage.SharedPrefManager;
import com.example.task.view.IProfileView;

public class ProfilePresenter implements IProfilePresenter {

    private IProfileView profileView;
    private Context context;
    private IUSer iuSer = SharedPrefManager.getInstance(context).getUser();

    public  ProfilePresenter(Context context,IProfileView profileView){
        this.profileView = profileView;
        this.context = context;
    }
    @Override
    public void initProfile() {
        profileView.populateEmailLayout(iuSer.getEmail());
        profileView.populateMobileLayout(iuSer.getPhone());
        profileView.populateNameLayout(iuSer.getName());

    }
}
