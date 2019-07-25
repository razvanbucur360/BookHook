package com.example.bookhook2.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.bookhook2.R;
import com.example.bookhook2.models.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.bookhook2.util.Constants.MAPVIEW_BUNDLE_KEY;

public class EventDescriptionFragment extends Fragment implements OnMapReadyCallback {

    private Event event = null;
    private Context mContext = getContext();
    private MapView mMapView;
    private Geocoder mGeocoder;
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;


    public EventDescriptionFragment(Event item) {
        this.event = item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.event_description, container, false);
        mContext = view.getContext();
        mMapView = view.findViewById(R.id.event_map);

        initView(view);
        initGoogleMap(savedInstanceState);

        return view;

    }

    public void getLocation() throws IOException {
        mGeocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> lAddresses = mGeocoder.getFromLocationName(event.getAddress(),3);
        Address lAddressForMap = lAddresses.get(0);

        double bottomBoundary = lAddressForMap.getLatitude() - .05;
        double leftBoundary = lAddressForMap.getLongitude() - .05;
        double topBoundary = lAddressForMap.getLatitude() + .05;
        double rightBoundary = lAddressForMap.getLongitude() + .05;

        mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary,0));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lAddressForMap.getLatitude(),lAddressForMap.getLongitude())));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    private void initView(View view) {
        ImageView eventImageView = (ImageView)view.findViewById(R.id.event_image);
        Picasso.with(getContext()).load(event.getImageURL()).into(eventImageView);

        TextView eventTitle = (TextView)view.findViewById(R.id.event_title);
        eventTitle.setText(event.getName());

        TextView eventDate = (TextView)view.findViewById(R.id.event_date);
        eventDate.setText(event.getStringDate());

        TextView eventPrice = (TextView)view.findViewById(R.id.event_price);
        eventPrice.setText(event.getPrice());

        TextView eventLocation = (TextView)view.findViewById(R.id.event_location);
        eventLocation.setText(event.getLocation());

        TextView eventAddress = (TextView)view.findViewById(R.id.event_address);
        eventAddress.setText(event.getAddress());

        TextView eventDescription = (TextView)view.findViewById(R.id.event_description);
        eventDescription.setText(event.getDescription());
    }

    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if(savedInstanceState!=null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap = map;
        try {
            getLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
