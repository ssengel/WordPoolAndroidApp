package com.ssengel.wordpool;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.asyncResponce.WordObjectCallBack;
import com.ssengel.wordpool.model.Word;

public class CreateWordActivity extends AppCompatActivity {

    private Button btnCreate;
    private EditText edtEng;
    private EditText edtTr;
    private EditText edtSentence;

    private WordDAO wordDAO;

    private void init(){
        btnCreate = (Button) findViewById(R.id.btnCreate);
        edtEng = (EditText) findViewById(R.id.edtEng);
        edtTr = (EditText) findViewById(R.id.edtTr);
        edtSentence = (EditText) findViewById(R.id.edtSentence);

        wordDAO = new WordDAO();
    }

    private void initListeners(){
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word();
                word.setEng(edtEng.getText().toString());
                word.setTr(edtTr.getText().toString());
                word.setSentence(edtSentence.getText().toString());
                wordDAO.createWord(word, new WordObjectCallBack() {
                    @Override
                    public void processFinish(Word word) {
                        Toast.makeText(getApplicationContext(),"Created new word.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                    @Override
                    public void responseError(Error error) {
                        Toast.makeText(getApplicationContext(),"Couldn't created new word\n." +error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        initListeners();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
