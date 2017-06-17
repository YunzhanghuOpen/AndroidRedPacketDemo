package com.yunzhanghu.redpacketdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunzhanghu.redpacketdemo.R;

/**
 * Created by Max on 2016/12/7
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] mStrArrays = {"单聊红包", "群聊红包", "系统红包", "广告红包", "零钱页面", "联系我们"};

    public RecyclerAdapter() {
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mStrArrays[position]);
    }

    @Override
    public int getItemCount() {
        return mStrArrays.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_item);
        }
    }
}
