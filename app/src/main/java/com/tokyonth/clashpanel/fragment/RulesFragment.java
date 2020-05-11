package com.tokyonth.clashpanel.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.adapter.RulesAdapter;
import com.tokyonth.clashpanel.base.BaseFragment;
import com.tokyonth.clashpanel.bean.clash.rules.RulesBean;
import com.tokyonth.clashpanel.utils.api.ApiDetail;
import com.tokyonth.clashpanel.utils.api.RetrofitFactory;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RulesFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView rvRules;
    private RulesAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rules;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rvRules = view.findViewById(R.id.rv_rules);
        rvRules.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void initData() {
        adapter = new RulesAdapter();
        rvRules.setAdapter(adapter);
        new RetrofitFactory(ApiDetail.CLASH_URL).getApiInterface()
                .getRules()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RulesBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RulesBean rulesBean) {
                        adapter.setData(rulesBean);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onClick(View v) {

    }

}
