

# 云账户红包SDK接入指南(支付宝版1.1.1)

## 一、依赖红包SDK

#####步骤1.在工程的build.gradle(Top-level build file)中添加远程仓库地址

```java
allprojects {
    repositories {
        jcenter()
        maven { url "https://raw.githubusercontent.com/YunzhanghuOpen/redpacket-maven-repo/master/release" }
    }
}
```

#####步骤2.在app的build.gradle(module build file)中添加红包SDK以及红包使用的三方库依赖
```java
dependencies {
    //更新红包SDK时只需要需要修改版本号即可。例如1.1.1修改为1.1.2。
    compile 'com.yunzhanghu.redpacket:redpacket-alipay:1.1.1@aar'
    compile files('libs/glide-3.7.0.jar')
    compile files('libs/alipaySdk-20161129.jar')
    compile files('libs/volley-1.0.19.jar')
    compile 'com.android.support:support-v4:25.1.0'
}
```

## 二、开始集成

### 1.配置清单文件

```java
<!--发红包页面-->
<activity
   android:name="com.yunzhanghu.redpacketui.ui.activity.RPRedPacketActivity"
   android:screenOrientation="portrait"
   android:windowSoftInputMode="adjustPan|stateVisible"/>
<!--红包详情页面-->          
<activity
   android:name="com.yunzhanghu.redpacketui.ui.activity.RPDetailActivity"
   android:screenOrientation="portrait"
   android:windowSoftInputMode="adjustPan"/>
<!--红包记录页面-->
<activity
   android:name="com.yunzhanghu.redpacketui.ui.activity.RPRecordActivity"
   android:screenOrientation="portrait"
   android:windowSoftInputMode="adjustPan"/>
<!--群成员列表页面-->
<activity
   android:name="com.yunzhanghu.redpacketui.ui.activity.RPGroupMemberActivity"
   android:screenOrientation="portrait"
   android:windowSoftInputMode="adjustPan|stateHidden"/>
<!--支付宝H5支付页面-->          
<activity
   android:name="com.alipay.sdk.app.H5PayActivity"
   android:configChanges="orientation|keyboardHidden|navigation|screenSize"
   android:exported="false"
   android:screenOrientation="behind"
   android:windowSoftInputMode="adjustResize|stateHidden" /> 
<!--支付宝H5授权页面-->          
<activity
   android:name="com.alipay.sdk.app.H5AuthActivity"
   android:configChanges="orientation|keyboardHidden|navigation|screenSize"
   android:exported="false"
   android:screenOrientation="behind"
   android:windowSoftInputMode="adjustResize|stateHidden"/>
<!-- 配置权限 -->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
```

### 2.初始化红包SDK

* **在Application的onCreate()方法中**

```java
RedPacket.getInstance().initRedPacket(context, RPConstant.AUTH_METHOD_SIGN, new RPInitRedPacketCallback() {
            @Override
            public void initTokenData(RPValueCallback<TokenData> callback) {
                // TokenData中的四个参数需要开发者请求APP Server的签名接口获取。
         		TokenData tokenData = new TokenData();
                // authPartner为商户id，需要在云账户官网注册商户后获取
         		tokenData.authPartner = authPartner;
                // 用户id
         		tokenData.appUserId = appUserId;
                // 时间戳
         		tokenData.timestamp = authTimestamp;
                // 签名
         		tokenData.authSign = authSign;
         		// 请求签名接口成功后,回调给红包SDK.
         		callback.onSuccess(tokenData);
            }
            @Override
            public RedPacketInfo initCurrentUserSync() {
                //这里需要同步设置当前用户id、昵称和头像url
                RedPacketInfo redPacketInfo = new RedPacketInfo();
                redPacketInfo.fromUserId = "yunzhanghu";
                redPacketInfo.fromAvatarUrl = "testURL";
                redPacketInfo.fromNickName = "yunzhanghu001";
                return redPacketInfo;
            }
        });
// 打开Log开关，正式发布需要关闭
RedPacket.getInstance().setDebugMode(true); 
```

* **initRedPacket(context, authMethod, callback) 参数说明**


| 参数名称       | 参数类型                    | 参数说明  | 必填         |
| ---------- | ----------------------- | ----- | ---------- |
| context    | Context                 | 上下文   | 是          |
| authMethod | String                  | 授权类型  | 是**（见注1）** |
| callback   | RPInitRedPacketCallback | 初始化接口 | 是          |


