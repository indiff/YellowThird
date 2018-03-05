package com.pear.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 *
 * @ClassName: ColorFilterImageView
 * @Description: 实现图像根据按下抬起动作变化颜色
 * @author hnclca
 * @date 2016-02-26
 *
 */
public class ColorFilterImageView extends ImageView implements OnTouchListener {
    public ColorFilterImageView(Context context) {
        this(context, null, 0);
    }

    public ColorFilterImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorFilterImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:  // 按下时图像变灰
                setColorFilter(Color.GRAY, Mode.MULTIPLY);
                break;
            case MotionEvent.ACTION_UP:   // 手指离开或取消操作时恢复原色
            case MotionEvent.ACTION_CANCEL:
                setColorFilter(Color.TRANSPARENT);
                break;
            default:
                break;
        }
        return false;
    }


    /**
     * 图片耗内存 oom问题。
     * http://blog.csdn.net/bobxie520/article/details/51167957
     * 这里不同于ViewPager+View方式的是，如果实际开发中在回收图片之前添加了将图片引用赋值为null后，
     * 出现图片不再加载的情况的话，就不能使用将图片引用赋值为null的方式了，
     * 但这样就会引起Canvas: trying to use a recycled bitmap异常，
     * 意思是正在使用一个已经回收过的bitmap，这样是不对的，所以需要在ImageView中的onDraw方法中捕获异常，
     * 因此，就需要自定义一个ImageView了，自定义ImageView代码很简单，如下代码只需try{}catch(){}即可。
     * */
    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            System.out.println("NotRecycledImageView.onDraw->exception=Canvas: trying to use a recycled bitmap");
        }
    }

}
