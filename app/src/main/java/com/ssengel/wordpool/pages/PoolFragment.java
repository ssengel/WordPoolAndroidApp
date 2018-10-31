package com.ssengel.wordpool.pages;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ssengel.wordpool.globalDAO.WordDAO;
import com.ssengel.wordpool.LocalDAO.OperationRepo;
import com.ssengel.wordpool.LocalDAO.WordRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.adapter.WordListAdapter;
import com.ssengel.wordpool.model.Word;
import com.ssengel.wordpool.syncronization.Sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ssengel.wordpool.pages.MainActivity.isInternetAvailable;

public class PoolFragment extends Fragment {

    private static final String TAG = "PoolFragment";

    private WordDAO wordDAO;
    private WordRepo wordRepo;
    private OperationRepo operationRepo;

    private RecyclerView recyclerView;
    private ArrayList<Word> mWordList;
    private WordListAdapter wordListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView searchView;
    private ProgressDialog pDialog;

    BottomNavigationView bottomNavigationView;

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
        wordRepo = new WordRepo(getContext());
        operationRepo = new OperationRepo(getContext());

        pDialog = new ProgressDialog(getContext());
        mWordList = new ArrayList<>();
        wordListAdapter = new WordListAdapter(mWordList, getContext());
        mLayoutManager = new LinearLayoutManager(getActivity());


        recyclerView = view.findViewById(R.id.wordList);
        recyclerView.setAdapter(wordListAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bottomNavigationView = getActivity().findViewById(R.id.navigation);

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
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(id == R.id.action_sync){
            sync();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        new getAllWords().execute();
    }


    private void sync(){
        if(isInternetAvailable(getContext())){
            pDialog.setMessage("Sync..");
            if(!pDialog.isShowing()) pDialog.show();
            Sync sync = new Sync(getContext(), new Sync.CustomCallback() {
                @Override
                public void successful() {
                    new getAllWords().execute();

                    if(pDialog.isShowing())
                        pDialog.dismiss();
                }
            });
            sync.execute();

        }else{
            new MainActivity.showMessage().execute("no internet connection !");
        }
    }



    private class getAllWords extends AsyncTask<Void, Void, List<Word>> {
        @Override
        protected List<Word> doInBackground(Void... voids) {
            try {
                return wordRepo.getAllWords();
            }catch (Exception e){
                new MainActivity.showMessage().execute(e.toString());
                return null;
            }
        }
        @Override
        protected void onPostExecute(List<Word> words) {
            if(words!= null){
                Collections.reverse(words);
                mWordList.clear();
                mWordList.addAll(words);
                wordListAdapter.notifyDataSetChanged();
            }
        }
    }


}
