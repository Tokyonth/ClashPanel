package com.tokyonth.clashpanel.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.adapter.LogsAdapter;
import com.tokyonth.clashpanel.base.BaseFragment;
import com.tokyonth.clashpanel.bean.clash.LogsBean;
import com.tokyonth.clashpanel.bean.clash.TrafficBean;
import com.tokyonth.clashpanel.chart.DynamicLineChartManager;
import com.tokyonth.clashpanel.utils.api.ApiDetail;
import com.tokyonth.clashpanel.utils.api.RetrofitFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Observable.interval;

public class MessageFragment extends BaseFragment {

    private LineChart lineChart;
    private DynamicLineChartManager dynamicLineChartManager;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    private Disposable subscribe;
    private RecyclerView rv_logs;

    private ArrayList<LogsBean> logsBeanArrayList;
    private LogsAdapter logsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rv_logs = view.findViewById(R.id.rv_logs);
        lineChart = view.findViewById(R.id.lineChart);
        rv_logs.setLayoutManager(new LinearLayoutManager(getContext()));
        logsBeanArrayList = new ArrayList<>();
        logsAdapter = new LogsAdapter(logsBeanArrayList);
        rv_logs.setAdapter(logsAdapter);
    }

    @Override
    protected void initData() {
        new Thread(() -> {
            while (true){
                try {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException ignored){ }
            }
        }).start();

        //折线名字
        names.add("上行");
        names.add("下行");
        //折线颜色
        colour.add(Color.parseColor("#6236FF"));
        colour.add(Color.parseColor("#F9345E"));

        dynamicLineChartManager = new DynamicLineChartManager(lineChart, names, colour);
        dynamicLineChartManager.setDescription("");
        dynamicLineChartManager.setMarkerView(getContext());
        new Handler().postDelayed(() -> {
            //设置曲线填充色 以及 MarkerView
            dynamicLineChartManager.setChartFillDrawable(Objects.requireNonNull(getContext()));
        }, 1000);

        new RetrofitFactory(ApiDetail.CLASH_URL).getApiInterface()
                .getTraffic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TrafficBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TrafficBean trafficBeanTimed) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        subscribe = interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(aLong -> new RetrofitFactory(ApiDetail.CLASH_URL).getApiInterface()
                        .getLogs(ApiDetail.CLASH_LOGS_TYPE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LogsBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LogsBean responseBody) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
                                Date date = new Date(System.currentTimeMillis());

                                logsBeanArrayList.add(new LogsBean(simpleDateFormat.format(date), responseBody.getType(), responseBody.getPayload()));
                                logsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }));

    }

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                list.add((95 + (int)(Math.random() * ((115 - 95) + 1))));
                list.add((85 + (int)(Math.random() * ((100 - 85) + 1))));
                dynamicLineChartManager.addEntry(list);
                list.clear();
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscribe.dispose();
    }

}
