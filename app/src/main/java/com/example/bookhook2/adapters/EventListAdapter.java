package com.example.bookhook2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookhook2.NetworkUtils;
import com.example.bookhook2.R;
import com.example.bookhook2.models.Event;


import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;


public class EventListAdapter extends ArrayAdapter<Event> {

    private Context mContext;
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/mm/dd");

    public EventListAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Event event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
        }

        TextView eventTitleView = (TextView)convertView.findViewById(R.id.event_title);
        eventTitleView.setText(event.getName());

        TextView eventLocationView = (TextView)convertView.findViewById(R.id.event_location);
        eventLocationView.setText(event.getLocation());

        TextView eventDateView = (TextView)convertView.findViewById(R.id.event_date);
        eventDateView.setText(event.getStringDate());

        ImageView eventImageView = (ImageView)convertView.findViewById(R.id.event_image);
        setImage(eventImageView, event.getImageURL());

        return convertView;

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
