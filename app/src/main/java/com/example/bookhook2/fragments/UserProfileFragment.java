package com.example.bookhook2.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.bookhook2.activities.LoginActivity;
import com.example.bookhook2.util.VerifyPhoneNumberActivity;
import com.example.bookhook2.R;
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

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private static final int RESULT_OK = 9005;
    private static final int REQUEST_CODE = 101;
    private Button buttonLogOut;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.user_profile_fragment,container,false);
        mAuth = FirebaseAuth.getInstance();
        mContext = view.getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        initView(view, user);
        loadUserInformation();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view, FirebaseUser user) {
        userFullName = (TextView)view.findViewById(R.id.userFullName);
        userProfileIamge = (ImageView)view.findViewById(R.id.userProfileImage);
        userEmail = (TextView)view.findViewById(R.id.user_email);
        userEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        userPhoneNumber = (TextView)view.findViewById(R.id.user_phone_number);


        userLocation = (TextView)view.findViewById(R.id.user_location);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(getContext());
        fetchLastLocation();
        buttonLogOut = (Button)view.findViewById(R.id.buttonLogOut);

        userProfileIamge.setOnClickListener(this);
        userPhoneNumber.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user.getPhotoUrl() != null){
            String photoURL = user.getPhotoUrl().toString();
            Picasso.with(getContext()).load(photoURL).into(userProfileIamge);
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

    @Override
    public void onClick(View view) {
        if(view == buttonLogOut){
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        if(view == userPhoneNumber){
            startActivity(new Intent(getActivity(), VerifyPhoneNumberActivity.class));
        }

        if(view == userProfileIamge){
            showImageChooser();
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
                        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && data != null && data.getData() != null){
            uriProfileImage = data.getData();
            Picasso.with(getContext()).load(uriProfileImage).into(userProfileIamge);
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
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    private void fetchLastLocation(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
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

}
