package com.example.bookhook2.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bookhook2.R;

import com.example.bookhook2.util.VerifyPhoneNumberActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.bookhook2.util.Constants.CHOOSE_IMAGE;

public class OrganiserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 101;
    private Button buttonLogOut;
    private Button buttonAddEvent;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    private TextView userPhoneNumber;
    private TextView userLocation;
    private ImageView userProfileIamge;
    private TextView userFullName;
    private Uri uriProfileImage;
    private String profileImageURL;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationProvider = null;
    private Location currentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_profile);
        mAuth = FirebaseAuth.getInstance();
        mContext = this;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        initView(user);
        loadUserInformation();
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user.getPhotoUrl() != null){
            String photoURL = user.getPhotoUrl().toString();
            Picasso.with(mContext).load(photoURL).into(userProfileIamge);
        }
        if(user.getDisplayName() != null){
            userFullName.setText(user.getDisplayName());
        }
        if(user.getPhoneNumber() != null){
            if(!user.getPhoneNumber().toString().isEmpty()){
                userPhoneNumber.setText(user.getPhoneNumber());
            }
        }
    }

    private void initView(FirebaseUser user) {
        userFullName = findViewById(R.id.userFullName);
        userProfileIamge = findViewById(R.id.userProfileImage);
        userEmail = findViewById(R.id.user_email);
        userEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        userPhoneNumber = findViewById(R.id.user_phone_number);
        userLocation = findViewById(R.id.user_location);

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(mContext);

        fetchLastLocation();

        buttonLogOut = findViewById(R.id.buttonLogOut);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);

        userProfileIamge.setOnClickListener(this);
        userPhoneNumber.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);
        buttonAddEvent.setOnClickListener(this);
    }

    private void fetchLastLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProvider.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                    try {
                        List<Address> lAddresses = geocoder.getFromLocation(currentLocation.getLatitude(),
                                currentLocation.getLongitude(), 1);
                        userLocation.setText(lAddresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogOut){
            firebaseAuth.signOut();
            startActivity(new Intent(mContext, LoginActivity.class));
        }

        if(view == userPhoneNumber){
            startActivity(new Intent(mContext, VerifyPhoneNumberActivity.class));
        }

        if(view == userProfileIamge){
            showImageChooser();
        }

        if(view == buttonAddEvent){
            finish();
            startActivity(new Intent(mContext, OrganiserFormActivity.class));
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && data != null && data.getData() != null){
            uriProfileImage = data.getData();
            Picasso.with(mContext).load(uriProfileImage).into(userProfileIamge);
            uploadImageToFirebaseStorage();
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if(uriProfileImage != null){
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImageURL = uriProfileImage.toString();
                    saveUserInformation();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void saveUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && profileImageURL != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(profileImageURL))
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(mContext, "Profile updated", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
