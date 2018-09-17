package com.ssengel.wordpool.asyncResponce;

import com.android.volley.VolleyError;

public interface AuthServiceCallBack {
    void processFinished();
    void responseError(VolleyError error);
}
