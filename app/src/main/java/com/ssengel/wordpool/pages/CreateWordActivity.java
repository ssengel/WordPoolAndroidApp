package com.ssengel.wordpool.pages;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.ssengel.wordpool.globalDAO.WordDAO;
import com.ssengel.wordpool.LocalDAO.OperationRepo;
import com.ssengel.wordpool.LocalDAO.WordRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.model.Operation;
import com.ssengel.wordpool.model.Word;

public class CreateWordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatSpinner sprCategory;
    private Button btnCreate;
    private EditText edtEng;
    private EditText edtTr;
    private EditText edtSentence;

    private WordDAO wordDAO= new WordDAO();
    private WordRepo wordRepo;
    private OperationRepo operationRepo;

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

            new insertWord().execute(word);
            }
        });
    }

    private class insertWord extends AsyncTask<Word, Void, Void> {

        @Override
        protected Void doInBackground(Word... words) {
            try {
                closeKeyboard();
                wordRepo.insertWord(words[0]);
                Operation op = new Operation();
                op.setWordId(words[0].get_id());
                op.setType("insert");
                operationRepo.insertOperation(op);

                finish();

            }catch (Exception e){
                Snackbar.make(findViewById(R.id.coordinatorCreateWord), e.toString(),Snackbar.LENGTH_LONG).show();
            }
            return null;
        }

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

        wordRepo = new WordRepo(getApplicationContext());
        operationRepo = new OperationRepo(getApplicationContext());

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
