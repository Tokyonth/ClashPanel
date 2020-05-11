package com.tokyonth.clashpanel.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.tokyonth.clashpanel.Contents;
import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.base.BaseFragment;
import com.tokyonth.clashpanel.utils.store.FileUtils;
import com.tokyonth.clashpanel.utils.store.SPUtils;
import com.tokyonth.clashpanel.utils.TimeUtils;
import com.tokyonth.clashpanel.utils.store.StatusbarColorUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Observable.interval;

public class OverviewFragment extends BaseFragment implements View.OnClickListener {

    private TextView tv_status;
    private Disposable subscribe;
    private TextView tv_run_time;
    private int s = 0;

    private View targetView;
    private CoordinatorLayout cdl_overview_main;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_overview;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusbarColorUtils.setStatusBarDarkIcon(mActivity, false);
        view.findViewById(R.id.brv_start).setOnClickListener(this);
        view.findViewById(R.id.brv_stop).setOnClickListener(this);
        view.findViewById(R.id.brv_restart).setOnClickListener(this);
        view.findViewById(R.id.brv_kill).setOnClickListener(this);

        targetView = view.findViewById(R.id.ll_overview_main);
        cdl_overview_main = view.findViewById(R.id.cdl_overview_main);
        tv_status = view.findViewById(R.id.tv_status);
        tv_run_time = view.findViewById(R.id.tv_run_time);
    }

    @Override
    protected void initData() {
        s += (Integer) SPUtils.getData(Contents.SP_RUNTIME, 0);
        subscribe = interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> getStatus());
    }

    private void getStatus() {
        if (FileUtils.isFileExist(Contents.CLASH_CONFIG_DIR + "RUNNING")) {
            tv_run_time.setText(TimeUtils.secToTime(s++));
            tv_status.setText("运行中...");
        } else {
            tv_status.setText("未运行...");
        }
    }

    @Override
    public void onClick(View v) {

        final int width = targetView.getMeasuredWidth();
        final int height = targetView.getMeasuredHeight();
        final float radius = (float)Math.sqrt(width*width + height*height) / 2;
        Animator animator = null;

        switch (v.getId()) {
            case R.id.brv_start:
                cdl_overview_main.setBackgroundColor(Color.parseColor("#FF5252"));
                FileUtils.createFile(Contents.CLASH_CONFIG_DIR, Contents.START_CLASH);
                animator = ViewAnimationUtils.createCircularReveal(targetView, width/2, height/2, 0, height);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        targetView.setBackgroundColor(Color.parseColor("#7C4DFF"));
                    }
                });
                animator.setDuration(1000);
                animator.start();

                break;
            case R.id.brv_stop:
                cdl_overview_main.setBackgroundColor(Color.parseColor("#FF5252"));
                FileUtils.createFile(Contents.CLASH_CONFIG_DIR, Contents.STOP_CLASH);
                animator = ViewAnimationUtils.createCircularReveal(targetView, width/2, height/2, radius, 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        targetView.setBackgroundColor(Color.parseColor("#FF5252"));
                    }
                });
                animator.setDuration(1000);
                animator.start();
                break;
            case R.id.brv_restart:
                FileUtils.createFile(Contents.CLASH_CONFIG_DIR, Contents.RESTART_CLASH);
                break;
            case R.id.brv_kill:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscribe.dispose();
        SPUtils.putData(Contents.SP_RUNTIME, s);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            StatusbarColorUtils.setStatusBarDarkIcon(mActivity, true);
        } else {
            StatusbarColorUtils.setStatusBarDarkIcon(mActivity, false);
        }
    }

}
