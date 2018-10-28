package com.ssengel.wordpool.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Operation {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String type;
    @NonNull
    private String wordId;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }


    @Override
    public String toString() {
        return "Operation{" +
                "_id=" + _id +
                ", type='" + type + '\'' +
                ", wordId='" + wordId + '\'' +
                '}';
    }
}
