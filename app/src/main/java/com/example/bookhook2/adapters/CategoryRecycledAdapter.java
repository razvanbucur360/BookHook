package com.example.bookhook2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookhook2.NetworkUtils;
import com.example.bookhook2.R;
import com.example.bookhook2.models.Category;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class CategoryRecycledAdapter extends RecyclerView.Adapter<CategoryRecycledAdapter.ViewHolder>{

    private static final String TAG = "CategoryRecycledAdapter";

    private ArrayList<Category> mCategoryList = new ArrayList<>();
    private Context mContext;

    public CategoryRecycledAdapter(ArrayList<Category> mCategoryList, Context mContext) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
    }

    public CategoryRecycledAdapter(ArrayList<Category> mCatergories) {
        this.mCategoryList = mCatergories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = mCategoryList.get(position);
        setImageBackground(holder.categoryName, category.getImageURL());
        holder.categoryName.setText(category.getName());
    }

    private void setImageBackground(TextView categoryName, String imageURL) {
        Bitmap resultBitmap = null;
        try {
            resultBitmap = new NetworkUtils().execute(imageURL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(resultBitmap != null){
            categoryName.setBackground(new BitmapDrawable(mContext.getResources(), resultBitmap));
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView categoryName;
        public RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categories);
            parentLayout = itemView.findViewById(R.id.categories);
        }

    }


}
