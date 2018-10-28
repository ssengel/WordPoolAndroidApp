package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.ssengel.wordpool.model.PWord;

import java.util.List;

public class PWordRepo {

    private String DB_NAME = "db_pword";
    private PWordDataBase pWordDataBase;

    public PWordRepo(Context context){
        pWordDataBase = Room.databaseBuilder(context, PWordDataBase.class, DB_NAME).build();
    }

    public void insertPWord(PWord pWord){
        pWordDataBase.pWordDAO().insertPWord(pWord);
    }

    public void deletePWord(String id){
        pWordDataBase.pWordDAO().deletePWord(id);
    }

    public PWord getPWordById(String id){
        return pWordDataBase.pWordDAO().getPWord(id);
    }

    public List<PWord> getPWordsByPoolId(String poolId){
        return pWordDataBase.pWordDAO().getPWordsByPoolId(poolId);
    }


}
