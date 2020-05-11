package com.tokyonth.clashpanel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.adapter.SettingsAdapter;
import com.tokyonth.clashpanel.bean.SettingsItemBean;
import com.tokyonth.clashpanel.utils.store.StatusbarColorUtils;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusbarColorUtils.setStatusBarDarkIcon(this, true);
        setContentView(R.layout.activity_settings);
        initView();
    }

    private void initView() {
        RecyclerView rvSettings = findViewById(R.id.rv_settings);
        rvSettings.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<SettingsItemBean> beans = new ArrayList<>();
        beans.add(new SettingsItemBean("订阅管理", "管理你所拥有的订阅", "#673AB7", R.drawable.ic_clash_reboot));
        beans.add(new SettingsItemBean("更新GeoIP", "从网络上更新", "#009688", R.drawable.ic_clash_reboot));
        beans.add(new SettingsItemBean("检查更新", "更新日期:2020-3-25", "#F44336", R.drawable.ic_clash_reboot));
        beans.add(new SettingsItemBean("鸣谢", null, "#4CAF50", R.drawable.ic_clash_reboot));


        SettingsAdapter settingsAdapter = new SettingsAdapter(this, beans);
        rvSettings.setAdapter(settingsAdapter);
        settingsAdapter.setOnItemClick((view, pos) -> {
            switch (pos) {
                case 0:
                    startActivity(new Intent(this, SubscriptionActivity.class));
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        });
    }

}
