package com.ssengel.wordpool.pages;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.LocalDAO.OperationRepo;
import com.ssengel.wordpool.LocalDAO.WordRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Operation;
import com.ssengel.wordpool.model.Word;


public class WordDetailActivity extends AppCompatActivity {

    private String wordId;
    private Word globalWord;
    private WordDAO wordDAO = new WordDAO();
    private WordRepo wordRepo;
    private OperationRepo operationRepo;

    private CoordinatorLayout coordinatorWordDetail;
    private ImageView imgCategory;
    private TextView txtEng;
    private TextView txtTr;
    private TextView txtSentence;
    private Toolbar toolbar;

    private AlertDialog.Builder builder;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        wordRepo = new WordRepo(getApplicationContext());
        operationRepo = new OperationRepo(getApplicationContext());


        wordId = getIntent().getStringExtra("wordId");

        new GetWordFromLocal().execute(wordId);

        initViews();


        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private class GetWordFromLocal extends AsyncTask<String, Void,Word>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Word doInBackground(String... ids) {

            Log.e("doInBack" , ids[0]);
            Word word = wordRepo.getWordById(ids[0]);
            return word;
        }

        @Override
        protected void onPostExecute(Word word) {
            globalWord = word;
            setFields(word);
        }
    }

    private void initViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorWordDetail = (CoordinatorLayout) findViewById(R.id.coordinatorWordDetail);
        imgCategory = (ImageView) findViewById(R.id.imgCategory);
        txtEng = (TextView) findViewById(R.id.txtEng);
        txtTr = (TextView) findViewById(R.id.txtTr);
        txtSentence = (TextView) findViewById(R.id.txtSentence);

        //builder
        builder = new AlertDialog.Builder(WordDetailActivity.this);
        createDialog();

    }

    private void setFields(Word word){
        ((TextView)findViewById(R.id.wordId)).setText(word.get_id());
        imgCategory.setImageResource(CategoryToResorceId.getImageResource(word.getCategory()));
        txtEng.setText(word.getEng());
        txtTr.setText(word.getTr());
        txtSentence.setText(word.getSentence());
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_word_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }if(item.getItemId() == R.id.action_edit){
            Intent intent = new Intent(getApplicationContext(), EditWordActivity.class);
            intent.putExtra("word", globalWord);
            startActivity(intent);
        }if(item.getItemId() == R.id.action_delete){
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDialog(){

        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                deleteWordFromLocal(wordId);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert = builder.create();

    }

    private void deleteWordFromLocal(final String wordId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wordRepo.deleteWord(wordId);
                    operationRepo.deleteOperationsByWordId(wordId);
                    if(wordId.length()> 13) {//web e senkron edilmis ise
                        Operation operation = new Operation();
                        operation.setWordId(wordId);
                        operation.setType("delete");
                        operationRepo.insertOperation(operation);
                    }

                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
