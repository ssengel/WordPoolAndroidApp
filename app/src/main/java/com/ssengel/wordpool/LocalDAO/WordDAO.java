package com.ssengel.wordpool.LocalDAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ssengel.wordpool.model.Word;

import java.util.List;

@Dao
public interface WordDAO {
    @Insert
    void insertWord(Word word);

    @Query("SELECT * FROM word")
    List<Word> getAllWords();

    @Query("SELECT * FROM word Where _id =:wordId")
    Word getWordById(String wordId);

    @Query("SELECT * FROM word Where eng =:eng")
    Word getWordByEng(String eng);

    @Query("UPDATE word SET _id =:newId WHERE _id=:id")
    void updateWordId(String id, String newId);

    @Query("DELETE FROM word WHERE _id =:id")
    void deleteWord(String id);

    @Update
    void updateWord(Word word);

}
