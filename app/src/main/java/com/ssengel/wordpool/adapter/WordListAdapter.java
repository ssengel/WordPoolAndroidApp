package com.ssengel.wordpool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssengel.wordpool.R;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;

public class WordListAdapter extends BaseAdapter {

    private ArrayList<Word> mArrayList;

    public WordListAdapter(ArrayList<Word> arrayList) {
        mArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rotView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
        TextView txtWord = (TextView) rotView.findViewById(R.id.txtEng);
        TextView txtSentence = (TextView) rotView.findViewById(R.id.txtSentence);
        txtWord.setText(mArrayList.get(position).getEng());
        txtSentence.setText(mArrayList.get(position).getSentence());

        return rotView;
    }
}
