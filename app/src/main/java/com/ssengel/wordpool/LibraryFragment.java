package com.ssengel.wordpool;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ssengel.wordpool.adapter.PoolListAdapter;
import com.ssengel.wordpool.helper.GridSpacingItemDecoration;
import com.ssengel.wordpool.model.Pool;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Pool> poolList;
    private PoolListAdapter poolListAdapter;

    public LibraryFragment() {    }

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        recyclerView = view.findViewById(R.id.categoryList);
        poolList = new ArrayList<>();
        poolListAdapter = new PoolListAdapter(getActivity(), poolList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(poolListAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        fetchCategories();

        return view;
    }



    private void fetchCategories(){


        ArrayList<Word> list1 = new ArrayList<>();
        Word w1 = new Word();
        w1.setEng("health");
        w1.setTr("saglik");
        w1.setSentence("asdfasdf asdfasdf asdf asd");
        w1.setCategory("Health");
        list1.add(w1);

        ArrayList<Word> list2 = new ArrayList<>();
        Word w2 = new Word();
        w2.setEng("Electronic");
        w2.setTr("saglik");
        w2.setSentence("asdfasdf asdfasdf asdf asd");
        w2.setCategory("Electronic");
        list2.add(w2);

        ArrayList<Word> list3 = new ArrayList<>();
        Word w3 = new Word();
        w3.setEng("Art");
        w3.setTr("saglik");
        w3.setSentence("asdfasdf asdfasdf asdf asd");
        w3.setCategory("Art");

        Word w4 = new Word();
        w4.setEng("Art");
        w4.setTr("saglik");
        w4.setSentence("asdfasdf asdfasdf asdf asd");
        w4.setCategory("Art");

        list3.add(w3);
        list3.add(w4);



        poolList.add(new Pool("asdf","Health", list1));
        poolList.add(new Pool("asdf","Science",new ArrayList<Word>()));
        poolList.add(new Pool("asdf","Electronic",list2));
        poolList.add(new Pool("asdf", "Art",list3));
        poolList.add(new Pool("asdf", "Kitchen",new ArrayList<Word>()));
        poolList.add(new Pool("adsf","Space",new ArrayList<Word>()));
        poolList.add(new Pool("asdfadf","Computer",new ArrayList<Word>()));
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
