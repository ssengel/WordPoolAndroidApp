package com.ssengel.wordpool.DAO;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.ssengel.wordpool.asyncResponce.WordObjectCallBack;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.asyncResponce.WordListCallBack;
import com.ssengel.wordpool.helper.MyVolley;
import com.ssengel.wordpool.model.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordDAO {

    private RequestQueue mRequestQueue;
    private ArrayList<Word> mWordList = new ArrayList<>();

    public WordDAO(Context context) {
        mRequestQueue = MyVolley.getInstance(context).getRequestQueue();
    }

    public void createWord(Word word, final WordObjectCallBack callBack){
        JSONObject params = new JSONObject();
        try {
            params.put("eng", word.getEng());
            params.put("tr", word.getTr());
            params.put("sentence", word.getSentence());
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.getUrlWord(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Word word = new Word();
                try {
                    word.set_id(response.getString("_id"));
                    word.setUserId(response.getString("userId"));
                    word.setEng(response.getString("eng"));
                    word.setTr(response.getString("tr"));
                    word.setSentence(response.getString("sentence"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callBack.processFinish(word);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.responseError(new Error(error));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };

        mRequestQueue.add(req);
    }

    public void getWords(final WordListCallBack callBack) {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, Config.getUrlWord(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                ArrayList<Word> mWordList = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Word word = new Word();
                        word.set_id(jsonObject.getString("_id"));
                        word.setUserId(jsonObject.getString("userId"));
                        word.setEng(jsonObject.getString("eng"));
                        word.setTr(jsonObject.getString("tr"));
                        word.setSentence(jsonObject.getString("sentence"));

                        mWordList.add(word);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                callBack.processFinish(mWordList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.responseError(new Error(error));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };

        mRequestQueue.add(arrayRequest);
    }
}
