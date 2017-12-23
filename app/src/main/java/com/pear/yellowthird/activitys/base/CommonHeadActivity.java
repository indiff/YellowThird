package com.pear.yellowthird.activitys.base;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.ogapants.playercontrolview.PlayerControlView;
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
            onCloseListener(backView);
        }

        /**标题*/
        {
            TextView titleView = findViewById(R.id.head_title);
            titleView.setText(title);
        }

    }

    /**
     * 监听关闭按钮
     */
    void onCloseListener(final View button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}