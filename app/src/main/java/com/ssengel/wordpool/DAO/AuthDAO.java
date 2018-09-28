package com.ssengel.wordpool.DAO;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ssengel.wordpool.helper.Config;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssengel.wordpool.asyncResponce.AuthServiceCallBack;
import com.ssengel.wordpool.helper.MyVolley;

import static android.content.ContentValues.TAG;

public class AuthDAO {

    public void login(String email, String password, final AuthServiceCallBack callBack) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "login: email veya password parametre olarak eklenemedi." + e.toString());
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Config.TOKEN = response.getString("token");
                    Config.USER_ID = response.getJSONObject("user").getString("_id");
                    callBack.processFinished();
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: "+ e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.responseError(error);
            }
        });

        MyVolley.getInstance().addToRequestQueue(objectRequest);
    }
}
