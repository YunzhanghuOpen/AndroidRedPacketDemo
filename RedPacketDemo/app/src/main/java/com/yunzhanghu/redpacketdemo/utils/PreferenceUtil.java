package com.yunzhanghu.redpacketdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "user_info";

    private static SharedPreferences mSharedPreferences;

    private static PreferenceUtil mPreferenceManager;

    private static SharedPreferences.Editor editor;

    private static String SHARED_KEY_SENDER_NAME = "SHARED_KEY_SENDER_NAME";

    private static String SHARED_KEY_RECEIVER_NAME = "SHARED_KEY_RECEIVER_NAME";


    private PreferenceUtil(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferenceManager == null) {
            mPreferenceManager = new PreferenceUtil(cxt);
        }
    }

    public synchronized static PreferenceUtil getInstance() {
        if (mPreferenceManager == null) {
            throw new RuntimeException("please init first!");
        }

        return mPreferenceManager;
    }

    public void setSenderName(String senderName) {
        editor.putString(SHARED_KEY_SENDER_NAME, senderName).commit();
    }

    public String getSenderName() {
        return mSharedPreferences.getString(SHARED_KEY_SENDER_NAME, null);
    }


    public void setReceiverName(String receiverName) {
        editor.putString(SHARED_KEY_RECEIVER_NAME, receiverName).commit();
    }

    public String getReceiverName() {
        return mSharedPreferences.getString(SHARED_KEY_RECEIVER_NAME, null);
    }


}
