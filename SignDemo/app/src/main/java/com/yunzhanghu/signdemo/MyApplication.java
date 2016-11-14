package com.yunzhanghu.signdemo;

import android.app.Application;
import android.util.Log;

import com.yunzhanghu.redpacketsdk.RPRefreshSignListener;
import com.yunzhanghu.redpacketsdk.RPValueCallback;
import com.yunzhanghu.redpacketsdk.RedPacket;
import com.yunzhanghu.redpacketsdk.bean.TokenData;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;
import com.yunzhanghu.signdemo.api.SignService;
import com.yunzhanghu.signdemo.model.SignModel;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Max on 2016/11/13.
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";


    @Override
    public void onCreate() {
        super.onCreate();
        initRedPacket();
    }

    private void initRedPacket() {
        //初始化红包sdk
        RedPacket.getInstance().initContext(this, RPConstant.AUTH_METHOD_SIGN);
        //开启红包相关日志输出
        RedPacket.getInstance().setDebugMode(true);
        //设置刷新签名的回调函数
        RedPacket.getInstance().setRefreshSignListener(new RPRefreshSignListener() {
            @Override
            public void onRefreshSign(final RPValueCallback<TokenData> callback) {
                //异步向App Server获取签名参数
                //这里使用随机生成的UUID代替App中的userId,生产环境需要传入App的userId
                String seeds = "Max";
                String userId = UUID.nameUUIDFromBytes(seeds.getBytes()).toString();
                String token = "tempValue";
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(JacksonConverterFactory.create())
                        //Demo用URL,生产环境需要替换
                        .baseUrl("https://rpv2.yunzhanghu.com/")
                        .build();
                SignService signService = retrofit.create(SignService.class);
                Call<SignModel> call = signService.getSignInfo(userId, token);
                call.enqueue(new Callback<SignModel>() {
                    @Override
                    public void onResponse(Call<SignModel> call, Response<SignModel> response) {
                        if (response.isSuccessful()) {
                            SignModel signModel = response.body();
                            Log.d(TAG, signModel.toString());
                            //赋值返回参数给TokenData
                            TokenData tokenData = new TokenData();
                            tokenData.authPartner = signModel.partner;
                            tokenData.authSign = signModel.sign;
                            tokenData.appUserId = signModel.user_id;
                            tokenData.timestamp = signModel.timestamp;
                            //回传签名参数给红包SDK
                            callback.onSuccess(tokenData);
                        } else {
                            String statusCode = response.code() + "";
                            callback.onError(statusCode, response.errorBody().toString());
                            Log.d(TAG, "StatusCode : " + statusCode + " Message : " + response.errorBody().toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<SignModel> call, Throwable t) {
                        Log.d(TAG, "onFailure :" + t.getMessage());
                        callback.onError("onFailure", t.getMessage());
                    }
                });

            }
        });
    }
}
