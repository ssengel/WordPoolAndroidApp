package com.ssengel.wordpool;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private static final String TAG = "PoolFragment";

    private RecyclerView recyclerView;
    private WordDAO wordDAO;
    private ArrayList<Word> mWordList;
    private WordListAdapter wordListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView;



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

        setHasOptionsMenu(true);//update toolbar and menu

        View view = inflater.inflate(R.layout.fragment_pool, container, false);
        wordDAO = new WordDAO();
        mWordList = new ArrayList<>();
        wordListAdapter = new WordListAdapter(mWordList);
        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView = view.findViewById(R.id.wordList);
        recyclerView.setAdapter(wordListAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fetchWords();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pool, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                wordListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                wordListAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_createWord) {
            startActivity(new Intent(getContext(), CreateWordActivity.class));
        }

        return super.onOptionsItemSelected(item);
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
