package com.yunzhanghu.redpacketdemo.utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.yunzhanghu.redpacketsdk.RPGroupMemberListener;
import com.yunzhanghu.redpacketsdk.RPValueCallback;
import com.yunzhanghu.redpacketsdk.RedPacket;
import com.yunzhanghu.redpacketsdk.bean.RPUserBean;
import com.yunzhanghu.redpacketsdk.bean.RedPacketInfo;
import com.yunzhanghu.redpacketsdk.bean.TokenData;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;
import com.yunzhanghu.redpacketui.ui.activity.RPChangeActivity;
import com.yunzhanghu.redpacketui.ui.activity.RPRedPacketActivity;
import com.yunzhanghu.redpacketui.ui.activity.RPTransferActivity;
import com.yunzhanghu.redpacketui.ui.activity.RPTransferDetailActivity;
import com.yunzhanghu.redpacketui.utils.RPRedPacketUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.yunzhanghu.redpacketdemo.DemoApplication.sCurrentAvatarUrl;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sCurrentNickname;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sCurrentUserId;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sToAvatarUrl;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sToNickname;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sToUserId;

/**
 * Created by Max on 2016/11/22.
 */

public class DemoUtil {

    private static int mGroupMemberCount = 10;

    /**
     * @param activity    FragmentActivity
     * @param chatType    聊天类型
     * @param isSpecial   是否是专属红包
     * @param requestCode requestCode
     */
    public static void startRedPacketForResult(FragmentActivity activity, int chatType, boolean isSpecial, int requestCode) {
        Intent intent = new Intent(activity, RPRedPacketActivity.class);
        intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO, paramsWrapper(chatType, isSpecial));
        intent.putExtra(RPConstant.EXTRA_TOKEN_DATA, getTokenData());
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 进入小额随机红包页面
     *
     * @param activity FragmentActivity
     * @param callBack RPRandomCallback
     */
    public static void startRandomPacket(FragmentActivity activity, RPRedPacketUtil.RPRandomCallback callBack) {
        //当前用户昵称和头像rul
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        redPacketInfo.chatType = RPConstant.CHATTYPE_SINGLE;
        redPacketInfo.fromNickName = sCurrentNickname;
        redPacketInfo.fromAvatarUrl = sCurrentAvatarUrl;
        //接收者id、昵称和头像url
        redPacketInfo.toUserId = sToUserId;
        redPacketInfo.toNickName = sToNickname;
        redPacketInfo.toAvatarUrl = sToAvatarUrl;
        RPRedPacketUtil.getInstance().enterRandomRedPacket(redPacketInfo, getTokenData(), activity, callBack);
    }

