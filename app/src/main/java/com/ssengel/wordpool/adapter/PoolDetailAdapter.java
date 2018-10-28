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
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.PWord;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;

public class PoolDetailAdapter extends RecyclerView.Adapter<PoolDetailAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<PWord> pWordList;
    private WordDAO wordDAO = new WordDAO();
    private String poolName;
    private int imgResorceId;

    public PoolDetailAdapter(Context context, ArrayList<PWord> pWordList, String poolName) {
        this.context = context;
        this.pWordList = pWordList;
        this.poolName = poolName;
        this.imgResorceId = CategoryToResorceId.getImageResource(poolName);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgCategory;
        public TextView txtEng;
        public TextView txtTr;
        public TextView txtSentence;
        public TextView txtDate;
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
            txtDate = (TextView) view.findViewById(R.id.txtDate);

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

        PWord pWord = pWordList.get(position);

        holder.imgCategory.setImageResource(imgResorceId);
        holder.txtEng.setText(pWord.getEng());
        holder.txtTr.setText(pWord.getTr());
        holder.txtSentence.setText(pWord.getSentence());
//        holder.txtDate.setText(new SimpleDateFormat("d-MM-yyy").format(date));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: Open Detail PWord
            }
        });
    }

    @Override
    public int getItemCount() {
        return pWordList.size();
    }

    public void removeItem(int position, WordDAO.WordObjectCallback callback) {
        PWord pWord = pWordList.get(position);
        Word word = new Word();
        word.setEng(pWord.getEng());
        word.setTr(pWord.getTr());
        word.setSentence(pWord.getSentence());
        word.setCategory(poolName);

        wordDAO.createWord(word,callback);
        pWordList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(PWord pWord, int position) {
        pWordList.add(position, pWord);
        notifyItemInserted(position);
    }
}
