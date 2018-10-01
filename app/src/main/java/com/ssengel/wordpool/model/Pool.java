package com.ssengel.wordpool.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Pool implements Serializable {
    private String _id;
    private String name;
    private ArrayList<Word> words;

    public Pool() {
    }

    public Pool(String _id, String name, ArrayList<Word> words) {
        this._id = _id;
        this.name = name;
        this.words = words;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }
}
