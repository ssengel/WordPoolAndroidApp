package com.ssengel.wordpool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.LibraryFragment;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.asyncResponce.WordObjectCallBack;
import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;

public class PoolDetailAdapter extends RecyclerView.Adapter<PoolDetailAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Word> wordList;
    private WordDAO wordDAO = new WordDAO();

    public PoolDetailAdapter(Context context, ArrayList<Word> wordList) {
        this.context = context;
        this.wordList = wordList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgCategory;
        public TextView txtEng;
        public TextView txtTr;
        public TextView txtSentence;
        public TextView txtTime;
        public View view;
        public RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

        public MyViewHolder(View listItem) {
            super(listItem);
            this.view = listItem;

            imgCategory = (ImageView) view.findViewById(R.id.imgCategory);
            txtEng = (TextView) view.findViewById(R.id.txtEng);
            txtTr = (TextView) view.findViewById(R.id.txtTr);
            txtSentence = (TextView) view.findViewById(R.id.txtSentence);
            txtTime = (TextView) view.findViewById(R.id.txtDate);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item_2, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Word word = wordList.get(position);
        int imgResorceId = CategoryToResorceId.getImageResource(word.getCategory());

        holder.imgCategory.setImageResource(imgResorceId);
        holder.txtEng.setText(word.getEng());
        holder.txtTr.setText(word.getTr());
        holder.txtSentence.setText(word.getSentence());
//        holder.txtTime.setText(new SimpleDateFormat("d-MM-yyy").format(date));

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

    public void removeItem(int position, WordObjectCallBack callback) {
        wordDAO.createWord(wordList.get(position), callback);
        wordList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Word word, int position) {
        wordList.add(position, word);
        notifyItemInserted(position);
    }
}
