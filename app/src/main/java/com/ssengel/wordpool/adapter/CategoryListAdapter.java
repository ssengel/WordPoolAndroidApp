package com.ssengel.wordpool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends  RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>{

    private Context context;
    private List<Category> categoryList;

    public CategoryListAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCategory;
        public ImageView imgCategory;

        public MyViewHolder(View listItem) {
            super(listItem);
            txtCategory = listItem.findViewById(R.id.txtCategory);
            imgCategory = listItem.findViewById(R.id.imgCategory);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        return new CategoryListAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtCategory.setText(category.getName());
        holder.imgCategory.setImageResource(R.drawable.category);
//        Glide.with(context).load("https://www.ubertheme.com/wp-content/uploads/sites/3/edd/2014/06/jm-category.png").into(holder.imgCategory);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
