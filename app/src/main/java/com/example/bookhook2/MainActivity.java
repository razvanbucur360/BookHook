package com.example.bookhook2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bookhook2.fragments.EventListFragment;
import com.example.bookhook2.fragments.ExploreFragment;
import com.example.bookhook2.models.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFireBaseDataBase;
    private DatabaseReference mDBReference;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_main);
        mFireBaseDataBase = FirebaseDatabase.getInstance();
        BottomNavigationView mNavigation = findViewById(R.id.navigation_view);
        BottomNavigationView.OnNavigationItemSelectedListener mNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment mCurrentFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.navigation_browse:
                        mCurrentFragment = new ExploreFragment();
                        break;
                    case R.id.navigation_search:
                        mCurrentFragment = new EventListFragment();
                        break;
                    case R.id.navigation_profile:

                        break;

                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, mCurrentFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }

        };
        mNavigation.setOnNavigationItemSelectedListener(mNavigationListener);
    }
}
