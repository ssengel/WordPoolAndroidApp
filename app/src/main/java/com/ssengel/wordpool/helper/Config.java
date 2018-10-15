package com.ssengel.wordpool.helper;

import com.ssengel.wordpool.model.Pool;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;

public class Config {
    public static String TOKEN = null;
    public static String USER_ID = null;
    public static final String URL_ROOT = "http://192.168.0.36:8080/";
    public static final String URL_LOGIN = URL_ROOT + "login";

    public static String URL_WORD(){
        return URL_ROOT +"user/"+ USER_ID +"/word/";
    }


    //simulation local Database
    private static boolean fetched = false;
    public static ArrayList<Pool> poolList = new ArrayList<>();

    public static ArrayList<Pool> GetLocalWords(){
        if(fetched){
            return poolList;
        }else{

            fetched = true;

            ArrayList<Word> list1 = new ArrayList<>();
            Word w1 = new Word();
            w1.setEng("health");
            w1.setTr("saglik");
            w1.setSentence("asdfasdf asdfasdf asdf asd");
            w1.setCategory("Health");
            list1.add(w1);

            ArrayList<Word> list2 = new ArrayList<>();
            Word w2 = new Word();
            w2.setEng("Electronic");
            w2.setTr("saglik");
            w2.setSentence("asdfasdf asdfasdf asdf asd");
            w2.setCategory("Electronic");
            list2.add(w2);

            ArrayList<Word> list3 = new ArrayList<>();
            Word w3 = new Word();
            w3.setEng("Art");
            w3.setTr("saglik");
            w3.setSentence("asdfasdf asdfasdf asdf asd");
            w3.setCategory("Art");

            Word w4 = new Word();
            w4.setEng("Art");
            w4.setTr("saglik");
            w4.setSentence("asdfasdf asdfasdf asdf asd");
            w4.setCategory("Art");

            list3.add(w3);
            list3.add(w4);



            poolList.add(new Pool("asdf","Health", list1));
            poolList.add(new Pool("asdf","Science",new ArrayList<Word>()));
            poolList.add(new Pool("asdf","Electronic",list2));
            poolList.add(new Pool("asdf", "Art",list3));
            poolList.add(new Pool("asdf", "Kitchen",new ArrayList<Word>()));
            poolList.add(new Pool("adsf","Space",new ArrayList<Word>()));
            poolList.add(new Pool("asdfadf","Computer",new ArrayList<Word>()));

            return poolList;
        }

    }
}
