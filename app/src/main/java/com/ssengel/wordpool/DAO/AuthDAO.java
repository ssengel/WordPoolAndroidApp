package com.ssengel.wordpool.DAO;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ssengel.wordpool.helper.Config;
import org.json.JSONException;
import org.json.JSONObject;
import com.ssengel.wordpool.helper.MyVolley;

import static android.content.ContentValues.TAG;

public class AuthDAO {

    public void login(String email, String password, final AuthCallback callBack) {

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
                    String token = response.getString("token");
                    String userId = response.getJSONObject("user").getString("_id");
                    callBack.successful(userId, token);
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: "+ e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.fail(new Error(error));
            }
        });

        MyVolley.getInstance().addToRequestQueue(objectRequest);
    }

    public interface AuthCallback{
        void successful(String userId, String token);
        void fail(Error err);
    }
}
