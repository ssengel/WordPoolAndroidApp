package com.ssengel.wordpool.syncronization;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ssengel.wordpool.LocalDAO.OperationRepo;
import com.ssengel.wordpool.LocalDAO.WordRepo;
import com.ssengel.wordpool.globalDAO.OperationDAO;
import com.ssengel.wordpool.globalDAO.WordDAO;

import com.ssengel.wordpool.model.GlobalOperation;
import com.ssengel.wordpool.model.Operation;
import com.ssengel.wordpool.model.Word;

import java.util.List;


public class Sync extends AsyncTask {

    private ProgressDialog progressDialog;
    private Context context;
    private OperationRepo operationRepo;
    private WordRepo wordRepo;
    private WordDAO wordDAO = new WordDAO();
    private OperationDAO operationDAO = new OperationDAO();
    private CustomCallback callback;

    public Sync(Context context, CustomCallback callback){
        this.context = context;
        this.callback = callback;
        progressDialog = new ProgressDialog(context);
        operationRepo = new OperationRepo(context);
        wordRepo  = new WordRepo(context);
    }



    @Override
    protected void onPreExecute() {
        //todo: progressbar da sorun var
    }

    @Override
    protected Object doInBackground(Object[] objects) {


        //global sync
        List<GlobalOperation> globalOperations = operationDAO.syncGetOperations();
        if (globalOperations != null){
            for (GlobalOperation op: globalOperations){
                makeGlobalOperation(op);
            }
        }

        //local sync
        List<Operation> localOperations = operationRepo.getAllOperations();
        if(localOperations != null){
            for (Operation op: localOperations){
                makeLocalOperation(op);
            }
        }
        simulation(200);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(callback != null){
            callback.successful();
        }
    }



    private void makeLocalOperation(Operation op){
        String operationType = op.getType();
        Word word = wordRepo.getWordById(op.getWordId());

        if(operationType.equals("insert")){
            Word createdWord = wordDAO.syncCreateWord(word);
            if(createdWord != null){
                //delete operation from local db
                operationRepo.deleteOperation(op.get_id());
                //update word with new word id
                wordRepo.updateWordId(op.getWordId(), createdWord.get_id());
            }
        }
        if(operationType.equals("delete")){
            //delete word from web db
            Boolean result = wordDAO.syncDeleteWord(op.getWordId());
            if(result){
                //delete operation from local db
                operationRepo.deleteOperation(op.get_id());
            }
        }
    }
    private void makeGlobalOperation(GlobalOperation op) {
        String operationType = op.getType();

        if(operationType.equals("insert")){
            Word word = wordDAO.syncGetWord(op.getWordId());
            if(word != null){
                wordRepo.insertWord(word);
                //delete operation from global
                Boolean result = operationDAO.syncDeleteGlobalOperation(op.get_id());
                if(!result){
                    //todo: eklenen word silinmeli
                }
            }

        }else if(operationType.equals("delete")){
            wordRepo.deleteWord(op.getWordId());
            operationRepo.deleteOperationsByWordId(op.getWordId());
            Boolean result = operationDAO.syncDeleteGlobalOperation(op.get_id());
            if(!result){
                //todo: silinen word eklenmeli
            }
        }

    }

    public interface CustomCallback{
        void successful();
    }

    private void simulation(long milisecond){
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
