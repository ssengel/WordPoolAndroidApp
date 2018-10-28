package com.ssengel.wordpool.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssengel.wordpool.R;
import com.ssengel.wordpool.pages.WordDetailActivity;
import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Word;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Word> wordList;
    private ArrayList<Word> wordListFiltered;

    public WordListAdapter(ArrayList<Word> wordList, Context context) {
        this.context = context;
        this.wordList = wordList;
        this.wordListFiltered = wordList;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgCategory;
        public TextView txtEng;
        public TextView txtTr;
        public TextView txtSentence;
        public TextView txtTime;
        public View view;

        public MyViewHolder(View listItem) {
            super(listItem);
            this.view = listItem;
            imgCategory = (ImageView) listItem.findViewById(R.id.imgCategory);
            txtEng = (TextView) itemView.findViewById(R.id.txtEng);
            txtTr = (TextView) itemView.findViewById(R.id.txtTr);
            txtSentence = (TextView) itemView.findViewById(R.id.txtSentence);
            txtTime = (TextView) itemView.findViewById(R.id.txtDate);

        }
    }


    @Override
    public WordListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Word word = wordListFiltered.get(position);
        Date date = word.getCreatedAt();
        int imgResorceId = CategoryToResorceId.getImageResource(word.getCategory());

        holder.imgCategory.setImageResource(imgResorceId);
        holder.txtEng.setText(word.getEng());
        holder.txtSentence.setText(word.getSentence());
        holder.txtTr.setText(word.getTr());
        holder.txtTime.setText(new SimpleDateFormat("d-MM-yyy").format(date));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: Open Detail Word
                Intent intent = new Intent(context,WordDetailActivity.class);
                intent.putExtra("wordId", word.get_id());
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    @Override
    public int getItemCount() {
        return wordListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    wordListFiltered = wordList;
                } else {
                    ArrayList<Word> filteredList = new ArrayList<>();
                    for (Word word : wordList) {
                        if (word.getEng().toLowerCase().contains(charString.toLowerCase()) || word.getTr().contains(charSequence) || word.getSentence().contains(charSequence)) {
                            filteredList.add(word);
                        }
                    }
                    wordListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = wordListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                wordListFiltered = (ArrayList<Word>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void removeItem(int position) {
        wordListFiltered.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Word word, int position) {
        wordListFiltered.add(position, word);
        notifyItemInserted(position);
    }
}