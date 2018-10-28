package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.ssengel.wordpool.model.Word;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class WordDataBase extends RoomDatabase {
    public abstract WordDAO wordDAO();
}
