package com.ssengel.wordpool.DAO;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.helper.MyVolley;
import com.ssengel.wordpool.model.PWord;
import com.ssengel.wordpool.model.Pool;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoolDAO {

    public void getAllPools(final PoolListCallback callBack){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, Config.URL_POOL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Pool> poolList = new Gson().fromJson(response.toString(), new TypeToken<List<Pool>>(){}.getType());
                callBack.succes(poolList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.err(new Error(error));
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

    public void getAllPWords(final PWordListCallBack callBack){
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, Config.URL_PWORD, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<PWord> pWords = new Gson().fromJson(response.toString(), new TypeToken<List<PWord>>(){}.getType());
                callBack.succes(pWords);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.err(new Error(error));
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

    public interface PWordListCallBack{
        void succes(List<PWord> pWordList);
        void err(Error error);
    }

    public interface PoolListCallback{
        void succes(List<Pool> poolList);
        void err(Error error);
    }
}
