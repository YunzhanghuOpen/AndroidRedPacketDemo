package com.yunzhanghu.redpacketdemo.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunzhanghu.redpacketdemo.R;
import com.yunzhanghu.redpacketdemo.utils.CircleTransform;
import com.yunzhanghu.redpacketsdk.RPSendPacketCallback;
import com.yunzhanghu.redpacketsdk.bean.RedPacketInfo;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;

import static com.yunzhanghu.redpacketdemo.DemoApplication.sCurrentAvatarUrl;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sCurrentNickname;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sCurrentUserId;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sToAvatarUrl;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sToNickname;
import static com.yunzhanghu.redpacketdemo.DemoApplication.sToUserId;
import static com.yunzhanghu.redpacketdemo.utils.RedPacketUtil.getRedPacketType;
import static com.yunzhanghu.redpacketdemo.utils.RedPacketUtil.openRedPacket;
import static com.yunzhanghu.redpacketdemo.utils.RedPacketUtil.openTransferPacket;
import static com.yunzhanghu.redpacketdemo.utils.RedPacketUtil.startRedPacket;

public class DetailActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "DetailActivity";

    private String mRedPacketId;

    private String mRedPacketType = "";

    private int mChatType;

    private View mSendPacketLayout;

    private View mReceivePacketLayout;

    private TextView mTvSendGreeting;

    private TextView mTvReceiveGreeting;

    private TextView mTvPacketSenderName;

    private TextView mTvPacketReceiveName;

    private ImageView mIvPacketSendAvatar;

    private ImageView mIvPacketReceiveAvatar;

    private TextView mTvSendRedPacketType;

    private TextView mTvReceiveRedPacketType;

    private View mSendTransferLayout;

    private View mReceiveTransferLayout;

    private TextView mTvSendTransfer;

    private TextView mTvReceiveTransfer;

    private String mTransferAmount;

    private String mTransferTime;

    private TextView mTvTransferSenderName;

    private TextView mTvTransferReceiveName;

    private ImageView mIvTransferSendAvatar;

    private ImageView mIvTransferReceiveAvatar;

    private String mReceiverId;

    private TextView mTvCurrentUserNickname;

    private ImageView mIvCurrentUserAvatar;

    private String mCurrentDirect = RPConstant.MESSAGE_DIRECT_SEND;

    private String mDirectReceive = RPConstant.MESSAGE_DIRECT_RECEIVE;

    private boolean mHasSentRedPacket = false;

    private boolean mHasSentTransferPacket = false;

    private TextView mTvRedPacketType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        switchButtonLayout();
        findViewById(R.id.btn_enter_single_red_packet).setOnClickListener(this);
        findViewById(R.id.btn_enter_group_red_packet).setOnClickListener(this);
        findViewById(R.id.btn_enter_group_p2p_red_packet).setOnClickListener(this);
        findViewById(R.id.btn_enter_transfer_packet).setOnClickListener(this);
        findViewById(R.id.btn_swap_user).setOnClickListener(this);
        findViewById(R.id.btn_enter_random_packet).setOnClickListener(this);
        mSendPacketLayout = findViewById(R.id.layout_red_packet_send);
        mSendPacketLayout.setOnClickListener(this);
        mReceivePacketLayout = findViewById(R.id.layout_red_packet_receive);
        mReceivePacketLayout.setOnClickListener(this);
        mTvSendGreeting = (TextView) findViewById(R.id.tv_send_greeting);
        mTvReceiveGreeting = (TextView) findViewById(R.id.tv_receive_greeting);
        mTvPacketSenderName = (TextView) findViewById(R.id.tv_send_username);
        mTvPacketReceiveName = (TextView) findViewById(R.id.tv_receive_username);
        mTvSendRedPacketType = (TextView) findViewById(R.id.tv_send_packet_type);
        mTvReceiveRedPacketType = (TextView) findViewById(R.id.tv_receive_packet_type);
        mSendTransferLayout = findViewById(R.id.layout_transfer_send);
        mSendTransferLayout.setOnClickListener(this);
        mReceiveTransferLayout = findViewById(R.id.layout_transfer_receive);
        mReceiveTransferLayout.setOnClickListener(this);
        mTvSendTransfer = (TextView) findViewById(R.id.tv_transfer_send);
        mTvReceiveTransfer = (TextView) findViewById(R.id.tv_transfer_receive);
        mTvTransferSenderName = (TextView) findViewById(R.id.tv_transfer_send_name);
        mTvTransferReceiveName = (TextView) findViewById(R.id.tv_transfer_receive_name);
        mTvCurrentUserNickname = (TextView) findViewById(R.id.tv_current_user_nickname);
        mIvCurrentUserAvatar = (ImageView) findViewById(R.id.iv_current_user_avatar);
        mIvPacketSendAvatar = (ImageView) findViewById(R.id.iv_red_packet_send_avatar);
        mIvPacketReceiveAvatar = (ImageView) findViewById(R.id.iv_red_packet_receive_avatar);
        mIvTransferSendAvatar = (ImageView) findViewById(R.id.iv_transfer_send_avatar);
        mIvTransferReceiveAvatar = (ImageView) findViewById(R.id.iv_transfer_receive_avatar);
        mTvRedPacketType = (TextView) findViewById(R.id.tv_red_packet_type);
        mTvCurrentUserNickname.setText(sCurrentNickname);
        Glide.with(this).load(sCurrentAvatarUrl).error(R.drawable.default_avatar).placeholder(R.drawable.default_avatar).transform(new CircleTransform(this)).into(mIvCurrentUserAvatar);
    }

    private void switchButtonLayout() {
        View singleButtonLayout = findViewById(R.id.layout_single);
        View groupButtonLayout = findViewById(R.id.layout_group);
        int fromFlag = getIntent().getIntExtra("from", 0);
        switch (fromFlag) {
            case 0:
                singleButtonLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                groupButtonLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        boolean isExclusive = false;
        int itemType = 0;
        switch (view.getId()) {
            case R.id.btn_enter_single_red_packet:
                mChatType = RPConstant.CHATTYPE_SINGLE;
                itemType = RPConstant.RP_ITEM_TYPE_SINGLE;
                break;
            case R.id.btn_enter_random_packet:
                mChatType = RPConstant.CHATTYPE_SINGLE;
                itemType = RPConstant.RP_ITEM_TYPE_RANDOM;
                break;
            case R.id.btn_enter_group_red_packet:
                mChatType = RPConstant.CHATTYPE_GROUP;
                itemType = RPConstant.RP_ITEM_TYPE_GROUP;
                break;
            case R.id.btn_enter_group_p2p_red_packet:
                isExclusive = true;
                mChatType = RPConstant.CHATTYPE_GROUP;
                itemType = RPConstant.RP_ITEM_TYPE_GROUP;
                break;
            case R.id.btn_enter_transfer_packet:
                mChatType = RPConstant.CHATTYPE_SINGLE;
                itemType = RPConstant.RP_ITEM_TYPE_TRANSFER;
                break;
            case R.id.btn_swap_user:
                swapUser();
                break;
            case R.id.layout_red_packet_send:
            case R.id.layout_red_packet_receive:
                openRedPacket(this, mChatType, mRedPacketId, mRedPacketType, mReceiverId, mCurrentDirect);
                break;
            case R.id.layout_transfer_send:
            case R.id.layout_transfer_receive:
                openTransferPacket(this, mCurrentDirect, mTransferAmount, mTransferTime);
                break;
        }
        if (itemType != 0) {
            startRedPacket(this, itemType, isExclusive, new RPSendPacketCallback() {
                @Override
                public void onSendPacketSuccess(RedPacketInfo redPacketInfo) {
                    clearMessageBubble();
                    if (redPacketInfo.redPacketType.equals(RPConstant.RED_PACKET_TYPE_TRANSFER)) {
                        mTransferAmount = redPacketInfo.redPacketAmount;
                        mTransferTime = redPacketInfo.transferTime;
                        showTransferMsg();
                    } else {
                        mRedPacketId = redPacketInfo.redPacketId;
                        mRedPacketType = redPacketInfo.redPacketType;
                        String greetings = redPacketInfo.redPacketGreeting;
                        mReceiverId = redPacketInfo.toUserId;
                        String senderNickname = redPacketInfo.fromNickName;
                        showRedPacketMsg(greetings);
                    }
                }

                @Override
                public void onGenerateRedPacketId(String redPacketId) {

                }
            });
        }
    }


    /**
     * 模拟在IM聊天页面中展示红包消息卡片
     *
     * @param greetings 祝福语
     */
    private void showRedPacketMsg(String greetings) {
        mHasSentRedPacket = true;
        mHasSentTransferPacket = false;
        mTvSendGreeting.setText(greetings);
        mTvReceiveGreeting.setText(greetings);
        mTvPacketSenderName.setText(sCurrentNickname);
        Glide.with(this).load(sCurrentAvatarUrl).error(R.drawable.default_avatar).placeholder(R.drawable.default_avatar).transform(new CircleTransform(this)).into(mIvPacketSendAvatar);
        if (mRedPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
            mTvSendRedPacketType.setVisibility(View.VISIBLE);
        } else {
            mTvSendRedPacketType.setVisibility(View.GONE);
        }
        mSendPacketLayout.setVisibility(View.VISIBLE);
        mTvRedPacketType.setVisibility(View.VISIBLE);
        mTvRedPacketType.setText(String.format(getString(R.string.str_red_packet_type), getRedPacketType(mRedPacketType)));
    }

    /**
     * 模拟在IM页面中展示转账消息卡片
     */
    private void showTransferMsg() {
        mHasSentTransferPacket = true;
        mHasSentRedPacket = false;
        mTvTransferSenderName.setText(sCurrentNickname);
        Glide.with(this).load(sCurrentAvatarUrl).error(R.drawable.default_avatar).placeholder(R.drawable.default_avatar).transform(new CircleTransform(this)).into(mIvTransferSendAvatar);
        mTvSendTransfer.setText(String.format(getString(R.string.msg_transfer_from_you), mTransferAmount));
        mSendTransferLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 模拟切换用户的方法
     */
    private void swapUser() {
        String tempUserId = sCurrentUserId;
        sCurrentUserId = sToUserId;
        sToUserId = tempUserId;
        String tempNickname = sCurrentNickname;
        sCurrentNickname = sToNickname;
        sToNickname = tempNickname;
        String tempAvatarUrl = sCurrentAvatarUrl;
        sCurrentAvatarUrl = sToAvatarUrl;
        sToAvatarUrl = tempAvatarUrl;
        mTvCurrentUserNickname.setText(sCurrentNickname);
        Glide.with(this).load(sCurrentAvatarUrl).error(R.drawable.default_avatar).placeholder(R.drawable.default_avatar).transform(new CircleTransform(this)).into(mIvCurrentUserAvatar);
        mTvPacketReceiveName.setText(tempNickname);
        mTvTransferReceiveName.setText(tempNickname);
        Glide.with(this).load(tempAvatarUrl).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).transform(new CircleTransform(this)).into(mIvPacketReceiveAvatar);
        Glide.with(this).load(tempAvatarUrl).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).transform(new CircleTransform(this)).into(mIvTransferReceiveAvatar);
        if (!mHasSentRedPacket && !mHasSentTransferPacket) {
            return;
        }
        String tempDirect = mCurrentDirect;
        mCurrentDirect = mDirectReceive;
        mDirectReceive = tempDirect;
        if (mCurrentDirect.equals(RPConstant.MESSAGE_DIRECT_SEND)) {
            if (mHasSentRedPacket) {
                mSendPacketLayout.setVisibility(View.VISIBLE);
                mReceivePacketLayout.setVisibility(View.GONE);
            }
            if (mHasSentTransferPacket) {
                mSendTransferLayout.setVisibility(View.VISIBLE);
                mReceiveTransferLayout.setVisibility(View.GONE);
            }
        } else if (mCurrentDirect.equals(RPConstant.MESSAGE_DIRECT_RECEIVE)) {
            if (mHasSentRedPacket) {
                mSendPacketLayout.setVisibility(View.GONE);
                mReceivePacketLayout.setVisibility(View.VISIBLE);
                if (mRedPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
                    mTvReceiveRedPacketType.setVisibility(View.VISIBLE);
                } else {
                    mTvReceiveRedPacketType.setVisibility(View.GONE);
                }
            }
            if (mHasSentTransferPacket) {
                mSendTransferLayout.setVisibility(View.GONE);
                mTvReceiveTransfer.setText(String.format(getString(R.string.msg_transfer_to_you), mTransferAmount));
                mReceiveTransferLayout.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 清除消息气泡的状态
     */
    private void clearMessageBubble() {
        mHasSentRedPacket = false;
        mHasSentTransferPacket = false;
        mCurrentDirect = RPConstant.MESSAGE_DIRECT_SEND;
        mDirectReceive = RPConstant.MESSAGE_DIRECT_RECEIVE;
        mSendPacketLayout.setVisibility(View.GONE);
        mReceivePacketLayout.setVisibility(View.GONE);
        mSendTransferLayout.setVisibility(View.GONE);
        mReceiveTransferLayout.setVisibility(View.GONE);
    }


}
