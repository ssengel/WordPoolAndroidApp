package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.ssengel.wordpool.model.Pool;

@Database(entities = {Pool.class}, version = 1, exportSchema = false)
public abstract class PoolDataBase extends RoomDatabase {
    public abstract PoolDAO poolDAO();
}
