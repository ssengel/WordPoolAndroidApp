package com.ssengel.wordpool.pages;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssengel.wordpool.globalDAO.WordDAO;
import com.ssengel.wordpool.LocalDAO.WordRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Word;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomFragment extends Fragment {

    private EasyFlipView flipView;
    private ImageView imgCategory;
    private TextView txtEng;
    private TextView txtTr;
    private TextView txtSentence;
    private ProgressDialog pDialog;

    private Word currentWord;
    private WordDAO wordDAO;
    private ArrayList<Word> wordList;
    private Random generator = new Random();

    private WordRepo wordRepo;


    public RandomFragment() {
        // Required empty public constructor
    }

    public static RandomFragment newInstance() {
        RandomFragment fragment = new RandomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_random, container, false);

        imgCategory = view.findViewById(R.id.imgCategory);
        txtEng = view.findViewById(R.id.txtEng);
        txtTr = view.findViewById(R.id.txtTr);
        txtSentence = view.findViewById(R.id.txtSentence);
        flipView = view.findViewById(R.id.flipView);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        wordDAO = new WordDAO();
        wordRepo = new WordRepo(getContext());

        new getAllWords().execute();
        initListeners();

        return view;
    }
    private void initListeners(){

        flipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                if(wordList != null){
                    if(easyFlipView.isFrontSide()){
                        txtTr.setText(currentWord.getTr());
                    }else{
                        updateCurrentWord();
                        txtEng.setText(currentWord.getEng());
                        txtSentence.setText(currentWord.getSentence());
                        imgCategory.setImageResource(CategoryToResorceId.getImageResource(currentWord.getCategory()));
                    }
                }

            }
        });
    }

    private void updateCurrentWord() {
        int i = generator.nextInt(wordList.size());
        currentWord = wordList.get(i);
    }

    private class getAllWords extends AsyncTask<String, Void, List<Word>> {

        @Override
        protected List<Word> doInBackground(String... strings) {
            try {
                return wordRepo.getAllWords();
            }catch (Exception e){
                Toast.makeText(getContext(), "depodan kelimeler cekilemedi !"+ e, Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Word> words) {
            wordList = (ArrayList<Word>) words;
        }
    }


}
