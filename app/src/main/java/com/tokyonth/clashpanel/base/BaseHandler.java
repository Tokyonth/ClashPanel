package com.tokyonth.clashpanel.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.jetbrains.annotations.NotNull;

public class BaseHandler extends Handler {

    private CallBack callBack;

    public interface CallBack {
        void handleMessage(Message msg);
    }

    public BaseHandler(CallBack callBack,Looper looper) {
        super(looper);
        this.callBack = callBack;
    }

    @Override
    public void handleMessage(@NotNull Message msg) {
        if(null != callBack){
            callBack.handleMessage(msg);
        }
    }

}
