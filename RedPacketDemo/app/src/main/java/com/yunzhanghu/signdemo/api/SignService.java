package com.yunzhanghu.signdemo.api;

import com.yunzhanghu.signdemo.model.SignModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Max on 2016/11/14.
 */

public interface SignService {

    @GET("api/demo-sign/")
    Call<SignModel> getSignInfo(@Query("uid") String userId, @Query("token") String token);
}
