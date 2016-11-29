package com.yunzhanghu.redpacketdemo.model;

/**
 * Created by Max on 2016/11/14.
 */

public class SignModel {

    public String partner;

    public String user_id;

    public String timestamp;

    public String sign;

    public String reg_hongbao_user;

    @Override
    public String toString() {
        return "SignModel{" +
                "partner='" + partner + '\'' +
                ", user_id='" + user_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", reg_hongbao_user='" + reg_hongbao_user + '\'' +
                '}';
    }
}
