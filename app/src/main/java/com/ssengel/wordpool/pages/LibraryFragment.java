package com.ssengel.wordpool.pages;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssengel.wordpool.globalDAO.PoolDAO;
import com.ssengel.wordpool.LocalDAO.PWordRepo;
import com.ssengel.wordpool.LocalDAO.PoolRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.adapter.PoolListAdapter;
import com.ssengel.wordpool.helper.GridSpacingItemDecoration;
import com.ssengel.wordpool.model.Pool;

import java.util.ArrayList;
import java.util.List;


public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Pool> poolList;
    private PoolListAdapter poolListAdapter;
    private PoolDAO poolDAO = new PoolDAO();
    private PoolRepo poolRepo;
    private PWordRepo pWordRepo;
    private SharedPreferences sharedPref;


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

        View view = inflater.inflate(R.layout.fragment_library, container, false);

        poolRepo = new PoolRepo(getContext());
        pWordRepo = new PWordRepo(getContext());
//        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);



        recyclerView = view.findViewById(R.id.categoryList);
        poolList = new ArrayList<>();
        poolListAdapter = new PoolListAdapter(getActivity(), poolList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(poolListAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        new GetPoolsFromLocal().execute();

        return view;
    }



//    private void checkIsLocalPoolExist(){
//        Boolean state = getLocalPoolState();
//        if(!state){
//            getPools();
//        }else {
//            new GetPoolsFromLocal().execute();
//        }
//
//    }
//    private boolean getLocalPoolState(){
//        Boolean state = sharedPref.getBoolean(Config.IS_LOCAL_POOL_KEY,false);
//        return state;
//    }
//    private void setLocalPoolState(boolean state){
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean(Config.IS_LOCAL_POOL_KEY, state);
//        editor.commit();
//    }


    private class GetPoolsFromLocal extends AsyncTask<Void, Void, List<Pool>>{

        @Override
        protected List<Pool> doInBackground(Void... voids) {
            try{
                return poolRepo.getAllPools();
            }catch (Exception e){
                new MainActivity.showMessage().execute(e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Pool> pools) {
            poolList.clear();
            poolList.addAll(pools);
            poolListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
