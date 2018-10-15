package com.ssengel.wordpool;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.asyncResponce.WordListCallBack;
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
    Random generator = new Random();


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

        fetchData();
        initListeners();

        return view;
    }
    private void initListeners(){
        flipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flipView.isFrontSide()){
                    txtTr.setText(currentWord.getTr());
                }else{
                    updateCurrentWord();
                    txtEng.setText(currentWord.getEng());
                    txtSentence.setText(currentWord.getSentence());
                    imgCategory.setImageResource(CategoryToResorceId.getImageResource(currentWord.getCategory()));
                }
            }
        });
//        flipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
//            @Override
//            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
//                if(easyFlipView.isFrontSide()){
//                    txtTr.setText(currentWord.getTr());
//                }else{
//                    updateCurrentWord();
//                    txtEng.setText(currentWord.getEng());
//                    txtSentence.setText(currentWord.getSentence());
//                    imgCategory.setImageResource(CategoryToResorceId.getImageResource(currentWord.getCategory()));
//                }
//            }
//        });
    }

    private void updateCurrentWord() {
        int i = generator.nextInt(wordList.size());
        currentWord = wordList.get(i);
    }

    private void fetchData(){
        wordDAO.getWords(new WordListCallBack() {
            @Override
            public void processFinish(List list) {
                wordList = (ArrayList<Word>) list;
            }

            @Override
            public void responseError(Error error) {
                Toast.makeText(getContext(),"Couldn't fetch the words..",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
