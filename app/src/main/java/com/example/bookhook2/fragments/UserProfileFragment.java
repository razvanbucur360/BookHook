package com.example.bookhook2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookhook2.LoginActivity;
import com.example.bookhook2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private TextView textViewWelcome;
    private Button buttonLogOut;
    private Context mContext;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.user_profile,container,false);
        mContext = view.getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        initView(view, user);


        return view;
    }

    private void initView(View view, FirebaseUser user) {
        textViewWelcome = (TextView) view.findViewById(R.id.textWelcomeUser);
        textViewWelcome.setText("Welcome " + user.getEmail());

        buttonLogOut = (Button)view.findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogOut){
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}
