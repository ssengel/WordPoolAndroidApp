package com.ssengel.wordpool.helper;

import com.ssengel.wordpool.model.Pool;
import com.ssengel.wordpool.model.Word;

import java.util.ArrayList;

public class Config {
    //VARS
    public static String TOKEN = null;
    public static String USER_ID = null;
    public static String EMAIL = null;
    public static String PASSWORD = null;

    //PREFS LOGIN
    public static String FIRST_ENTRY_KEY = "firstEntry";
    public static String TOKEN_KEY = "token";
    public static String USER_ID_KEY = "userId";
    public static String EMAIL_KEY = "email";
    public static String PASSWORD_KEY = "password";

    //PREFS MAIN
    public static String LIBRARY_KEY = "library";

    //URLS
    public static final String URL_ROOT = "http://192.168.1.38:8080/";
    public static final String URL_LOGIN = URL_ROOT + "login";
    public static final String URL_POOL = URL_ROOT + "pool";
    public static final String URL_PWORD = URL_ROOT + "pword";
    public static String URL_WORD(){
        return URL_ROOT +"user/"+ USER_ID +"/word/";
    }
    public static String URL_OPERATION(){
        return URL_ROOT +"user/"+ USER_ID +"/operation/";
    }
    public static String URL_DELETE_OPERATIONS_BY_USER_ID(){ return URL_ROOT +"user/" + USER_ID +"/deleteOps"; }
}
