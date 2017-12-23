package com.pear.yellowthird.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.pear.yellowthird.activitys.base.CommonHeadActivity;
import com.pear.yellowthird.adapter.BillAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.BillVo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import rx.functions.Action1;

/**
 * 账单
 */

public class BillActivity extends CommonHeadActivity {

    /**账单集合界面*/
    ListView mListView;

    /**账单的适配器*/
    BillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = buildRootView();
        setContentView(rootView);
        initHeadBar("账单");
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshBillList();
    }


    /**
     * 生产根目录的视图
     */
    private View buildRootView() {
        LinearLayout rootView = new LinearLayout(this);
        rootView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        rootView.setOrientation(LinearLayout.VERTICAL);

        /**头部状态栏*/
        {
            View headView = getLayoutInflater().inflate(R.layout.sub_common_head, null);
            rootView.addView(headView);
        }

        /**账单集合*/
        {
            mListView = new ListView(this);
            mListView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));

            mListView.setVerticalScrollBarEnabled(false);
            adapter=new BillAdapter(this);
            mListView.setAdapter(adapter);

            rootView.addView(mListView);
        }


        return rootView;
    }

    /**
     * 刷新账单信息
     * */
    private void refreshBillList()
    {

        ServiceDisposeFactory.getInstance().getServiceDispose().getBilli().subscribe(new Action1<BillVo[]>() {
            @Override
            public void call(BillVo[] datas) {
                adapter.setData(Arrays.asList(datas));
            }
        });;

        /*
        mListView.setAdapter(new BillAdapter(this,
                new ArrayList<BillVo>()
                {{
                    add(new BillVo("充值100绿币",new Date(System.currentTimeMillis()),100));
                    add(new BillVo("查看川老师的电影",new Date(System.currentTimeMillis()-1000*60*60),-1.00));
                    add(new BillVo("查看动物的图片",new Date(System.currentTimeMillis()-1000*60*60*24*7),-0.11));

                    add(new BillVo("充值100绿币",new Date(System.currentTimeMillis()),100));
                    add(new BillVo("查看川老师的电影",new Date(System.currentTimeMillis()-1000*60*60),-1.00));
                    add(new BillVo("查看动物的图片",new Date(System.currentTimeMillis()-1000*60*60*24*7),-0.11));
                    add(new BillVo("充值100绿币",new Date(System.currentTimeMillis()),100));
                    add(new BillVo("查看川老师的电影",new Date(System.currentTimeMillis()-1000*60*60),-1.00));
                    add(new BillVo("查看动物的图片",new Date(System.currentTimeMillis()-1000*60*60*24*7),-0.11));
                    add(new BillVo("充值100绿币",new Date(System.currentTimeMillis()),100));
                    add(new BillVo("查看川老师的电影",new Date(System.currentTimeMillis()-1000*60*60),-1.00));
                    add(new BillVo("查看动物的图片",new Date(System.currentTimeMillis()-1000*60*60*24*7),-0.11));
                    add(new BillVo("充值100绿币",new Date(System.currentTimeMillis()),100));
                    add(new BillVo("查看川老师的电影",new Date(System.currentTimeMillis()-1000*60*60),-1.00));
                    add(new BillVo("查看动物的图片",new Date(System.currentTimeMillis()-1000*60*60*24*7),-0.11));
                    add(new BillVo("充值100绿币",new Date(System.currentTimeMillis()),100));
                    add(new BillVo("查看川老师的电影",new Date(System.currentTimeMillis()-1000*60*60),-1.00));
                    add(new BillVo("查看动物的图片",new Date(System.currentTimeMillis()-1000*60*60*24*7),-0.11));



                }}));
        */
    }



}