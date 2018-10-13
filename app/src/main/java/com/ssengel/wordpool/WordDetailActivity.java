package com.ssengel.wordpool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssengel.wordpool.helper.CategoryToResorceId;
import com.ssengel.wordpool.model.Word;

import org.w3c.dom.Text;

import maes.tech.intentanim.CustomIntent;

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

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"right-to-left");
    }
}
