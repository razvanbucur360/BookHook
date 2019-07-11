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

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;

public class EventListFragment extends Fragment {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDBReference;
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

        mDBReference = mFirebaseDatabase.getReference().child("events");
        Event event = new Event("Neversea","Constanta, Romania","Come and live your dreams at the shores of Constanta",
                 "https://pbs.twimg.com/profile_images/851357217655795712/tYSlqPja_400x400.jpg", "Plaja Modern", "2019/07/04");
        mDBReference.push().setValue(event);
        event = new Event("Untold","Cluj, Romania","Fifth anniversary of the greatest festival in Romania",
                 "https://www.clujlife.com/wp-content/uploads/2019/04/cazare-untold-1.jpg", "Cluj Arena", "2019/08/04");
        mDBReference.push().setValue(event);

        //event = new Event("")
        initialiseData();

        return view;

    }

    private void initialiseData() {
        if(mListener == null){
            mListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Event event = (Event) dataSnapshot.getValue(Event.class);
                    mEventListAdapter.add(event);
                    mEventListAdapter.notifyDataSetChanged();
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
            mDBReference.addChildEventListener(mListener);
        }
    }
}
