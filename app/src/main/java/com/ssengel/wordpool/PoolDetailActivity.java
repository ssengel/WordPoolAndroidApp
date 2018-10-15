package com.ssengel.wordpool;

import android.graphics.Color;
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

import com.ssengel.wordpool.adapter.PoolDetailAdapter;
import com.ssengel.wordpool.adapter.WordListAdapter;
import com.ssengel.wordpool.asyncResponce.WordObjectCallBack;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.helper.RecyclerItemTouchHelper;
import com.ssengel.wordpool.model.Pool;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;

public class PoolDetailActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private CoordinatorLayout coordinatorLayout;
    private Pool pool;
    private ArrayList<Word> wordList;
    private RecyclerView recyclerView;
    private PoolDetailAdapter poolDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_detail);

        pool =(Pool) getIntent().getSerializableExtra("pool");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(pool.getName().toUpperCase());

        init();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    private void init(){


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        wordList = pool.getWords();
        poolDetailAdapter = new PoolDetailAdapter(getApplicationContext(), wordList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(poolDetailAdapter);



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
            final String name = wordList.get(viewHolder.getAdapterPosition()).getEng();

            // backup of removed item for undo purpose
            final Word deletedWord = wordList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            poolDetailAdapter.removeItem(viewHolder.getAdapterPosition(), new WordObjectCallBack() {
                @Override
                public void processFinish(Word word) {
                    //removo word from local database
                    ArrayList<Word> wordList;
                    for (Pool pool: Config.poolList) {
                        if(pool.getName().equals(deletedWord.getCategory())){
                            pool.getWords().remove(deletedIndex);
                        }
                    }

                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, name + " added your pool!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // undo is selected, restore the deleted item
                            poolDetailAdapter.restoreItem(deletedWord, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }

                @Override
                public void responseError(Error error) {
                    Snackbar.make(coordinatorLayout,
                            name + " couldn't add your pool!!!\n"+ error.toString(),
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            });


        }
    }
}
