package com.ssengel.wordpool.asyncResponce;

import com.android.volley.VolleyError;
import com.ssengel.wordpool.model.Word;

import java.util.List;

public interface WordObjectCallBack {
    void processFinish(Word word);
    void responseError(Error error);
}
