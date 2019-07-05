package com.example.task.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.task.model.User;

public class SharedPrefManager {

    private static final String MY_SHARED_PREF = "my_shared_pref";
    private static SharedPrefManager mInstance;
    private Context mContext;

    private SharedPrefManager(Context context)
    {
        this.mContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){

        if(mInstance==null)
        {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public void saveUser(User user){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",user.getId());
        editor.putString("email",user.getEmail());
        editor.putString("name",user.getName());
        editor.putString("phone",user.getPhone());
        editor.putString("api_token",user.getApi_token());
        editor.putString("reset_password_code",user.getReset_password_code());
        editor.apply();
    }

    public User getUser(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREF,Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id",-1),
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("phone",null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("api_token",null),
                sharedPreferences.getString("reset_password_code",null)

        );
    }

    public boolean isLoggedIn(){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREF,Context.MODE_PRIVATE);

        return sharedPreferences.getInt("id",-1)!=-1;

    }


    // to Log out
    public void clear()
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
