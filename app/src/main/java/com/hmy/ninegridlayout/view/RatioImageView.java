package com.hmy.ninegridlayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.pear.yellowthird.activitys.R;


/**
 * 根据宽高比例自动计算高度ImageView
 * Created by HMY on 2016/4/21.
 */
public class RatioImageView extends ImageView {

    /**
     * 宽高比例
     */
    private float mRatio = 0f;

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);

        mRatio = typedArray.getFloat(R.styleable.RatioImageView_ratio, 0f);
        typedArray.recycle();
    }

    public RatioImageView(Context context) {
        super(context);
    }

    /**
     * 设置ImageView的宽高比
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mRatio != 0) {
            float height = width / mRatio;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    drawable.mutate().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp = getDrawable();
                if (drawableUp != null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
        }

        return super.onTouchEvent(event);
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
