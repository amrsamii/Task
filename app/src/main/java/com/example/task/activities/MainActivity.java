package com.example.task.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.task.R;
import com.example.task.fragments.HomeFragment;
import com.example.task.fragments.ProfileFragment;
import com.example.task.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    Fragment fragment = null;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                if(!(fragment instanceof HomeFragment)) {
                    fragment = new HomeFragment();
                    bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                }
                break;

            case R.id.menu_more:
                if(!(fragment instanceof SettingsFragment)) {
                    fragment = new SettingsFragment();
                    bottomNavigationView.getMenu().findItem(R.id.menu_more).setChecked(true);

                }
                break;
            case R.id.menu_profile:
                if(!(fragment instanceof ProfileFragment)) {
                    fragment = new ProfileFragment();
                    bottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);


                }
                break;

        }
        if (fragment != null)
            displayFragment(fragment);

        return false;
    }
    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.cont, fragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(fragment instanceof ProfileFragment){
            displayFragment( new ProfileFragment());
        }
    }

    @Override
    public void onBackPressed() {
        if(fragment instanceof ProfileFragment) {
            fragment = new HomeFragment();
            bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
            displayFragment(fragment);
        }
        else if(fragment instanceof SettingsFragment) {
            fragment = new HomeFragment();
            bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
            displayFragment(fragment);
        }
        else
            super.onBackPressed();
    }
}
