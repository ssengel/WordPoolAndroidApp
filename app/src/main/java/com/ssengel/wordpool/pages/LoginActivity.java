package com.ssengel.wordpool.pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssengel.wordpool.DAO.AuthDAO;

import com.ssengel.wordpool.DAO.PoolDAO;
import com.ssengel.wordpool.LocalDAO.PWordRepo;
import com.ssengel.wordpool.LocalDAO.PoolRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.model.PWord;
import com.ssengel.wordpool.model.Pool;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN ACTIVITY";
    //UI references
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;

    //instance variables
    private AuthDAO authDAO;
    private PoolDAO poolDAO;
    private PoolRepo poolRepo;
    private PWordRepo pWordRepo;
    private ProgressDialog pDialog;


    private void initVars(){
        //UI
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //instance
        authDAO = new AuthDAO();
        poolDAO = new PoolDAO();
        poolRepo = new PoolRepo(getApplicationContext());
        pWordRepo = new PWordRepo(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }
    private void initListeners(){

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                login(email,password);
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

    private void login(final String email, final String password){
        closeKeyboard();
        pDialog.setMessage("Logging in ...");
        showDialog();
        
        authDAO.login(email, password, new AuthDAO.AuthCallback() {
            @Override
            public void successful(String userId, String token) {

                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putBoolean(Config.FIRST_ENTRY_KEY, true);
                editor.putString(Config.TOKEN_KEY, token);
                editor.putString(Config.USER_ID_KEY, userId);
                editor.putString(Config.EMAIL_KEY, email);
                editor.putString(Config.PASSWORD_KEY, password);
                editor.commit();

                Config.TOKEN = token;
                Config.USER_ID = userId;
                hideDialog();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            @Override
            public void fail(Error error) {
                hideDialog();
                Log.i(TAG, "responseError: "+error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Boolean fEntry = sharedPref.getBoolean(Config.FIRST_ENTRY_KEY, false);

        initVars();
        initListeners();

        if(fEntry){//ilk giris daha onceden yapilmis
            //todo: internet is detected Sync
            Config.TOKEN = sharedPref.getString(Config.TOKEN_KEY,null);
            Config.USER_ID = sharedPref.getString(Config.USER_ID_KEY,null);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{
            //todo: display login page and just Sync from web to local
        }


    }



}
