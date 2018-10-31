package com.ssengel.wordpool.pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssengel.wordpool.globalDAO.AuthDAO;

import com.ssengel.wordpool.globalDAO.PoolDAO;
import com.ssengel.wordpool.LocalDAO.PWordRepo;
import com.ssengel.wordpool.LocalDAO.PoolRepo;
import com.ssengel.wordpool.R;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.syncronization.Sync;

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
                if(MainActivity.isInternetAvailable(getApplicationContext())){
                    String email = txtEmail.getText().toString();
                    String password = txtPassword.getText().toString();
                    login(email,password);
                }else{
                    Toast.makeText(getApplicationContext(),"No internet Connection Detected.",Toast.LENGTH_LONG).show();
                }
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

                sync();
            }
            @Override
            public void fail(Error error) {
                hideDialog();
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

        if(fEntry){
            Config.TOKEN = sharedPref.getString(Config.TOKEN_KEY,null);
            Config.USER_ID = sharedPref.getString(Config.USER_ID_KEY,null);

            if(MainActivity.isInternetAvailable(getApplicationContext())) {
                sync();
            }else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }else{
            //show login page
        }


    }

    private void sync(){
        try{
            pDialog.setMessage("\tSync..");
            showDialog();

            Sync sync = new Sync(getApplicationContext(), null);
            sync.execute();

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            hideDialog();
        }catch (Exception e){
            hideDialog();
            Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG).show();
        }


    }
}

