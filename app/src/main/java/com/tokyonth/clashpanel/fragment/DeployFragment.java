package com.tokyonth.clashpanel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.activity.SettingsActivity;
import com.tokyonth.clashpanel.base.BaseFragment;


public class DeployFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_config;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.iv_settings).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
