package com.ssengel.wordpool.asyncResponce;

import java.util.List;

public interface WordListCallBack {
    void processFinish(List list);
    void responseError(Error error);
}
