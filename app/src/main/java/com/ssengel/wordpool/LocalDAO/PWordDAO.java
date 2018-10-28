package com.ssengel.wordpool.LocalDAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ssengel.wordpool.model.PWord;

import java.util.List;

@Dao
public interface PWordDAO {
    @Insert
    void insertPWord(PWord pWord);

    @Query("DELETE FROM pword WHERE _id =:id")
    void deletePWord(String id);

    @Query("SELECT * FROM PWord WHERE _id =:pWordId")
    PWord getPWord(String pWordId);

    @Query("SELECT * FROM PWord WHERE poolId =:poolId")
    List<PWord> getPWordsByPoolId(String poolId);

}
