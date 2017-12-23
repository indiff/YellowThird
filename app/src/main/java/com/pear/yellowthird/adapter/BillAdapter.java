package com.pear.yellowthird.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.vo.databases.BillVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 账单的适配器
 */

public class BillAdapter extends BaseAdapter {

    Context mContext;

    List<BillVo> mData=new ArrayList<>();

    public BillAdapter(Context context)
    {
        mContext=context;
    }

    public List<BillVo> getData() {
        return mData;
    }

    public void setData(List<BillVo> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_account_bill_list_line, null);
        }

        BillVo data = mData.get(position);

        /**消费内容概要*/
        {
            TextView contentView=convertView.findViewById(R.id.content);
            contentView.setText(data.getDescription());
        }

        /**产生时间*/
        {
            TextView timeView=convertView.findViewById(R.id.time);
            timeView.setText(data.getPayTime());
        }

        /**金额数量*/
        {
            TextView moneyView=convertView.findViewById(R.id.money);

            double money=data.getGold();
            String moneyText=String.valueOf(money);

            /**参考别的app +号更加美化*/
            if(money>0)
                moneyText="+"+moneyText;

            moneyView.setText(moneyText);
        }

        return convertView;
    }
}
