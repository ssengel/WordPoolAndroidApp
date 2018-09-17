package com.ssengel.wordpool.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssengel.wordpool.R;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Word> wordList;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtEng;
        public TextView txtTr;
        public TextView txtSentence;

        public MyViewHolder(View listItem) {
            super(listItem);
            txtEng = (TextView) itemView.findViewById(R.id.txtEng);
            txtTr = (TextView) itemView.findViewById(R.id.txtTr);
            txtSentence = (TextView) itemView.findViewById(R.id.txtSentence);

        }
    }

    public MyAdapter(ArrayList<Word> wordList) {
        this.wordList = wordList;
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtEng.setText(wordList.get(position).getEng());
        holder.txtSentence.setText(wordList.get(position).getSentence());
        holder.txtTr.setText(wordList.get(position).getTr());
    }
    @Override
    public int getItemCount() {
        return wordList.size();
    }
}