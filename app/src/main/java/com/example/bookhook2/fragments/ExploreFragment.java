package com.example.bookhook2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookhook2.R;
import com.example.bookhook2.adapters.CategoryAdapter;
import com.example.bookhook2.models.Category;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    private FirebaseDatabase mFireBaseDataBase;
    private DatabaseReference mDBReference;
    private ArrayList<Category> mCatergories;
    private CategoryAdapter categoryAdapter;
    private ChildEventListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.explore_fragment, container, false);
        mCatergories = new ArrayList<Category>();
        categoryAdapter = new CategoryAdapter(getContext(), mCatergories);
        ListView listView = view.findViewById(R.id.categories);
        listView.setAdapter(categoryAdapter);

        mFireBaseDataBase = FirebaseDatabase.getInstance();
        mDBReference = mFireBaseDataBase.getReference().child("categories");

        initialiseData();

        return view;
    }

    private void initialiseData() {
        if(listener == null){
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Category category = (Category) dataSnapshot.getValue(Category.class);
                    categoryAdapter.add(category);
                    categoryAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mDBReference.addChildEventListener(listener);
        }
    }
}
