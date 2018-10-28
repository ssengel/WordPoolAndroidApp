package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.ssengel.wordpool.model.Operation;

import java.util.List;

public class OperationRepo {
    private String DB_NAME = "db_operation";
    private OperationDataBase operationDataBase;

    public OperationRepo(Context context){
        operationDataBase = Room.databaseBuilder(context, OperationDataBase.class, DB_NAME).build();
    }

    public void insertOperation(Operation op){
        operationDataBase.operationDAO().insertOperation(op);
    }

    public void deleteOperation(int id){
        operationDataBase.operationDAO().deleteOperation(id);
    }

    public List<Operation> getAllOperations(){
        return operationDataBase.operationDAO().getAllOperations();
    }

    public void deleteOperationsByWordId(String wordId){
        operationDataBase.operationDAO().deleteOperationsByWordId(wordId);
    }
}