    /**
     * 进入转账页面
     *
     * @param activity    FragmentActivity
     * @param requestCode requestCode
     */
    public static void startTransferActivityForResult(FragmentActivity activity, int requestCode) {
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        //当前用户头像Url
        redPacketInfo.fromAvatarUrl = sCurrentAvatarUrl;
        //当前用户昵称
        redPacketInfo.fromNickName = sCurrentNickname;
        //接收者Id
        redPacketInfo.toUserId = sToUserId;
        //接收者昵称
        redPacketInfo.toNickName = sToNickname;
        //接收者头像Url
        redPacketInfo.toAvatarUrl = sToAvatarUrl;
        Intent intent = new Intent(activity, RPTransferActivity.class);
        intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO, redPacketInfo);
        intent.putExtra(RPConstant.EXTRA_TOKEN_DATA, getTokenData());
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 进入零钱页面
     *
     * @param activity FragmentActivity
     */
    public static void startChangeActivity(FragmentActivity activity) {
        RedPacketInfo redPacketInfo = getCurrentUserInfo();
        TokenData tokenData = getTokenData();
        Intent intent = new Intent(activity, RPChangeActivity.class);
        intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO, redPacketInfo);
        intent.putExtra(RPConstant.EXTRA_TOKEN_DATA, tokenData);
        activity.startActivity(intent);
    }


    /**
     * 拆红包方法
     *
     * @param activity      FragmentActivity
     * @param chatType      聊天类型
     * @param redPacketId   红包id
     * @param redPacketType 红包类型
     * @param receiverId    接收者id
     * @param messageDirect 消息的方向
     */
    public static void openRedPacket(final FragmentActivity activity, final int chatType, String redPacketId, String redPacketType, String receiverId, String messageDirect) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        redPacketInfo.redPacketId = redPacketId;
        redPacketInfo.toUserId = sCurrentUserId;
        redPacketInfo.toNickName = sCurrentNickname;
        redPacketInfo.toAvatarUrl = sCurrentAvatarUrl;
        redPacketInfo.moneyMsgDirect = messageDirect;
        redPacketInfo.chatType = chatType;
        if (!TextUtils.isEmpty(redPacketType) && redPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
            //根据mReceiverId来获取专属红包接收者的头像Url和昵称
            redPacketInfo.specialAvatarUrl = "testURL";
            redPacketInfo.specialNickname = findNicknameByUserId(receiverId);
        }
        RPRedPacketUtil.getInstance().openRedPacket(redPacketInfo, getTokenData(), activity, new RPRedPacketUtil.RPOpenPacketCallback() {
            @Override
            public void onSuccess(String senderId, String senderNickname, String myAmount) {
                //领取红包成功 发送回执消息到聊天窗口
                Toast.makeText(activity, "拆红包成功，红包金额" + myAmount + "元", Toast.LENGTH_LONG).show();
            }

            @Override
            public void showLoading() {
                progressDialog.show();
            }

            @Override
            public void hideLoading() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(String code, String message) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查看转账详情
     *
     * @param activity       FragmentActivity
     * @param messageDirect  消息的方向
     * @param transferAmount 转账金额
     * @param transferTime   转账时间
     */
    public static void openTransferPacket(FragmentActivity activity, String messageDirect, String transferAmount, String transferTime) {
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        redPacketInfo.moneyMsgDirect = messageDirect;
        redPacketInfo.fromNickName = sCurrentNickname;
        redPacketInfo.fromAvatarUrl = sCurrentAvatarUrl;
        redPacketInfo.redPacketAmount = transferAmount;
        redPacketInfo.transferTime = transferTime;
        Intent intent = new Intent(activity, RPTransferDetailActivity.class);
        intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO, redPacketInfo);
        intent.putExtra(RPConstant.EXTRA_TOKEN_DATA, getTokenData());
        activity.startActivity(intent);
    }

    /**
     * 模拟获取当前用户信息的方法
     *
     * @return RedPacketInfo
     */
    private static RedPacketInfo getCurrentUserInfo() {
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        //红包发送者昵称 不可为空
        redPacketInfo.fromNickName = sCurrentNickname;
        //红包发送者头像url 不可为空
        redPacketInfo.fromAvatarUrl = sCurrentAvatarUrl;
        return redPacketInfo;
    }

    /**
     * 进入红包页面所需参数的包装方法
     *
     * @param chatType 聊天类型（单聊1，群聊2）
     * @return RedPacketInfo
     */
    private static RedPacketInfo paramsWrapper(int chatType, boolean isSpecial) {
        RedPacketInfo redPacketInfo = getCurrentUserInfo();
        //聊天类型
        redPacketInfo.chatType = chatType;
        if (chatType == RPConstant.CHATTYPE_SINGLE) {
            //单聊红包传入 ：接收者Id
            redPacketInfo.toUserId = sToUserId;
        } else {
            //群聊红包传入 ：群组Id和群成员个数
            redPacketInfo.toGroupId = "testGroupId";
            redPacketInfo.groupMemberCount = mGroupMemberCount;
            //需要专属红包功能需要设置下面的回调函数，不需要可不设置
            if (isSpecial) {
                RedPacket.getInstance().setRPGroupMemberListener(new RPGroupMemberListener() {
                    @Override
                    public void getGroupMember(String groupId, RPValueCallback<List<RPUserBean>> rpValueCallback) {
                        rpValueCallback.onSuccess(generateGroupMemberList(mGroupMemberCount));
                    }
                });
            } else {
                //Demo演示需要 如果不需要专属红包 不设置改回调即可
                RedPacket.getInstance().setRPGroupMemberListener(null);
            }
        }
        return redPacketInfo;
    }

    private static TokenData getTokenData() {
        TokenData tokenData = new TokenData();
        //当前用户id，必传参数，用于SDK区分是否切换用户
        tokenData.appUserId = sCurrentUserId;
        return tokenData;
    }

    /**
     * 模拟生成群成员列表的方法
     *
     * @param groupMemberCount 群成员数量
     * @return 群成员列表
     */
    private static List<RPUserBean> generateGroupMemberList(int groupMemberCount) {
        //专属红包不能给自己发，所以这个列表中不能包含当前用户
        List<RPUserBean> userBeanList = new ArrayList<>();
        for (int i = 0; i < groupMemberCount; i++) {
            RPUserBean userBean = new RPUserBean();
            userBean.userId = "1000" + i;
            userBean.userNickname = "yunzhanghu00" + i;
            userBean.userAvatar = "";
            userBeanList.add(userBean);
        }
        return userBeanList;
    }

    /**
     * 模拟通过用户id 获取用户昵称的方法
     *
     * @param userId 用户id
     * @return 用户昵称
     */
    private static String findNicknameByUserId(String userId) {
        String nickname = "";
        List<RPUserBean> userBeanList = generateGroupMemberList(mGroupMemberCount);
        for (int i = 0; i < userBeanList.size(); i++) {
            if (userBeanList.get(i).userId.equals(userId)) {
                nickname = userBeanList.get(i).userNickname;
                break;
            }
        }
        return nickname;
    }


    /**
     * 红包类型的转义方法 用于展示红包的类型
     *
     * @param redPacketType 红包类型
     * @return 返回转义后的红包类型
     */
    public static String getRedPacketType(String redPacketType) {
        String typeStr = "";
        if (TextUtils.isEmpty(redPacketType)) {
            typeStr = "单聊红包";
        } else if (redPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_RANDOM)) {
            typeStr = "拼手气群红包";
        } else if (redPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_AVERAGE)) {
            typeStr = "普通群红包";
        } else if (redPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
            typeStr = "专属红包";
        }
        return typeStr;
    }


    public static void initUserInfo() {
        //缓存用户信息到本地
        sCurrentNickname = PreferenceUtil.getInstance().getSenderName();
        sToNickname = PreferenceUtil.getInstance().getReceiverName();
        //使用昵称做为种子生成的用户id，实际开发中需传入APP生成的用户id
        sCurrentUserId = UUID.nameUUIDFromBytes(sCurrentNickname.getBytes()).toString();
        sCurrentAvatarUrl = "http://i.imgur.com/DvpvklR.png";
        sToUserId = UUID.nameUUIDFromBytes(sToNickname.getBytes()).toString();
        sToAvatarUrl = "http://i.imgur.com/Nptlyr9.jpg";
    }
}
