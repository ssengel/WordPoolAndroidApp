package com.ssengel.wordpool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.adapter.WordListAdapter;
import com.ssengel.wordpool.asyncResponce.WordListCallBack;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PoolFragment extends Fragment {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private WordDAO wordDAO;
    private ArrayList<Word> mWordList;
    private WordListAdapter wordListAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    public PoolFragment() {
    }

    public static PoolFragment newInstance() {
        PoolFragment fragment = new PoolFragment();
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

        View view = inflater.inflate(R.layout.fragment_pool, container, false);

        wordDAO = new WordDAO();
        mWordList = new ArrayList<>();
        wordListAdapter = new WordListAdapter(mWordList);
        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView = view.findViewById(R.id.wordList);
        recyclerView.setAdapter(wordListAdapter);
        recyclerView.setLayoutManager(mLayoutManager);

        fetchWords();
        return view;
    }

    private void fetchWords() {
        wordDAO.getWords(new WordListCallBack() {
            @Override
            public void processFinish(List list) {
                Collections.reverse(list);
                mWordList.clear();
                mWordList.addAll(list);
                wordListAdapter.notifyDataSetChanged();
            }

            @Override
            public void responseError(Error error) {
                Toast.makeText(getActivity(), "Could not fetch the word items.\n" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
