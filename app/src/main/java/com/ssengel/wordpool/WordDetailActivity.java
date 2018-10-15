package com.ssengel.wordpool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Word;


public class WordDetailActivity extends AppCompatActivity {

    private Word word;

    private ImageView imgCategory;
    private TextView txtEng;
    private TextView txtTr;
    private TextView txtSentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        word = (Word) getIntent().getExtras().get("word");
        init();
        setFields();
        setToolbar(word.getEng());

    }

    private void init(){
        imgCategory = (ImageView) findViewById(R.id.imgCategory);
        txtEng = (TextView) findViewById(R.id.txtEng);
        txtTr = (TextView) findViewById(R.id.txtTr);
        txtSentence = (TextView) findViewById(R.id.txtSentence);
    }

    private void setFields(){
        imgCategory.setImageResource(CategoryToResorceId.getImageResource(word.getCategory()));
        txtEng.setText(word.getEng());
        txtTr.setText(word.getTr());
        txtSentence.setText(word.getSentence());
    }

    private void setToolbar(String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title.toUpperCase());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
