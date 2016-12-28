package com.yunzhanghu.redpacketdemo.utils;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.yunzhanghu.redpacketsdk.RPGroupMemberListener;
import com.yunzhanghu.redpacketsdk.RPSendPacketCallback;
import com.yunzhanghu.redpacketsdk.RPValueCallback;
import com.yunzhanghu.redpacketsdk.RedPacket;
import com.yunzhanghu.redpacketsdk.bean.RPUserBean;
import com.yunzhanghu.redpacketsdk.bean.RedPacketInfo;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;
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

public class RedPacketUtil {

    private static int mGroupMemberCount = 10;

    public static void startRedPacket(FragmentActivity activity, int itemType, boolean isExclusive, RPSendPacketCallback callback) {
        RPRedPacketUtil.getInstance().startRedPacket(activity, itemType, getRedPacketInfo(itemType, isExclusive), callback);
    }


    /**
     * 拆红包方法
     *
     * @param activity      FragmentActivity(由于使用了DialogFragment，这个参数类型必须为FragmentActivity)
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
        redPacketInfo.messageDirect = messageDirect;
        redPacketInfo.chatType = chatType;
        //如果在3.4.0之前使用过红包SDK,并已经有上线版本，需要添加如下代码对旧版做兼容;处于开发阶段的用户可以不添加。
        if (!TextUtils.isEmpty(redPacketType) && redPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
            //根据receiverId来获取专属红包接收者的头像url和昵称
            redPacketInfo.specialAvatarUrl = "testUrl";
            redPacketInfo.specialNickname = findNicknameByUserId(receiverId);
        }
        //兼容 end
        RPRedPacketUtil.getInstance().openRedPacket(redPacketInfo, activity, new RPRedPacketUtil.RPOpenPacketCallback() {
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
     * 拆转账红包方法
     * <p>
     * 由于打开转账传入参数和拆红包方法无法兼容，这里独立封装了一个方法
     *
     * @param activity       FragmentActivity
     * @param messageDirect  消息的方向
     * @param transferAmount 转账金额
     * @param transferTime   转账时间
     */
    public static void openTransferPacket(FragmentActivity activity, String messageDirect, String transferAmount, String transferTime) {
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        redPacketInfo.messageDirect = messageDirect;
        redPacketInfo.redPacketAmount = transferAmount;
        redPacketInfo.transferTime = transferTime;
        RPRedPacketUtil.getInstance().openTransferPacket(activity, redPacketInfo);
    }

    /**
     * 封装进入红包、转账页面所需参数
     *
     * @param itemType    项目类型：
     *                    (单聊红包：RPConstant.RP_ITEM_TYPE_SINGLE
     *                    群聊红包：RPConstant.RP_ITEM_TYPE_GROUP
     *                    小额随机红包：RPConstant.RP_ITEM_TYPE_RANDOM
     *                    转账：RPConstant.RP_ITEM_TYPE_TRANSFER）
     * @param isExclusive 是否为专属红包
     * @return RedPacketInfo
     */
    private static RedPacketInfo getRedPacketInfo(int itemType, boolean isExclusive) {
        RedPacketInfo redPacketInfo = getCurrentUserInfo();
        //项目类型
        if (itemType == RPConstant.RP_ITEM_TYPE_GROUP) {
            //群聊红包传入 ：群组Id和群成员个数
            redPacketInfo.toGroupId = "testGroupId";
            redPacketInfo.groupMemberCount = mGroupMemberCount;
            //使用专属红包功能需要设置如下回调函数，不需要可不设置。
            if (isExclusive) {
                RedPacket.getInstance().setRPGroupMemberListener(new RPGroupMemberListener() {
                    @Override
                    public void getGroupMember(String groupId, RPValueCallback<List<RPUserBean>> rpValueCallback) {
                        rpValueCallback.onSuccess(generateGroupMemberList(mGroupMemberCount));
                    }
                });
            } else {
                //Demo演示使用，如果不需要专属红包，不设置该回调即可。
                RedPacket.getInstance().setRPGroupMemberListener(null);
            }
        } else {
            //单聊红包、小额随机红包和转账都只传入 ：接收者Id、昵称和头像
            redPacketInfo.toUserId = sToUserId;
            redPacketInfo.toNickName = sToNickname;
            redPacketInfo.toAvatarUrl = sToAvatarUrl;
        }
        return redPacketInfo;
    }


    /**
     * 模拟获取当前用户信息的方法
     *
     * @return RedPacketInfo
     */
    public static RedPacketInfo getCurrentUserInfo() {
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        //红包发送者昵称 不可为空
        redPacketInfo.fromNickName = sCurrentNickname;
        //红包发送者头像url 不可为空
        redPacketInfo.fromAvatarUrl = sCurrentAvatarUrl;
        return redPacketInfo;
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
        } else if (redPacketType.equals(RPConstant.RED_PACKET_TYPE_RANDOM)) {
            typeStr = "小额随机红包";
        }
        return typeStr;
    }

    /**
     * 模拟初始化用户信息的方法
     */
    public static void initUserInfo() {
        //缓存用户信息到本地
        sCurrentNickname = "Max";
        sToNickname = PreferenceUtil.getInstance().getReceiverName();
        //使用昵称做为种子生成的用户id，实际开发中需传入APP生成的用户id
        sCurrentUserId = UUID.nameUUIDFromBytes(sCurrentNickname.getBytes()).toString();
        sCurrentAvatarUrl = "http://i.imgur.com/DvpvklR.png";
        sToUserId = UUID.nameUUIDFromBytes(sToNickname.getBytes()).toString();
        sToAvatarUrl = "http://i.imgur.com/oqJ9rSg.jpg";
    }
}
