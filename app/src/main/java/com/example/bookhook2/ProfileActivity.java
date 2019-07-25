package com.example.bookhook2;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookhook2.fragments.EventListFragment;
import com.example.bookhook2.fragments.ExploreFragment;
import com.example.bookhook2.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user =  firebaseAuth.getCurrentUser();
        //bring up the categories
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, new ExploreFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        BottomNavigationView mNavigation = findViewById(R.id.navigation_view);
        BottomNavigationView.OnNavigationItemSelectedListener mNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment lCurrentFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.navigation_browse:
                        lCurrentFragment = new ExploreFragment();
                        break;
                    case R.id.navigation_search:
                        lCurrentFragment = new EventListFragment();
                        break;
                    case R.id.navigation_profile:
                        lCurrentFragment = new UserProfileFragment();
                        break;

                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, lCurrentFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }

        };

        mNavigation.setOnNavigationItemSelectedListener(mNavigationListener);

    }
}
