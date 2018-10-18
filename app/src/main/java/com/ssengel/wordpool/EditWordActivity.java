package com.ssengel.wordpool;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssengel.wordpool.DAO.WordDAO;
import com.ssengel.wordpool.asyncResponce.WordObjectCallBack;
import com.ssengel.wordpool.helper.CategoryToResorceId;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        word = (Word) getIntent().getExtras().get("word");

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

                pDialog.setMessage("Updating word ..");
                showDialog();

                Word mWord = new Word();
                mWord.set_id(word.get_id() +1 );
                mWord.setEng(edtEng.getText().toString());
                mWord.setTr(edtTr.getText().toString());
                mWord.setSentence(edtSentence.getText().toString());
                mWord.setCategory(sprCategory.getSelectedItem().toString());


                wordDAO.updateWord(mWord, new WordObjectCallBack() {
                    @Override
                    public void processFinish(Word word) {
                        hideDialog();
                        finish();
                    }

                    @Override
                    public void responseError(Error error) {
                        Snackbar.make(coordinatorEditWord, error.toString(),Snackbar.LENGTH_LONG).show();
                        hideDialog();
                    }
                });






            }
        });
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
