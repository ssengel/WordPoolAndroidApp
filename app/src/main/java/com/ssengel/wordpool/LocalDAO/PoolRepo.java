package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.ssengel.wordpool.model.Pool;

import java.util.List;

public class PoolRepo {

    private String DB_NAME = "db_pool";
    private PoolDataBase poolDataBase;

    public PoolRepo(Context context){
        poolDataBase = Room.databaseBuilder(context, PoolDataBase.class, DB_NAME).build();
    }

    public void insertPool(Pool pool){
        poolDataBase.poolDAO().insertPool(pool);
    }

    public List<Pool> getAllPools(){
        return poolDataBase.poolDAO().getAllPools();
    }


}
