package com.ssengel.wordpool.model;

public class Word {
    private String _id;
    private String userId;
    private String eng;
    private String tr;
    private String sentence;

    public Word() {
    }

    public Word(String _id, String userId, String eng, String tr, String sentence) {
        this._id = _id;
        this.userId = userId;
        this.eng = eng;
        this.tr = tr;
        this.sentence = sentence;
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

    @Override
    public String toString() {
        return "Word{" +
                "_id='" + _id + '\'' +
                ", userId='" + userId + '\'' +
                ", eng='" + eng + '\'' +
                ", tr='" + tr + '\'' +
                ", sentence='" + sentence + '\'' +
                '}';
    }
}
