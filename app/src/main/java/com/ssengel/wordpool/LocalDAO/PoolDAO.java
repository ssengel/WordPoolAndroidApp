package com.ssengel.wordpool.LocalDAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ssengel.wordpool.model.Pool;

import java.util.List;

@Dao
public interface PoolDAO {
    @Insert
    void insertPool(Pool pool);

    @Query("SELECT * FROM Pool")
    List<Pool> getAllPools();

}
