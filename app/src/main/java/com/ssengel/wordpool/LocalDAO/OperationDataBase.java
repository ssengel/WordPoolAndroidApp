package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ssengel.wordpool.model.Operation;


@Database(entities = {Operation.class}, version = 1, exportSchema = false)
public abstract class OperationDataBase extends RoomDatabase {
    public abstract OperationDAO operationDAO();
}
