package com.ssengel.wordpool.model;

import java.io.Serializable;
import java.util.Date;

public class Word implements Serializable {
    private String _id;
    private String userId;
    private String eng;
    private String tr;
    private String sentence;
    private String category= "";
    private Date createdAt;

    public Word() {
    }

    public Word(String _id, String userId, String eng, String tr, String sentence,String category, Date createdAt) {
        this._id = _id;
        this.userId = userId;
        this.eng = eng;
        this.tr = tr;
        this.sentence = sentence;
        this.category = category;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
