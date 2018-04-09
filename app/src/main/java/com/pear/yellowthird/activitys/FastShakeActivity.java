package com.pear.yellowthird.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.databases.FastShakeDatabases;
import com.pear.yellowthird.adapter.FastShakeAdapter;
import com.pear.yellowthird.adapter.abstracts.RecycleViewItemListener;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.init.AllOnceInit;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.vo.databases.FastShakeVo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * 快抖的主界面,短视频
 */

public class FastShakeActivity extends AppCompatActivity implements View.OnClickListener{

    /**上下拉刷新*/
    RecyclerViewPager recyclerView;

    /**
     * 短视频数据的适配器
     * */
    FastShakeAdapter adapter;

    /**点击发表抖音*/
    ImageView addPublishView;

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fast_shake_main);
        AllOnceInit.init(this);

        addPublishView=findViewById(R.id.add_publish);
        recyclerView = findViewById(R.id.pager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setSinglePageFling(true);
        adapter = new FastShakeAdapter(this);
        recyclerView.setAdapter(adapter);

        addPublishView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ServiceDisposeFactory.getInstance().getServiceDispose().getFastShakeList()
                .subscribe(new Action1<FastShakeVo[]>() {
                    @Override
                    public void call(FastShakeVo[] data) {
                        adapter.setDatas(new ArrayList<>(Arrays.asList(data)));
                    }
                });

        Toast.makeText(this,"向下滑动切换短视频",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.setDatas(new ArrayList<>());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log.debug("onDestroy");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_publish:
                Toast.makeText(this,"只有到期时间大于3个月的会员才能使用该功能",Toast.LENGTH_LONG).show();
                break;
        }
    }

}
