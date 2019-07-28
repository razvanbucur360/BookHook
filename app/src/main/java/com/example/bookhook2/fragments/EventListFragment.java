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

public class EventListFragment extends Fragment {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEventsReference;
    private ArrayList<Event> mEvents;
    private EventListAdapter mEventListAdapter;
    private ChildEventListener mListener;
    private String eventType;
    private EditText searchView;
    private ListView listView;


    public EventListFragment() {

    }

    public EventListFragment(String eventType) {
        this.eventType = eventType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.event_list_fragment, container, false);
        mEvents = new ArrayList<>();
        mEventListAdapter = new EventListAdapter(getContext(), mEvents);

        searchView = (EditText)view.findViewById(R.id.textViewSearch);
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

//        Event event = new Event("Neversea","Constanta, Romania","Come and live your dreams at the shores of Constanta",
//                 "https://pbs.twimg.com/profile_images/851357217655795712/tYSlqPja_400x400.jpg", "Plaja Modern", "2019/07/04","100$","Music");
//        mEventsReference.push().setValue(event);
//        event = new Event("Untold","Cluj, Romania","Fifth anniversary of the greatest festival in Romania",
//                 "https://www.clujlife.com/wp-content/uploads/2019/04/cazare-untold-1.jpg", "Cluj Arena", "2019/08/04", "150$","Music");
//        mEventsReference.push().setValue(event);
//        event = new Event("AFC Asian Cup","Abu Dhabi, Al Ain, Dubai and Sharjah","From the Far East to the Middle East, the top Asian footballing nations will meet in the UAE to compete for the AFC Asian Cup UAE 2019. The largest tournament field in history, with 24 participating nations",
//                "https://upload.wikimedia.org/wikipedia/en/thumb/f/fa/2019_AFC_Asian_Cup_logo.svg/1200px-2019_AFC_Asian_Cup_logo.svg.png", "Abu Dhabi, Al Ain, Dubai and Sharjah", "2019/01/05", "200$","Sports");
//        mEventsReference.push().setValue(event);
//        event = new Event("Melbourne Cup ","VICTORIA | AUSTRALIA","The Melbourne Cup is Australia’s most well known annual Thoroughbred horse race. It is a 3,200 metre race for three-year-olds and over, conducted by the Victoria Racing Club on the Flemington Racecourse in Melbourne, Victoria as part of the Melbourne Spring Racing Carnival. It is the richest “two-mile” handicap in the world, and one of the richest turf races. The event starts at 3pm on the first Tuesday in November and is known locally as “the race that stops a nation",
//                "https://i0.wp.com/tvtonight.com.au/wp-content/uploads/2017-11-02_2326.jpg", "Flemington Racecourse", "2019/11/05", "200$","Sports");
//        mEventsReference.push().setValue(event);
//        event = new Event("Cherry Blossom Festival","Shillong, Meghalaya","Cherry Blossom Festival celebrates the unique autumn flowering of Himalayan Cherry Blossoms with several cultural events at Shillong, Meghalaya. Visitors can enjoy fashion shows, rock concerts, a beauty pageant and even compete in an amateur Golf Tournament. Alongside, there will be stalls showcasing the region’s food, wine and crafts and there are several Japanese cultural events, a Japanese Food Pavilion and a Higher Education Stall in partnership with the Embassy of Japan in India.",
//                "https://assets3.thrillist.com/v1/image/2813377/size/gn-gift_guide_variable_c.jpg", "Shillong, Meghalaya", "2019/11/14", "120$","Cultural");
//        mEventsReference.push().setValue(event);
//        event = new Event("Hornbill Festival","Kisama, Nagaland","The Hornbill Festival is a celebration held every year from 1 – 10 December, in Nagaland, Northeast India. It is also called the ‘Festival of Festivals’. The state of Nagaland is home to several tribes, which have their own distinct festivals. More than 60% of the population of Nagaland depends on agriculture and therefore most of their festivals revolve around agriculture. The Nagas consider their festivals sacred, so participation in these festivals is essential.",
//                "https://theonewithaplan.com/wp-content/uploads/2018/12/hornbill-festival-nagaland-55.jpg", "Kisama, Nagaland", "2019/12/01", "160$","Cultural");
//        mEventsReference.push().setValue(event);
//        event = new Event("City Hall Reception","Town Hall","\n" +
//                "The LOC is delighted to announce the invitation of the City of Helsinki to a reception at the City Hall. The City Hall is one of the city's landmarks and is located in the Kruununhaka quarter which has been – since the 20th century – the shopping and business centre, as well as the centre of the social life of the city. All delegates are invited to discover a piece of this urban culture of Helsinki while some snacks and drinks will be served.",
//                "https://media-cdn.tripadvisor.com/media/photo-s/02/15/26/ba/city-hall-helsinki.jpg", "Pohjoisesplanadi 11–13, 00170 Helsinki", "2019/09/09","300$","Social");
//        mEventsReference.push().setValue(event);
//        event = new Event("Welcome Reception","Finlandia Hall, Exhibition Area","On the first evening of the congress, all delegates are kindly invited to join the Welcome Reception which will take place directly after the Opening Ceremony at the congress venue, in the exhibition area. It states the perfect opportunity to get in first contact with colleagues and/or business partners in a relaxed atmosphere while enjoying local snacks and drinks.",
//                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Finlandia-talo_ulkisivu_itapuolelta_east_side_facade_Photo_Rauno_Traskelin.jpg/220px-Finlandia-talo_ulkisivu_itapuolelta_east_side_facade_Photo_Rauno_Traskelin.jpg", "Mannerheimintie 13e, 00100 Helsinki", "2019/09/08","400$","Social");
//        mEventsReference.push().setValue(event);

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
                        if(eventItem.getCategory().equals(eventType))
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
            mEventsReference.orderByChild("category").equalTo(eventType).addChildEventListener(mListener);

        }
    }



    private interface FirebaseCallback{
        void onCallback(EventListAdapter events);
    }
}
