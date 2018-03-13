package com.pear.yellowthird.activitys.operations;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 双击后退2次，退出程序
 */

public class DoubleBackClickOperation {

    /**
     * 主程序实例
     * */
    private Activity activity;

    /**
     * 上次的退出时间
     * */
    private long exitTime = 0;

    public DoubleBackClickOperation(Activity activity)
    {
        this.activity=activity;
    }

    /**
     * 检测回退建按下时间
     * @return true 拦截中了，false忽略
     * */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return false;
    }

    /**
     * 计算退出机制
     * */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(activity, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            activity.finish();
            System.exit(0);
        }
    }

}
