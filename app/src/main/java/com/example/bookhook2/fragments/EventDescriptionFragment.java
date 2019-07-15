package com.example.bookhook2.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookhook2.NetworkUtils;
import com.example.bookhook2.R;
import com.example.bookhook2.models.Event;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class EventDescriptionFragment extends Fragment {

    private Event event = null;
    private Context mContext = getContext();

    public EventDescriptionFragment(Event item) {
        this.event = item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.event_description, container, false);
        mContext = view.getContext();

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

        return view;

    }

    private void setImage(ImageView eventImageView, String imageURL) {
        Bitmap resultBitmap = null;
        try {
            resultBitmap = new NetworkUtils().execute(imageURL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(resultBitmap != null){
            eventImageView.setBackground(new BitmapDrawable(mContext.getResources(), resultBitmap));
        }
    }

}
