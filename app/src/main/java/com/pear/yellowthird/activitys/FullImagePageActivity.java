package com.pear.yellowthird.activitys;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.pear.yellowthird.adapter.FullImageAdapter;
import com.pear.android.listener.empty.EmptyOnPageChangeListener;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

/**
 * 全屏可滑动显示图片
 */

public class FullImagePageActivity extends AppCompatActivity {


    FullImageAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

    /**图片的数据*/
    ImageIntroduceVo mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = (ImageIntroduceVo)getIntent().getSerializableExtra("image");

        setContentView(R.layout.activity_full_image_page);

        //mAdapter = new FullImageAdapter(getSupportFragmentManager(),mData.getImages());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        ImageButton closeButton=findViewById(R.id.close);
        onCloseListener(closeButton);
    }

    /**
     * 监听关闭按钮
     * 点击进入全屏浏览模式
     */
    void onCloseListener(final View button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPager.setOnPageChangeListener(new EmptyOnPageChangeListener(){

            /**总的page数量*/
            int pageCount=mData.getImages().size();

            public void onPageSelected(int position) {
                if((position+1)==pageCount)
                    button.setVisibility(View.VISIBLE);
                else
                    button.setVisibility(View.GONE);
            }
        });

    }


}