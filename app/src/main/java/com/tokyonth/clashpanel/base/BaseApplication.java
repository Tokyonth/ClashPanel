package com.tokyonth.clashpanel.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.arialyy.aria.util.FileUtil;
import com.tokyonth.clashpanel.Contents;
import com.tokyonth.clashpanel.bean.yaml.ClashConfig;
import com.tokyonth.clashpanel.utils.store.FileUtils;
import com.tokyonth.clashpanel.utils.store.SPUtils;

import org.yaml.snakeyaml.Yaml;

public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SPUtils.getInstance(this, Contents.SP_FILE_NAME);


    }

    public static Context getContext(){
        return context;
    }

}
