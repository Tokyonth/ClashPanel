package com.tokyonth.clashpanel.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.base.BaseActivity;
import com.tokyonth.clashpanel.fragment.RulesFragment;
import com.tokyonth.clashpanel.fragment.MessageFragment;
import com.tokyonth.clashpanel.fragment.OverviewFragment;
import com.tokyonth.clashpanel.fragment.DeployFragment;

import java.util.ArrayList;
import java.util.Map;

import liang.lollipop.ltabview.LTabHelper;
import liang.lollipop.ltabview.LTabView;
import liang.lollipop.ltabview.builder.ExpandBuilder;

public class MainAppActivity extends BaseActivity {

    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private int currentIndex = 0;
    private long mExitTime;

    private Fragment currentFragment;
    private ArrayList<Fragment> fragments;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main_app);
        checkPermission();
        initView();
        initData(savedInstanceState);

        Map<String,String> map = YmlUtils.getYmlByFileName("/sdcard/config.yaml", "name");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT, currentIndex);
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        LTabView tabView = findViewById(R.id.tabView);
        ExpandBuilder helper = LTabHelper.INSTANCE.withExpandItem(tabView)
                .addItem(expandItem -> {
                    int color = ContextCompat.getColor(expandItem.getRootView().getContext(), R.color.colorAccent);
                    expandItem.setExpandColor(color & 0x60FFFFFF);
                    expandItem.setIcon(R.drawable.ic_tab_overview);
                    expandItem.setSelectedIconColor(color);
                    expandItem.setUnselectedIconColor(getResources().getColor(R.color.colorSvg));
                    expandItem.setText("面板");
                    expandItem.setTextColor(color);
                    return null;
                })
                .addItem(expandItem -> {
                    int color = ContextCompat.getColor(expandItem.getRootView().getContext(), R.color.colorTabItem0);
                    expandItem.setExpandColor(color & 0x60FFFFFF);
                    expandItem.setIcon(R.drawable.ic_tab_settings);
                    expandItem.setSelectedIconColor(color);
                    expandItem.setUnselectedIconColor(getResources().getColor(R.color.colorSvg));
                    expandItem.setText("代理");
                    expandItem.setTextColor(color);
                    return null;
                }).addItem(expandItem -> {
                    int color = ContextCompat.getColor(expandItem.getRootView().getContext(), R.color.colorTabItem0);
                    expandItem.setExpandColor(color & 0x60FFFFFF);
                    expandItem.setIcon(R.drawable.ic_tab_settings);
                    expandItem.setSelectedIconColor(color);
                    expandItem.setUnselectedIconColor(getResources().getColor(R.color.colorSvg));
                    expandItem.setText("配置");
                    expandItem.setTextColor(color);
                    return null;
                }).addItem(expandItem -> {
                    int color = ContextCompat.getColor(expandItem.getRootView().getContext(), R.color.colorTabITem1);
                    expandItem.setExpandColor(color & 0x60FFFFFF);
                    expandItem.setIcon(R.drawable.ic_tab_rules);
                    expandItem.setSelectedIconColor(color);
                    expandItem.setUnselectedIconColor(getResources().getColor(R.color.colorSvg));
                    expandItem.setText("规则");
                    expandItem.setTextColor(color);
                    return null;
                }).addItem(expandItem -> {
                    int color = Color.parseColor("#4CAF50");
                    expandItem.setExpandColor(color & 0x60FFFFFF);
                    expandItem.setIcon(R.drawable.ic_tab_message);
                    expandItem.setSelectedIconColor(color);
                    expandItem.setUnselectedIconColor(getResources().getColor(R.color.colorSvg));
                    expandItem.setText("信息");
                    expandItem.setTextColor(color);
                    return null;
                });
        helper.setLayoutStyle(LTabView.Style.Center);
        helper.addOnSelectedListener(index -> {
            currentIndex = index;
            showFragment();
        });
    }

    private void initData(Bundle savedInstanceState) {
        currentFragment = new Fragment();
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
            //注意，添加顺序要跟下面添加的顺序一样！！！！
            fragments.clear();
            fragments.add(fragmentManager.findFragmentByTag(String.valueOf(0)));
            fragments.add(fragmentManager.findFragmentByTag(String.valueOf(1)));
            fragments.add(fragmentManager.findFragmentByTag(String.valueOf(2)));
            fragments.add(fragmentManager.findFragmentByTag(String.valueOf(3)));
            restoreFragment();
        } else {
            //正常启动时调用
            fragments.add(new OverviewFragment());
            fragments.add(new DeployFragment());
            fragments.add(new RulesFragment());
            fragments.add(new MessageFragment());
            showFragment();
        }
    }

    private void showFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!fragments.get(currentIndex).isAdded()) {
            transaction.hide(currentFragment)
                    .add(R.id.main_frame_layout,fragments.get(currentIndex), String.valueOf(currentIndex));
        } else {
            transaction.hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }
        currentFragment = fragments.get(currentIndex);
        transaction.commit();
    }

    private void restoreFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if(i == currentIndex) {
                transaction.show(fragments.get(i));
            } else {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
        currentFragment = fragments.get(currentIndex);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
