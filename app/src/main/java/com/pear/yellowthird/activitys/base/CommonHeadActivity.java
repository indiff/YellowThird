package com.pear.yellowthird.activitys.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.pear.yellowthird.activitys.R;

/**
 * 通用的头部状态栏
 */

public class CommonHeadActivity extends AppCompatActivity implements View.OnClickListener{


    /**返回*/
    protected View headBackView;

    /**标题*/
    protected TextView titleView;

    /**右侧标题*/
    protected TextView rightTitleView;

    /**
     * 初始化头部状态栏
     * @param title 标题
     */
    protected void initHeadBar(String title) {

        /**监听返回按钮*/
        {
            headBackView = findViewById(R.id.head_back);
            headBackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        /**标题*/
        {
            titleView = findViewById(R.id.head_title);
            titleView.setText(title);
        }
    }

    /**
     * 初始化头部状态栏
     * @param title 标题
     * @param rightTitle 右侧的标题
     */
    protected void initHeadBar(String title,String rightTitle) {
        initHeadBar(title);

        /**右侧的标题*/
        {
            rightTitleView = findViewById(R.id.head_right);
            rightTitleView.setText(rightTitle);
            rightTitleView.setOnClickListener(this);
            rightTitleView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
    }
}