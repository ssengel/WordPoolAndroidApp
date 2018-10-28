package com.ssengel.wordpool.syncronization;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssengel.wordpool.LocalDAO.OperationRepo;
import com.ssengel.wordpool.LocalDAO.WordRepo;
import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.helper.MyVolley;
import com.ssengel.wordpool.model.Operation;
import com.ssengel.wordpool.model.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sync extends AsyncTask {

    private ProgressDialog progressDialog;
    private Context context;
    private OperationRepo operationRepo;
    private WordRepo wordRepo;
    private CustomCallback callback;

    //callback de alicak
    public Sync(Context context, CustomCallback callback){
        this.context = context;
        this.callback = callback;
        progressDialog = new ProgressDialog(context);
        operationRepo = new OperationRepo(context);
        wordRepo  = new WordRepo(context);
    }

    public Word syncCreateWord(Word word){

        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        JSONObject params = new JSONObject();
        try {
            params.put("eng", word.getEng());
            params.put("tr", word.getTr());
            params.put("sentence", word.getSentence());
            params.put("category", word.getCategory());
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Config.URL_WORD(), params, future,future){
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
            Word newWord = new Gson().fromJson(response.toString(), new TypeToken<Word>(){}.getType());
            return newWord;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Boolean syncDeleteOperation(String operationId){
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

    public Boolean syncDeleteWord(String wordId){
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, Config.URL_WORD() + wordId,null,future,future){
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
    public Word syncGetWord(String wordId){
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Config.URL_WORD() + wordId,null,future,future){
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
            Word word = new Gson().fromJson(response.toString(), new TypeToken<Word>(){}.getType());
            return word;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void makeLocalOperation(Operation op){
        String operationType = op.getType();
        Word word = wordRepo.getWordById(op.getWordId());

        if(operationType.equals("insert")){
            Word createdWord = syncCreateWord(word);
            if(createdWord != null){
                //delete operation from local db
                operationRepo.deleteOperation(op.get_id());
                //update word with new word id
                wordRepo.updateWordId(op.getWordId(), createdWord.get_id());
            }
        }
        if(operationType.equals("delete")){
            //delete word from web db
            Boolean result = syncDeleteWord(op.getWordId());
            if(result){
                //delete operation from local db
                operationRepo.deleteOperation(op.get_id());
            }
        }
    }
    private void makeGlobalOperation(GlobalOperation op) {
        String operationType = op.getType();


        if(operationType.equals("insert")){
            Word word = syncGetWord(op.getWordId());
            if(word != null){
                wordRepo.insertWord(word);
                //delete operation from global
                Boolean result = syncDeleteOperation(op.get_id());
                if(!result){
                    //todo: eklenen word silinmeli
                }
            }

        }else if(operationType.equals("delete")){
            wordRepo.deleteWord(op.getWordId());
            operationRepo.deleteOperationsByWordId(op.getWordId());
            Boolean result = syncDeleteOperation(op.get_id());
            if(!result){
                //todo: silinen word eklenmeli
            }
        }

    }

    private void simulation(long milisecond){
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("\tSync..");
        progressDialog.show();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        //global sync
        progressDialog.setMessage("\tSync Web..");

        List<GlobalOperation> globalOperations = syncGetOperations();
        if (globalOperations != null){
            for (GlobalOperation op: globalOperations){
                makeGlobalOperation(op);
            }
        }
        simulation(500);

        //local sync
        progressDialog.setMessage("\tSync local..");
        List<Operation> localOperations = operationRepo.getAllOperations();
        if(localOperations != null){
            for (Operation op: localOperations){
                makeLocalOperation(op);
            }
        }
        simulation(500);
        progressDialog.setMessage("\tSync Successful.");
        simulation(500);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(callback != null){
            callback.successful();
        }
        progressDialog.dismiss();
    }

    public interface CustomCallback{
        void successful();
    }

    private class GlobalOperation{
        String _id;
        String userId;
        String wordId;
        String type;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getWordId() {
            return wordId;
        }

        public void setWordId(String wordId) {
            this.wordId = wordId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
