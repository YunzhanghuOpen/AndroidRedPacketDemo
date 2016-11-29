package com.yunzhanghu.redpacketdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.yunzhanghu.redpacketdemo.R;
import com.yunzhanghu.redpacketdemo.utils.PreferenceUtil;
import com.yunzhanghu.redpacketdemo.utils.RedPacketUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends FragmentActivity {

    private EditText mSenderView;
    private EditText mReceiverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSenderView = (EditText) findViewById(R.id.sender);
        mReceiverView = (EditText) findViewById(R.id.receiver);
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    private void attemptLogin() {
        mSenderView.setError(null);
        mReceiverView.setError(null);
        String senderName = mSenderView.getText().toString();
        String receiverName = mReceiverView.getText().toString();
        if (checkParams(senderName, receiverName)) return;
        //缓存输入的用户信息
        PreferenceUtil.getInstance().setSenderName(senderName);
        PreferenceUtil.getInstance().setReceiverName(receiverName);
        RedPacketUtil.initUserInfo();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    private boolean checkParams(String senderName, String receiverName) {
        if (TextUtils.isEmpty(senderName)) {
            mSenderView.requestFocus();
            mSenderView.setError(getString(R.string.error_invalid_sender_name));
            return true;
        }
        if (TextUtils.isEmpty(receiverName)) {
            mReceiverView.requestFocus();
            mReceiverView.setError(getString(R.string.error_invalid_receiver_name));
            return true;
        }
        if (!isMobileNO(senderName)) {
            mSenderView.requestFocus();
            mSenderView.setError(getString(R.string.error_invalid_mobile_no));
            return true;
        }
        if (!isMobileNO(receiverName)) {
            mReceiverView.requestFocus();
            mReceiverView.setError(getString(R.string.error_invalid_mobile_no));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 手机号正则验证
     *
     * @param mobiles 手机号码
     * @return true or false
     */
    private boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0678]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}

