package com.ssengel.wordpool.pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.ssengel.wordpool.globalDAO.PoolDAO;
import com.ssengel.wordpool.globalDAO.WordDAO;
import com.ssengel.wordpool.LocalDAO.OperationRepo;
import com.ssengel.wordpool.LocalDAO.PWordRepo;
import com.ssengel.wordpool.LocalDAO.PoolRepo;
import com.ssengel.wordpool.LocalDAO.WordRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.BottomNavigationBehavior;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.helper.MyVolley;
import com.ssengel.wordpool.model.PWord;
import com.ssengel.wordpool.model.Pool;
import com.ssengel.wordpool.model.Word;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static BottomNavigationView bottomNavigationView;
    public static CoordinatorLayout placeSnackBar;

    private PoolDAO poolDAO;
    private WordDAO wordDAO;
    private WordRepo wordRepo;
    private PoolRepo poolRepo;
    private PWordRepo pWordRepo;
    private OperationRepo operationRepo;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        placeSnackBar = (CoordinatorLayout) findViewById(R.id.placeSnackBar);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        poolDAO = new PoolDAO();
        wordDAO = new WordDAO();
        wordRepo = new WordRepo(getApplicationContext());
        poolRepo = new PoolRepo(getApplicationContext());
        pWordRepo = new PWordRepo(getApplicationContext());
        operationRepo = new OperationRepo(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //set bottom manu behavior
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        //initial fragment
        getSupportActionBar().setTitle("Pool");
        loadFragment(new PoolFragment());

        //sync control
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Boolean library = sharedPref.getBoolean(Config.LIBRARY_KEY, false);

        if(!library){
            if(isInternetAvailable(getApplicationContext())){
                //fetch pools from web
                getWords();
                getPools();

            }else{
                new showMessage().execute("Your system is not Sync..");
            }
        }



    }

    public void getWords(){
        wordDAO.getWords(new WordDAO.WordListCallback() {
            @Override
            public void successful(final List list) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            for (Word word: (List<Word>) list){
                                wordRepo.insertWord(word);
                            }
                            if(syncDeleteAllOperation()){
                                loadFragment(new PoolFragment());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
            @Override
            public void fail(Error error) {

            }
        });
    }

    public Boolean syncDeleteAllOperation()  {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, Config.URL_DELETE_OPERATIONS_BY_USER_ID(),null,future,future){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };

        MyVolley.getInstance().addToRequestQueue(req);
        try {
            JSONObject response = future.get(10, TimeUnit.SECONDS); // this will block (forever)
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_random:
                    loadFragment(new RandomFragment());
                    toolbar.setTitle("Random");
                    return true;
                case R.id.navigation_pool:
                    loadFragment(new PoolFragment());
                    toolbar.setTitle("Pool");
                    return true;
                case R.id.navigation_library:
                    loadFragment(new LibraryFragment());
                    toolbar.setTitle("Library");
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static class showMessage extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(200);
            Snackbar.make(placeSnackBar, strings[0], Snackbar.LENGTH_SHORT).show();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            bottomNavigationView.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(200);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void getPools(){
        showDialog();
        pDialog.setMessage("\tFetching pools..");
        poolDAO.getAllPools(new PoolDAO.PoolListCallback() {
            @Override
            public void succes(final List<Pool> pools) {
                pDialog.setMessage("\tFetching pWords..");
                poolDAO.getAllPWords(new PoolDAO.PWordListCallBack() {
                    @Override
                    public void succes(List<PWord> pWords) {
                        //set Pools and PWords
                        try{
                            pDialog.setMessage("\tInserting pWords and pools..");
                            new InsertPoolsToLocal().execute(pools, pWords);

                        }catch (Exception e){
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "Couldn't insert pools or pwords", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void err(Error error) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void err(Error error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private class InsertPoolsToLocal extends AsyncTask<List, Void, Void>{
        @Override
        protected Void doInBackground(List... lists) {

            try {
                for (Pool p: (List<Pool>)lists[0]){
                    poolRepo.insertPool(p);
                }
                for(PWord pW: (List<PWord>) lists[1]){
                    pWordRepo.insertPWord(pW);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                new showMessage().execute(e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putBoolean(Config.LIBRARY_KEY, true);
            editor.commit();
            hideDialog();
        }
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true); return;
    }

}
