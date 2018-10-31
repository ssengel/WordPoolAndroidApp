package com.ssengel.wordpool.globalDAO;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.helper.MyVolley;
import com.ssengel.wordpool.model.GlobalOperation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationDAO {

    public List<GlobalOperation> syncGetOperations(){
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, Config.URL_OPERATION(),null,future ,future){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };
        MyVolley.getInstance().addToRequestQueue(req);
        try {
            JSONArray response = future.get(); // this will block (forever)
            List<GlobalOperation> operations = new Gson().fromJson(response.toString(), new TypeToken<List<GlobalOperation>>(){}.getType());
            return operations;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Boolean syncDeleteGlobalOperation(String operationId){
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, Config.URL_OPERATION() + operationId,null,future,future){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-access-token", Config.TOKEN);
                return params;
            }
        };
        MyVolley.getInstance().addToRequestQueue(req);
        try {
            JSONObject response = future.get(); // this will block (forever)
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
