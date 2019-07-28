package com.example.bookhook2.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookhook2.R;
import com.example.bookhook2.adapters.EventListAdapter;
import com.example.bookhook2.models.Event;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllCategoryEventsFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEventsReference;
    private ArrayList<Event> mEvents;
    private EventListAdapter mEventListAdapter;
    private ChildEventListener mListener;
    private EditText searchView;
    private ListView listView;

    public AllCategoryEventsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.event_list_fragment,container,false);
        mEvents = new ArrayList<>();
        mEventListAdapter = new EventListAdapter(getContext(), mEvents);

        searchView = (EditText) view.findViewById(R.id.textViewSearch);
        searchView.setBackgroundResource(R.drawable.searchview);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    search(editable.toString());
                }else{
                    search("");
                }
            }
        });

        listView = view.findViewById(R.id.event_list);
        listView.setAdapter(mEventListAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEventsReference = mFirebaseDatabase.getReference().child("events");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment lCurrentFragment = new EventDescriptionFragment(mEventListAdapter.getItem(i));
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment, lCurrentFragment);
                fr.addToBackStack(null);
                fr.commit();
            }
        });

        readData(new FirebaseCallback() {
            @Override
            public void onCallback(EventListAdapter events) {

            }
        });
        return view;
    }

    private void search(String s) {
        Query query = mEventsReference.orderByChild("name")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    mEvents.clear();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        final Event eventItem = ds.getValue(Event.class);
                        mEvents.add(eventItem);
                    }
                    EventListAdapter adapter = new EventListAdapter(getContext(), mEvents);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            mEventsReference.addChildEventListener(mListener);

        }
    }

    private interface FirebaseCallback{
        void onCallback(EventListAdapter events);
    }

}
