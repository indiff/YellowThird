package com.pear.yellowthird.activitys.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.pear.yellowthird.activitys.R;

/**
 * 通用的头部状态栏
 */

public class CommonHeadActivity extends AppCompatActivity {


    /**
     * 初始化头部状态栏
     * @param title 标题
     */
    protected void initHeadBar(String title) {

        /**监听返回按钮*/
        {
            View backView = findViewById(R.id.head_back);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        /**标题*/
        {
            TextView titleView = findViewById(R.id.head_title);
            titleView.setText(title);
        }
    }


    /**
     * 初始化头部状态栏
     * @param title 标题
     * @param rightTitle 右侧的标题
     * @param rightClick 右侧的标题
     */
    protected void initHeadBar(String title,String rightTitle,View.OnClickListener rightClick) {
        initHeadBar(title);

        /**右侧的标题*/
        {
            TextView rightTitleView = findViewById(R.id.head_right);
            rightTitleView.setText(rightTitle);
            rightTitleView.setOnClickListener(rightClick);
        }

    }

}