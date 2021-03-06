package com.ssengel.wordpool.pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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

public class EditWordActivity extends AppCompatActivity {

    private Word word;
    private WordDAO wordDAO = new WordDAO();

    private CoordinatorLayout coordinatorEditWord;
    private EditText edtEng;
    private EditText edtTr;
    private EditText edtSentence;
    private Button btnSave;
    private AppCompatSpinner sprCategory;
    private Toolbar toolbar;
    private ProgressDialog pDialog;
    private WordRepo wordRepo;
    private OperationRepo operationRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        word = (Word) getIntent().getExtras().get("word");
        wordRepo = new WordRepo(getApplicationContext());
        operationRepo = new OperationRepo(getApplicationContext());

        initViews();

        setFields();

        initListeners();

        //Action Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void initViews(){

        coordinatorEditWord = (CoordinatorLayout) findViewById(R.id.coordinatorEditWord);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edtEng = (EditText) findViewById(R.id.edtEng);
        edtTr = (EditText) findViewById(R.id.edtTr);
        edtSentence = (EditText) findViewById(R.id.edtSentence);
        btnSave = (Button) findViewById(R.id.btnSave);
        sprCategory = (AppCompatSpinner) findViewById(R.id.sprCategory);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }
    private void initListeners(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                closeKeyboard();

                word.setEng(edtEng.getText().toString());
                word.setTr(edtTr.getText().toString());
                word.setSentence(edtSentence.getText().toString());
                word.setCategory(sprCategory.getSelectedItem().toString());

                updateWord(word);
            }
        });
    }

    private void updateWord(final Word word){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    wordRepo.updateWord(word);
                    operationRepo.insertOperation(new Operation("update",word.get_id()));
                    finish();
                }catch (Exception e){
                    Snackbar.make(coordinatorEditWord, e.toString(),Snackbar.LENGTH_LONG).show();
                }

            }
        }).start();

    }


    private void setFields(){
        edtEng.setText(word.getEng());
        edtTr.setText(word.getTr());
        edtSentence.setText(word.getSentence());

        //spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprCategory.setAdapter(adapter);
        sprCategory.setSelection(adapter.getPosition(word.getCategory()));


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

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
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
}
