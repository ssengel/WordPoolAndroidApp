package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ssengel.wordpool.model.Operation;

import java.util.List;

@Dao
public interface OperationDAO {

    @Insert
    void insertOperation(Operation op);

    @Query("DELETE FROM operation WHERE _id =:id")
    void deleteOperation(int id);

    @Query("SELECT * FROM operation")
    List<Operation> getAllOperations();

    @Query("DELETE FROM operation WHERE wordId =:wordId")
    void deleteOperationsByWordId(String wordId);

}
