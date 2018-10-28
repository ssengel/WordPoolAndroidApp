package com.ssengel.wordpool.pages;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.LocalDAO.PWordRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.adapter.PoolDetailAdapter;
import com.ssengel.wordpool.helper.RecyclerItemTouchHelper;
import com.ssengel.wordpool.model.PWord;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;
import java.util.List;

public class PoolDetailActivity  extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<PWord> pWordList;
    private RecyclerView recyclerView;
    private PoolDetailAdapter poolDetailAdapter;

    private PWordRepo pWordRepo;
    private String poolId;
    private String poolName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_detail);

        poolId = getIntent().getStringExtra("poolId");
        poolName = getIntent().getStringExtra("poolName");

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(poolName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        initViews();

        //swipe
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    private void initViews(){
        context = getApplicationContext();
        pWordRepo = new PWordRepo(context);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorPoolDetail);
        pWordList = new ArrayList<>();
        poolDetailAdapter = new PoolDetailAdapter(context, pWordList, poolName);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(poolDetailAdapter);

        new GetPWordsToLocal().execute(poolId);

    }

    private class GetPWordsToLocal extends AsyncTask<String, Void, List<PWord>> {
        @Override
        protected List<PWord> doInBackground(String... strings) {
            return pWordRepo.getPWordsByPoolId(poolId);
        }
        @Override
        protected void onPostExecute(List<PWord> pWords) {
            pWordList.clear();
            pWordList.addAll(pWords);
            poolDetailAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof PoolDetailAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            final String name = pWordList.get(viewHolder.getAdapterPosition()).getEng();

            // backup of removed item for undo purpose
            final PWord deletedItem = pWordList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            poolDetailAdapter.removeItem(viewHolder.getAdapterPosition(), new WordDAO.WordObjectCallback() {
                @Override
                public void successful(Word word) {
                    //removo word from local database
                    new DeletePWord().execute(deletedItem.get_id());
                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, name + " added your pool!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // undo is selected, restore the deleted item
                            poolDetailAdapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }

                @Override
                public void fail(Error error) {
                    Snackbar.make(coordinatorLayout,
                            name + " couldn't add your pool!!!\n"+ error.toString(),
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            });


        }
    }
    private class DeletePWord extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            pWordRepo.deletePWord(strings[0]);
            return null;
        }
    }
}
