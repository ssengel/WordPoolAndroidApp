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
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.helper.GridSpacingItemDecoration;
import com.ssengel.wordpool.model.Pool;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    public  ArrayList<Pool> poolList;
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
        fetchCategories();// DAO' kullanildiginda callback ile herhangi bir anda fetch edilir.
        poolListAdapter = new PoolListAdapter(getActivity(), poolList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(poolListAdapter);
        recyclerView.setNestedScrollingEnabled(false);


        return view;
    }



    private void fetchCategories(){

        poolList = Config.GetLocalWords();

    }

    @Override
    public void onResume() {
        super.onResume();
        poolListAdapter.notifyDataSetChanged();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
