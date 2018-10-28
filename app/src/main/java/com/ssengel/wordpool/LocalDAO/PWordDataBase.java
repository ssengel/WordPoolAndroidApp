package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ssengel.wordpool.model.PWord;

@Database(entities = {PWord.class}, version = 1, exportSchema = false)
public abstract class PWordDataBase extends RoomDatabase {
    public abstract PWordDAO pWordDAO();
}
