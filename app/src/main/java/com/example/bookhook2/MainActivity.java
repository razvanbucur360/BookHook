package com.example.bookhook2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bookhook2.fragments.ExploreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFireBaseDataBase;
    private DatabaseReference mDBReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFireBaseDataBase = FirebaseDatabase.getInstance();
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

                        break;
                    case R.id.navigation_profile:

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
