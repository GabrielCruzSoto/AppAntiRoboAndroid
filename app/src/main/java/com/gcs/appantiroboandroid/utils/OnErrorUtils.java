package com.gcs.appantiroboandroid.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class OnErrorUtils implements Handler.Callback {

    @Override
    public boolean handleMessage(Message msg) {
        Log.e("onError","ERROR "+  msg);
        return false;
    }


}
