package com.ssengel.wordpool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.asyncResponce.WordObjectCallBack;
import com.ssengel.wordpool.model.Word;

public class CreateWordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatSpinner sprCategory;
    private Button btnCreate;
    private EditText edtEng;
    private EditText edtTr;
    private EditText edtSentence;

    private WordDAO wordDAO= new WordDAO();

    private void initViews(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sprCategory = (AppCompatSpinner) findViewById(R.id.spinner);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        edtEng = (EditText) findViewById(R.id.edtEng);
        edtTr = (EditText) findViewById(R.id.edtTr);
        edtSentence = (EditText) findViewById(R.id.edtSentence);

    }

    private void initListeners(){
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Word word = new Word();
                word.setEng(edtEng.getText().toString());
                word.setTr(edtTr.getText().toString());
                word.setSentence(edtSentence.getText().toString());
                word.setCategory(sprCategory.getSelectedItem().toString());

                wordDAO.createWord(word, new WordObjectCallBack() {
                    @Override
                    public void processFinish(Word word) {
                        finish();
                        Toast.makeText(getApplicationContext(),"Created new word.",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void responseError(Error error) {
                        closeKeyboard();
                        Snackbar.make(view, "Couldn't created word !!\n" + error.toString(),Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word);

        initViews();
        initListeners();

        //action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprCategory.setAdapter(adapter);

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
}
