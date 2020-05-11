package com.tokyonth.clashpanel.database;

import android.provider.BaseColumns;

public final class MetaData {

    private MetaData(){}
    //表的定义
    public static abstract class SubscriptionTable implements BaseColumns {
        public static final String TABLE_NAME = "subscription";
        public static final String NAME = "name";
        public static final String URL = "url";
        public static final String COLOR = "color";
    }
}

