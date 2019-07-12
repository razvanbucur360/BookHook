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
import com.example.bookhook2.adapters.EventListAdapter;
import com.example.bookhook2.models.Event;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SocialEventsFragment extends Fragment {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEventsReference;
    private ArrayList<Event> mEvents;
    private EventListAdapter mEventListAdapter;
    private ChildEventListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.event_list_fragment, container, false);
        mEvents = new ArrayList<>();
        mEventListAdapter = new EventListAdapter(getContext(), mEvents);
        ListView listView = view.findViewById(R.id.event_list);
        listView.setAdapter(mEventListAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEventsReference = mFirebaseDatabase.getReference().child("events");


        readData(new FirebaseCallback() {
            @Override
            public void onCallback(EventListAdapter events) {

            }
        });
        return view;
    }


    private void readData(final FirebaseCallback firebaseCallback){
        if(mListener == null){
            mListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Event event = (Event) dataSnapshot.getValue(Event.class);
                    mEventListAdapter.add(event);
                    mEventListAdapter.notifyDataSetChanged();
                    firebaseCallback.onCallback(mEventListAdapter);
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
            mEventsReference.orderByChild("category").equalTo("Social").addChildEventListener(mListener);
        }
    }

    private interface FirebaseCallback{
        void onCallback(EventListAdapter events);
    }
}
