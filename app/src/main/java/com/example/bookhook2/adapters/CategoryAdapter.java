package com.example.bookhook2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bookhook2.util.NetworkUtils;
import com.example.bookhook2.R;
import com.example.bookhook2.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Context mContext;

    public CategoryAdapter(Context context, ArrayList<Category> categories){
        super(context, 0, categories);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false);
        }

        TextView categoryView =(TextView)convertView.findViewById(R.id.category);
        categoryView.setText(category.getName());

        setImageBackground(categoryView, category.getImageURL());

        return convertView;
    }

    private void setImageBackground(TextView categoryView, String imageURL) {
        Bitmap resultBitmap = null;
        try {
            resultBitmap = new NetworkUtils().execute(imageURL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(resultBitmap != null){
            categoryView.setBackground(new BitmapDrawable(mContext.getResources(), resultBitmap));
        }
    }
}
