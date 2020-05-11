package com.tokyonth.clashpanel.utils.api;

import com.tokyonth.clashpanel.bean.clash.ConfigsBean;
import com.tokyonth.clashpanel.bean.clash.LogsBean;
import com.tokyonth.clashpanel.bean.clash.rules.RulesBean;
import com.tokyonth.clashpanel.bean.clash.TrafficBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("traffic")
    Observable<TrafficBean> getTraffic();

    @GET("logs")
    Observable<LogsBean> getLogs(@Query("type") String type);

    @GET("configs")
    Observable<ConfigsBean> getConfigs();

    @GET("rules")
    Observable<RulesBean> getRules();

}
