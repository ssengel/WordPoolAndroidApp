package com.ssengel.wordpool.DAO;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.helper.MyVolley;
import com.ssengel.wordpool.model.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordDAO {

    private ArrayList<Word> mWordList;

    public void updateWord(Word word, final WordObjectCallback callBack){
        JSONObject params = new JSONObject();
        try {
            params.put("eng", word.getEng());
            params.put("tr", word.getTr());
            params.put("sentence", word.getSentence());
            params.put("category", word.getCategory());
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, Config.URL_WORD()+word.get_id(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Word word = new Gson().fromJson(response.toString(), new TypeToken<Word>(){}.getType());
                callBack.successful(word);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.fail(new Error(error));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };


        MyVolley.getInstance().addToRequestQueue(req);
    }

    public void getWord(String id, final WordObjectCallback callBack){
        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.GET, Config.URL_WORD() + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Word word = new Gson().fromJson(response.toString(), new TypeToken<Word>(){}.getType());
                callBack.successful(word);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.fail(new Error(error));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };

        MyVolley.getInstance().addToRequestQueue(deleteRequest);
    }

    public void deleteWord(String _id, final WordObjectCallback callBack){
        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, Config.URL_WORD() + _id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.successful(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.fail(new Error(error));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };

        MyVolley.getInstance().addToRequestQueue(deleteRequest);
    }

    public void createWord(Word word, final WordObjectCallback callBack){
        JSONObject params = new JSONObject();
        try {
            params.put("eng", word.getEng());
            params.put("tr", word.getTr());
            params.put("sentence", word.getSentence());
            params.put("category", word.getCategory());
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.URL_WORD(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Word word = new Gson().fromJson(response.toString(), new TypeToken<Word>(){}.getType());
                callBack.successful(word);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.fail(new Error(error));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };


        MyVolley.getInstance().addToRequestQueue(req);
    }

    public void getWords(final WordListCallback callBack) {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, Config.URL_WORD(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<Word> mWordList = new Gson().fromJson(response.toString(), new TypeToken<List<Word>>(){}.getType());
                callBack.successful(mWordList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.fail(new Error(error));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };

        MyVolley.getInstance().addToRequestQueue(arrayRequest);
    }

    public interface WordListCallback {
        void successful(List list);
        void fail(Error error);
    }

    public interface WordObjectCallback {
        void successful(Word word);
        void fail(Error error);
    }

}
