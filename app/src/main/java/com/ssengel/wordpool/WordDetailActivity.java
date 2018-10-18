package com.ssengel.wordpool;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.asyncResponce.WordObjectCallBack;
import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Word;


public class WordDetailActivity extends AppCompatActivity {

    private Word word;
    private WordDAO wordDAO = new WordDAO();

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


        word = (Word) getIntent().getExtras().get("word");
        initViews();
        setFields();

        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

    private void setFields(){
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
            intent.putExtra("word", word);
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

                deleteWord(word.get_id());
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

    private void deleteWord(String id){
            wordDAO.deleteWord(id+1, new WordObjectCallBack() {
                @Override
                public void processFinish(Word word) {
                    finish();
                }
                @Override
                public void responseError(Error error) {
                    Snackbar.make(coordinatorWordDetail, error.toString(), Snackbar.LENGTH_LONG).show();
                }
            });
    }


}
