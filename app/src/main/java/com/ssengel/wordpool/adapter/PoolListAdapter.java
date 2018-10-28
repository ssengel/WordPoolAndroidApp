package com.ssengel.wordpool.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Pool;
import com.ssengel.wordpool.pages.PoolDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PoolListAdapter extends  RecyclerView.Adapter<PoolListAdapter.MyViewHolder>{

    private Context context;
    private List<Pool> poolList;

    public PoolListAdapter(Context context, ArrayList<Pool> poolList) {
        this.context = context;
        this.poolList = poolList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCategory;
        public ImageView imgCategory;
        public View view;

        public  MyViewHolder(View view) {
            super(view);
            this.view = view;

            txtCategory = view.findViewById(R.id.txtCategory);
            imgCategory = view.findViewById(R.id.imgCategory);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        return new PoolListAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Pool pool = poolList.get(position);
        int imgResource = CategoryToResorceId.getImageResource(pool.getName());

        holder.txtCategory.setText(pool.getName());
        holder.imgCategory.setImageResource(imgResource);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent poolDetailIntent = new Intent(context, PoolDetailActivity.class);
                poolDetailIntent.putExtra("poolId", pool.get_id());
                poolDetailIntent.putExtra("poolName", pool.getName());
                context.startActivity(poolDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return poolList.size();
    }

}
