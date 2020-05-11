package com.tokyonth.clashpanel.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.tokyonth.clashpanel.Contents;
import com.tokyonth.clashpanel.R;
import com.tokyonth.clashpanel.adapter.SubscriptionAdapter;
import com.tokyonth.clashpanel.bean.SubscriptionBean;
import com.tokyonth.clashpanel.database.DataBaseOperate;
import com.tokyonth.clashpanel.utils.store.FileUtils;
import com.tokyonth.clashpanel.utils.store.SPUtils;

import java.util.List;
import java.util.Objects;

public class SubscriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv_subscription;
    private SubscriptionAdapter adapter;
    private ImageView iv_del_subscription;
    private ExtendedFloatingActionButton floatingActionButton;
    private List<SubscriptionBean> beanList;
    private DataBaseOperate dbUtils;
    private boolean DEL_TAG = false;
    private int subscription_color = 0;

    private String DownLoadUrl;
    private long taskId;
    private AlertDialog dialogs;

    private View dialog_view;
    private TextView dialog_tv_name;
    private ProgressBar dialog_progressBar;
    private TextView dialog_tv_speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_subscription);
        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(null);

        rv_subscription = findViewById(R.id.rv_subscript);
//        iv_del_subscription = findViewById(R.id.iv_del_subscription);
      //  iv_del_subscription.setOnClickListener(this);
        floatingActionButton = findViewById(R.id.fab_add_subscript);
        floatingActionButton.setOnClickListener(this);
    }

    private void initData() {
        Aria.download(this).register();
        dbUtils = new DataBaseOperate(SubscriptionActivity.this);
        beanList = dbUtils.findAll();
        if (beanList.size() == 0) {
//            iv_del_subscription.setVisibility(View.GONE);
        }
        adapter = new SubscriptionAdapter(beanList);
        rv_subscription.setLayoutManager(new LinearLayoutManager(this));
        rv_subscription.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            View dialog_view = View.inflate(SubscriptionActivity.this, R.layout.dialog_add_subscript, null);
            final TextInputEditText et_name = dialog_view.findViewById(R.id.edit_subscription_name);
            final TextInputEditText et_url = dialog_view.findViewById(R.id.edit_subscription_url);
            et_name.setText(beanList.get(position).getName());
            et_url.setText(beanList.get(position).getUrl());

            new MaterialAlertDialogBuilder(SubscriptionActivity.this)
            .setTitle("提示")
            .setCancelable(false)
            .setView(dialog_view)
            .setPositiveButton("修改", (dialog, which) -> {
                String name = Objects.requireNonNull(et_name.getText()).toString().trim();
                String url = Objects.requireNonNull(et_url.getText()).toString().trim();
                if (!name.equals("") && !url.equals("")) {
                    SubscriptionBean bean = new SubscriptionBean();
                    bean.setId(beanList.get(position).getId());
                    bean.setUrl(url);
                    bean.setName(name);
                    bean.setColor("#F44336");
                    beanList.get(position).setName(name);
                    beanList.get(position).setUrl(url);
                    dbUtils.update(bean);
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(rv_subscription, "不能为空！", Snackbar.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("取消", null)
            .create().show();
        });
        adapter.setOnItemDelListener((view, position) -> {
            int id = beanList.get(position).getId();
            dbUtils.delete(id);
            adapter.removeData(position);
            if (beanList.size() == 0) {
                adapter.setDelMode(false);
                DEL_TAG = false;
                iv_del_subscription.setVisibility(View.GONE);
            }
        });
        adapter.setOnItemSubscriptSelected(new SubscriptionAdapter.OnItemSubscriptionSelected() {
            @Override
            public void onItemSubscriptClick(View view, int position) {
                SPUtils.putData("SelectedId", beanList.get(position).getId());
                String name = beanList.get(position).getName();
                String url = beanList.get(position).getUrl();

                DownLoadUrl = url;
                taskId =  Aria.download(this)
                        .load(url) // 下载地址
                        .setFilePath(Contents.CLASH_CONFIG_DIR + "config.yaml", true) // 设置文件保存路径
                        .create();
                dialog_view = View.inflate(SubscriptionActivity.this, R.layout.dialog_dwonload_subscription, null);
                dialog_tv_name = dialog_view.findViewById(R.id.config_name);
                dialog_progressBar = dialog_view.findViewById(R.id.pb_down);
                dialog_tv_speed = dialog_view.findViewById(R.id.tv_speed);
                dialog_tv_name.setText(name);

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SubscriptionActivity.this);
                builder.setTitle("提示");
                builder.setCancelable(true);
                builder.setView(dialog_view);
                builder.setNegativeButton("取消下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Aria.download(this).load(taskId).cancel(true);
                    }
                });
                dialogs = builder.show();
            }
        });
    }

    /*
     * 任务执行中
     */
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        if (task.getKey().equals(DownLoadUrl)) { // 判断任务是否是指定任务
         //   ALog.d(TAG, "isRunning");
          //  progress.setProgress(task.getPercent()); // 获取百分比进度
           // speed.setSpeed(task.getConvertSpeed()); // 获取速度
            dialog_tv_speed.setText(task.getPercent() + "%\t" + task.getSpeed() + "bps");
            dialog_progressBar.setProgress(task.getPercent());
        }
    }

    /*
     * 任务完成
     */
    @Download.onTaskComplete
    protected void taskComplete(DownloadTask task) {
        if (task.getKey().equals(DownLoadUrl)) {
            dialog_progressBar.setProgress(100);
            dialogs.dismiss();
            FileUtils.createFile(Contents.CLASH_CONFIG_DIR, Contents.RESTART_CLASH);
            Snackbar.make(rv_subscription, "下载完成！", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void addSubscription() {
        View view = View.inflate(SubscriptionActivity.this, R.layout.dialog_add_subscript, null);
        final TextInputEditText et_name = view.findViewById(R.id.edit_subscription_name);
        final TextInputEditText et_url = view.findViewById(R.id.edit_subscription_url);

        RadioGroup rg_color = view.findViewById(R.id.rg_subscription_color);
        rg_color.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.sub_color0:
                    subscription_color = 0;
                    break;
                case R.id.sub_color1:
                    subscription_color = 1;
                    break;
                case R.id.sub_color2:
                    subscription_color = 2;
                    break;
                case R.id.sub_color3:
                    subscription_color = 3;
                    break;
                case R.id.sub_color4:
                    subscription_color = 4;
                    break;
                case R.id.sub_color5:
                    subscription_color = 5;
                    break;
                case R.id.sub_color6:
                    subscription_color = 6;
                    break;
                case R.id.sub_color7:
                    subscription_color = 7;
                    break;
            }
        });

        new MaterialAlertDialogBuilder(SubscriptionActivity.this)
                .setTitle("提示")
                .setCancelable(true)
                .setView(view)
                .setPositiveButton("确定", (dialog, which) -> {
                    String name = Objects.requireNonNull(et_name.getText()).toString().trim();
                    String url = Objects.requireNonNull(et_url.getText()).toString().trim();
                    if (!name.equals("") && !url.equals("")) {
                        SubscriptionBean bean = new SubscriptionBean();
                        bean.setUrl(url);
                        bean.setName(name);
                        bean.setColor(Contents.COLOR_LIST[subscription_color]);
                        beanList.add(bean);
                        dbUtils.add(bean);
                        adapter.notifyDataSetChanged();
                        iv_del_subscription.setVisibility(View.VISIBLE);
                    } else {
                        Snackbar.make(rv_subscription, "不能为空！", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
         /*   case R.id.iv_del_subscription:
                DEL_TAG = true;
                adapter.setDelMode(true);
                floatingActionButton.setText("取消");
               // floatingActionButton.setIconResource(R.drawable.ic_close);
                floatingActionButton.setBackgroundColor(getResources().getColor(R.color.colorTabItem0));
                iv_del_subscription.setVisibility(View.INVISIBLE);
                break;
            case R.id.fab_add_subscript:
                if (DEL_TAG) {
                    DEL_TAG = false;
                    adapter.setDelMode(false);
                    floatingActionButton.setText("添加");
                 //   floatingActionButton.setIconResource(R.drawable.ic_add);
                    floatingActionButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    iv_del_subscription.setVisibility(View.VISIBLE);
                } else {
                    addSubscription();
                }
                break;

          */
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
