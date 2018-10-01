package com.ssengel.wordpool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Word;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PoolDetailAdapter extends RecyclerView.Adapter<PoolDetailAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Word> wordList;

    public PoolDetailAdapter(Context context, ArrayList<Word> wordList) {
        this.context = context;
        this.wordList = wordList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgCategory;
        public TextView txtEng;
        public TextView txtTr;
        public TextView txtSentence;
        public View view;

        public MyViewHolder(View listItem) {
            super(listItem);
            this.view = listItem;
            imgCategory = (ImageView) listItem.findViewById(R.id.imgCategory);
            txtEng = (TextView) itemView.findViewById(R.id.txtEng);
            txtTr = (TextView) itemView.findViewById(R.id.txtTr);
            txtSentence = (TextView) itemView.findViewById(R.id.txtSentence);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Word word = wordList.get(position);
        int imgResorceId = CategoryToResorceId.getImageResource(word.getCategory());

        holder.imgCategory.setImageResource(imgResorceId);
        holder.txtEng.setText(word.getEng());
        holder.txtSentence.setText(word.getSentence());
        holder.txtTr.setText(word.getTr());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: Open Detail Word
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }
}
