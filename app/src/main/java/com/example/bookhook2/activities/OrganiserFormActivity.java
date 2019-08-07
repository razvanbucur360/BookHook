package com.example.bookhook2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookhook2.R;
import com.example.bookhook2.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrganiserFormActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private EditText editTextEventName;
    private EditText editTextEventLocation;
    private EditText editTextEventDescription;
    private EditText editTextEventImageURL;
    private EditText editTextEventAddress;
    private EditText editTextEventDate;
    private EditText editTextEventPrice;
    private Spinner spinnerCategory;
    private Button buttonSumbit;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String categoryText;
    private DatabaseReference mEventsReference;
    private FirebaseDatabase mFirebaseDatabase = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_form);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEventsReference = mFirebaseDatabase.getReference().child("events");

        if(user == null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        initiateView();
    }

    private void initiateView() {
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        editTextEventDescription = findViewById(R.id.editTextEventDescription);
        editTextEventImageURL = findViewById(R.id.editTextEventImageURL);
        editTextEventAddress = findViewById(R.id.editTextEventAddress);
        editTextEventDate = findViewById(R.id.editTextEventDate);
        editTextEventPrice = findViewById(R.id.editTextEventPrice);

        spinnerCategory = findViewById(R.id.spinnerEventCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(this);

        buttonSumbit = findViewById(R.id.buttonSubmit);
        buttonSumbit.setOnClickListener(this);
    }
    

    @Override
    public void onClick(View view) {
        if(view == buttonSumbit){
            Event event = new Event(editTextEventName.getText().toString(), editTextEventLocation.getText().toString(),
                    editTextEventDescription.getText().toString(), editTextEventImageURL.getText().toString(),
                    editTextEventAddress.getText().toString(), editTextEventDate.getText().toString(),
                    editTextEventPrice.getText().toString(), categoryText);
            mEventsReference.push().setValue(event);
            Toast.makeText(this, "Event added successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, OrganiserProfileActivity.class));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categoryText = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, OrganiserProfileActivity.class));
    }
}