* **RPInitRedPacketCallback 接口说明**

| **initTokenData(RPValueCallback<TokenData> callback)** |
| ---------------------------------------- |
| **该方法用于初始化TokenData，在进入红包相关页面、红包Token不存在或红包Token过期时调用。TokenData是请求红包Token所需要的数据模型，建议在该方法中异步向APP服务器获取相关参数，以保证数据的有效性；不建议从本地缓存中获取TokenData所需的参数，可能导致获取红包Token无效。** |
| **initCurrentUserSync()**                |
| **该方法用于初始化当前用户信息，在进入红包相关页面时调用，需同步获取。**   |

* **注1 ：**


**使用签名方式获取红包Token时，authMethod赋值必须为RPConstant.AUTH_METHOD_SIGN。**

* **注意：App Server提供的获取签名的接口必须先验证用户身份，并保证签名的用户和该登录用户一致，防止该接口被滥用。详见云账户[REST API开发文档](http://yunzhanghu-com.oss-cn-qdjbp-a.aliyuncs.com/%E4%BA%91%E8%B4%A6%E6%88%B7%E7%BA%A2%E5%8C%85%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3-v3_1_0.pdf)** 


### 3.进入红包页面方法

```java
RPRedPacketUtil.getInstance().startRedPacket(activity, itemType(见注2), redPacketInfo, new RPSendPacketCallback() {
            @Override
            public void onGenerateRedPacketId(String redPacketId) {
                // 生成红包id后回调该接口，可使用红包id查询红包状态（详见7.1）
            }
            @Override
            public void onSendPacketSuccess(RedPacketInfo redPacketInfo) {
                // 发送红包或转账消息到IM聊天页面
            }
```
* **单聊红包、小额随机红包传入参数**	

| 参数名称        | 参数类型   | 参数说明     | 必填   |
| ----------- | ------ | -------- | ---- |
| toUserId    | String | 接收者id    | 是    |
| toNickname  | String | 接收者昵称    | 是    |
| toAvatarUrl | String | 接收者头像url | 是    |

* **群聊红包redPacketInfo传入参数**

| 参数名称             | 参数类型   | 参数说明  | 必填   |
| ---------------- | ------ | ----- | ---- |
| toGroupId        | String | 群组id  | 是    |
| groupMemberCount | int    | 群成员个数 | 是    |

* **onSendPacketSuccess中RedPacketInfo返回数据说明**


| 参数名称              | 参数类型   | 参数说明      |
| ----------------- | ------ | --------- |
| redPacketId       | String | 红包id      |
| redPacketGreeting | String | 红包祝福语     |
| redPacketType     | String | 红包类型      |
| toUserId          | String | 接收者id或群id |

* **注2 : **


**itemType有以下几个常量值**

| itemType类型 | 常量值                            |
| ---------- | ------------------------------ |
| 单聊红包       | RPConstant.RP_ITEM_TYPE_SINGLE |
| 群聊红包       | PConstant.RP_ITEM_TYPE_GROUP   |
| 小额随机红包     | RPConstant.RP_ITEM_TYPE_RANDOM |

* **注3 : **

**RPGroupMemberListener在使用群专属红包时需要设置，不需要专属红包的开发者，可以不设置该回调函数。**

* **参考示例**

```java
RedPacket.getInstance().setRPGroupMemberListener(new RPGroupMemberListener() {
          @Override
          public void getGroupMember(String groupId, RPValueCallback<List<RPUserBean>> callback) {
              List<RPUserBean> userBeanList = new ArrayList<RPUserBean>();
              for (int i = 0; i < groupMembers.size(); i++) {
                  // RPUserBean中需要设置群成员的id、昵称和头像url(不包含当前用户)
                  RPUserBean userBean = new RPUserBean();
                  userBean.userId = "userId";
                  userBean.userNickname = "nickName";
                  userBean.userAvatar = "avatarUrl";
                  userBeanList.add(userBean);
              }
              callback.onSuccess(userBeanList);
          }
      });
```
### 4.拆红包方法

#### 4.1 拆单聊、群聊、小额随机红包

```JAVA
final ProgressDialog progressDialog = new ProgressDialog(context);
RPRedPacketUtil.getInstance().openRedPacket(redPacketInfo, activity, new RPRedPacketUtil.RPOpenPacketCallback() {
        @Override
        public void onSuccess(String senderId, String senderNickname, String myAmount)       
        {
             // 发送红包回执消息到聊天页面
        }

        @Override
        public void showLoading() {
           progressDialog.show();  
        }

        @Override
        public void hideLoading() {
           progressDialog.hide();
        }

        @Override
        public void onError(String code, String message) {
           // 错误处理
        }
 });
```


* **redPacketInfo传入参数说明**


| 参数名称          | 参数类型   | 参数说明           |
| ------------- | ------ | -------------- |
| redPacketId   | String | 红包id           |
| messageDirect | String | 消息的方向**(见注4)** |
| chatType      | int    | 聊天类型           |

- **注 4 ：**

**messageDirect为RPConstant.MESSAGE_DIRECT_SEND或RPConstant.MESSAGE_DIRECT_RECEIVE**

#### 4.2 拆营销红包（原广告红包）

```java
final ProgressDialog progressDialog = new ProgressDialog(context);
RPRedPacketUtil.getInstance().openADRedPacket(redPacketId, activity, true, new RPADPacketCallback() {
        @Override
        public void onReceivePacketSuccess(String myAmount) {
            // 领取营销红包成功，供调用者做统计用
        }
        @Override
        public void onDetailSuccess(RedPacketInfo data) {
            // 进入营销红包详情页面成功，供调用者做统计用
        }
        @Override
        public void shareToFriends(FragmentActivity activity, String shareMsg, String campaignCode) {
            // 分享给朋友，由调用者实现分享到社交平台的功能。
        }
        @Override
        public void loadLandingPage(FragmentActivity activity, String url, String title){
            // 加载营销链接 
        }
        Override
        public void showLoading() {
            progressDialog.show();
        }
        @Override
        public void hideLoading() {
            progressDialog.hide();
        }
        @Override
        public void onError(String code, String message) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
});
```

* **传入参数说明**

| 参数名称        | 参数类型               | 参数说明             |
| ----------- | ------------------ | ---------------- |
| redPacketId | String             | 红包id             |
| activity    | FragmentActivity   | FragmentActivity |
| isPlaySound | boolean            | 是否播放拆营销红包声音      |
| callback    | RPADPacketCallback | 拆营销红包回调接口        |

### 5.进入红包记录页面

* **方法一**

```java
//展示切换收发红包记录的按钮
RPRedPacketUtil.getInstance().startRecordActivity(context)
```

* **方法二**

```java
//第二个参数设置为false,则不展示切换收发红包记录的按钮
RPRedPacketUtil.getInstance().startRecordActivity(context,false)
```

### 6.增加拆红包音效

* **在app的assets目录下增加名为open_packet_sound.mp3或open_packet_sound.wav文件即可（文件名不可更改）。**


### 7.新增接口说明

#### 7.1.查询红包状态接口

```java
RPRedPacketUtil.getInstance().checkRedPacketStatus(redPacketId, new RPValueCallback<RedPacketInfo>() {
    @Override
    public void onSuccess(RedPacketInfo data) {
         // 查询成功，返回发红包所需数据               
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
         
    }
 });
}
```

* **参数说明**

| 参数名称        | 参数类型                         | 参数说明     |
| ----------- | ---------------------------- | -------- |
| redPacketId | String                       | 红包id     |
| callback    | RPValueCallback<RedPackInfo> | 查询接口回调函数 |

* **该接口可用于在某些异常情况下出现的用户支付成功却未发出红包消息的情形。**
* **onSuccess返回即说明此红包存在，如未发出红包消息卡片，此时可通过返回的红包数据组装一个红包消息，发送到聊天页面中，以达到补发红包的目的。**
* **onError说明此红包并不存在或其他错误，可不进行补发操作。**
* **onSuccess中RedPacketInfo返回数据说明**


| 参数名称              | 参数类型   | 参数说明      |
| ----------------- | ------ | --------- |
| redPacketId       | String | 红包id      |
| redPacketType     | String | 红包类型      |
| redPacketGreeting | String | 红包祝福语     |
| toUserId          | String | 接收者id或群id |


#### 7.2.detachView接口

```java
RPRedPacketUtil.getInstance().detachView();
```

* **在拆红包方法所在页面销毁时调用，可防止内存泄漏。**
* **调用示例（以Fragment为例）**

```java
@Override
public void onDestroy() {
    super.onDestroy();
    RPRedPacketUtil.getInstance().detachView();
}
```







