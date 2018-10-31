package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.ssengel.wordpool.helper.Config;
import com.ssengel.wordpool.model.Operation;
import com.ssengel.wordpool.model.Word;

import java.util.Date;
import java.util.List;

public class WordRepo {
    private String DB_NAME = "db_word";
    private WordDataBase wordDataBase;

    public WordRepo(Context context){
        wordDataBase = Room.databaseBuilder(context,WordDataBase.class,DB_NAME).build();
    }

    public void insertWord(Word word){
        // web den eklenmemis kelimeler icin
        if(word.get_id() == null){
            word.set_id(new Date().getTime() + "");
            word.setUserId(Config.USER_ID);
            word.setCreatedAt(new Date());
        }
        wordDataBase.wordDAO().insertWord(word);
    }

    public Word getWordById(String id){
        return wordDataBase.wordDAO().getWordById(id);
    }

    public List<Word> getAllWords(){
        return wordDataBase.wordDAO().getAllWords();
    }

    public void updateWordId(String id, String newId){
        wordDataBase.wordDAO().updateWordId(id, newId);
    }

    public void deleteWord(String id){
        wordDataBase.wordDAO().deleteWord(id);
    }

    public void updateWord(Word word){
        wordDataBase.wordDAO().updateWord(word);
    }
}
