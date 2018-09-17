package com.ssengel.wordpool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ssengel.wordpool.DAO.AuthDAO;

import com.ssengel.wordpool.asyncResponce.AuthServiceCallBack;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN ACTIVITY";
    //UI references
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;

    //instance variables
    private AuthDAO authDAO;
    private ProgressDialog pDialog;


    private void initVars(){
        //UI
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //instance
        authDAO = new AuthDAO(getApplicationContext());
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

    private void login(String email, String password){
        pDialog.setMessage("Logging in ...");
        showDialog();
        
        authDAO.login(email, password, new AuthServiceCallBack() {
            @Override
            public void processFinished() {
                hideDialog();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            @Override
            public void responseError(VolleyError error) {
                hideDialog();
                Log.i(TAG, "responseError: "+error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVars();
        initListeners();
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
