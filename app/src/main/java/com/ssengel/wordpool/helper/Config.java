package com.ssengel.wordpool.helper;

public class Config {
    public static String TOKEN = null;
    public static String USER_ID = null;
    public static final String URL_ROOT = "http://192.168.0.36:8080/";
    public static final String URL_LOGIN = URL_ROOT + "login";
    public static String getUrlWord(){
        return URL_ROOT +"user/"+ USER_ID +"/word/";
    }
}
