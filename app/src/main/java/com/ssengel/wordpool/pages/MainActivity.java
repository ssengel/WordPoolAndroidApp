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
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ssengel.wordpool.DAO.PoolDAO;
import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.LocalDAO.OperationRepo;
import com.ssengel.wordpool.LocalDAO.PWordRepo;
import com.ssengel.wordpool.LocalDAO.PoolRepo;
import com.ssengel.wordpool.LocalDAO.WordRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.BottomNavigationBehavior;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.model.Operation;
import com.ssengel.wordpool.model.PWord;
import com.ssengel.wordpool.model.Pool;
import com.ssengel.wordpool.model.Word;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


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


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Boolean library = sharedPref.getBoolean(Config.LIBRARY_KEY, false);

        if(!library){
            if(isInternetAvailable(getApplicationContext())){
                //fetch pools from web
                getPools();
                //todo: fetch words if there are operation in web
            }else{
                new showMessage().execute("Your system is no up to date.\n No internet Connection..");
            }
        }else if(isInternetAvailable(getApplicationContext())){
//            new GetOperationsToLocal().execute();
            //todo: operasyolari kontrol et
        }else{

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
//                            Toast.makeText(getApplicationContext(), "Couldn't insert pools or pwords", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void err(Error error) {
                        hideDialog();
//                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void err(Error error) {
                hideDialog();
//                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
            new showMessage().execute("Your system is up to date..");
        }
    }

    private class GetOperationsToLocal extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            List<Operation> operations= operationRepo.getAllOperations();
            Log.e("OPERATIONS: =>> ", operations.toString());
            if (operations.size() > 0){
                for(Operation op: operations){
                    makeLocalOperation(op);
                }
            }
            return null;
        }
    }

    private void makeLocalOperation(final Operation op){

        String operationType = op.getType();
        final Word word = wordRepo.getWordById(op.getWordId());

        if(isInternetAvailable(getApplicationContext())){
            if(operationType.equals("insert")){
                wordDAO.createWord(word, new WordDAO.WordObjectCallback() {
                    @Override
                    public void successful(Word word) {
                       deleteOperationFromLocal(op, word);
                    }
                    @Override
                    public void fail(Error error) {
                        new showMessage().execute("fail operation for : "+ word.getEng());
                    }
                });
            }
            if(operationType.equals("delete")){
                wordDAO.deleteWord(op.getWordId(), new WordDAO.WordObjectCallback() {
                    @Override
                    public void successful(Word word) {
                        deleteOperationFromLocal(op, word);
                    }
                    @Override
                    public void fail(Error error) {
                        new showMessage().execute("fail operation for : "+ word.getEng());
                    }
                });
            }
        }
    }


    private void deleteOperationFromLocal(final Operation op, final Word word){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(op.getType().equals("insert")){
                    operationRepo.deleteOperation(op.get_id());
                    wordRepo.updateWordId(op.getWordId(), word.get_id());
                }
                if(op.getType().equals("delete")){
                    operationRepo.deleteOperation(op.get_id());
                }

            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true); return;
    }

}
