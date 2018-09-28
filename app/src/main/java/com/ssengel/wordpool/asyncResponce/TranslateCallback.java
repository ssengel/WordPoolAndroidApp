package com.ssengel.wordpool.asyncResponce;

public interface TranslateCallback {
    void onSuccess(String translatedText);
    void onFailure();
}
