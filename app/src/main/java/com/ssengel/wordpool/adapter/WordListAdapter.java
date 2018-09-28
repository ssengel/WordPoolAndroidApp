package com.ssengel.wordpool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssengel.wordpool.R;
import com.ssengel.wordpool.model.Word;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.MyViewHolder> {

    private ArrayList<Word> wordList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtEng;
        public TextView txtTr;
        public TextView txtSentence;
        public TextView txtTime;

        public MyViewHolder(View listItem) {
            super(listItem);
            txtEng = (TextView) itemView.findViewById(R.id.txtEng);
            txtTr = (TextView) itemView.findViewById(R.id.txtTr);
            txtSentence = (TextView) itemView.findViewById(R.id.txtSentence);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);

        }
    }

    public WordListAdapter(ArrayList<Word> wordList) {
        this.wordList = wordList;
    }

    @Override
    public WordListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Date date = wordList.get(position).getCreatedAt();

        holder.txtEng.setText(wordList.get(position).getEng());
        holder.txtSentence.setText(wordList.get(position).getSentence());
        holder.txtTr.setText(wordList.get(position).getTr());
        holder.txtTime.setText(new SimpleDateFormat("d-MM-yyy").format(date));
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }
}