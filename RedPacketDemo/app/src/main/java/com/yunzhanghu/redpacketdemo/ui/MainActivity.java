package com.yunzhanghu.redpacketdemo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yunzhanghu.redpacketdemo.R;
import com.yunzhanghu.redpacketdemo.utils.DemoUtil;
import com.yunzhanghu.redpacketdemo.utils.PreferenceUtil;

import static com.yunzhanghu.redpacketdemo.DemoApplication.sCurrentNickname;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private TextView mTvCurrentUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TextUtils.isEmpty(PreferenceUtil.getInstance().getSenderName()) || TextUtils.isEmpty(PreferenceUtil.getInstance().getReceiverName())) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            DemoUtil.initUserInfo();
        }
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_single).setOnClickListener(this);
        findViewById(R.id.btn_group).setOnClickListener(this);
        findViewById(R.id.btn_system).setOnClickListener(this);
        findViewById(R.id.btn_ad).setOnClickListener(this);
        findViewById(R.id.btn_change).setOnClickListener(this);
        findViewById(R.id.btn_contact_us).setOnClickListener(this);
        findViewById(R.id.btn_clear_user_cache).setOnClickListener(this);
        mTvCurrentUsername = (TextView) findViewById(R.id.tv_current_user_nickname);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvCurrentUsername.setText(String.format("当前用户昵称 ：%s", sCurrentNickname));
    }

    @Override
    public void onClick(View view) {
        int fromFlag;
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_single:
                fromFlag = 0;
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra("from", fromFlag);
                break;
            case R.id.btn_group:
                fromFlag = 1;
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra("from", fromFlag);
                break;
            case R.id.btn_system:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.contact_us)
                        .setMessage(getString(R.string.msg_contact_us))
                        .setPositiveButton(R.string.btn_str_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            case R.id.btn_ad:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.contact_us)
                        .setMessage(getString(R.string.msg_contact_us))
                        .setPositiveButton(R.string.btn_str_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            case R.id.btn_change:
                DemoUtil.startChangeActivity(this);
                break;
            case R.id.btn_contact_us:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.contact_us)
                        .setMessage(getString(R.string.msg_contact_us))
                        .setPositiveButton(R.string.btn_str_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            case R.id.btn_clear_user_cache:
                clearCache();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void clearCache() {
        PreferenceUtil.getInstance().setSenderName(null);
        PreferenceUtil.getInstance().setReceiverName(null);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


}
