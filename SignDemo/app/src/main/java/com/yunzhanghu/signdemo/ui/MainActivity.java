package com.yunzhanghu.signdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yunzhanghu.redpacketsdk.bean.RedPacketInfo;
import com.yunzhanghu.redpacketsdk.bean.TokenData;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;
import com.yunzhanghu.redpacketui.ui.activity.RPChangeActivity;
import com.yunzhanghu.redpacketui.ui.activity.RPRedPacketActivity;
import com.yunzhanghu.signdemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_enter_red_packet).setOnClickListener(this);
        findViewById(R.id.btn_enter_change).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enter_red_packet:
                Intent intent = new Intent(this, RPRedPacketActivity.class);
                intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO, new RedPacketInfo());
                intent.putExtra(RPConstant.EXTRA_TOKEN_DATA, new TokenData());
                startActivity(intent);
                break;
            case R.id.btn_enter_change:
                Intent changeIntent = new Intent(this, RPChangeActivity.class);
                changeIntent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO, new RedPacketInfo());
                changeIntent.putExtra(RPConstant.EXTRA_TOKEN_DATA, new TokenData());
                startActivity(changeIntent);
                break;
        }

    }
}
