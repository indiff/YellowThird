package com.pear.android.view;

/**
 * Created by su on 2017/11/8.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 在ScrollView中嵌套使用ListView，ListView只会显示一行到两行的数据。
 * 起初我以为是样式的问题，一直在对XML文件的样式进行尝试性设置，但始终得不到想要的效果。
 * 后来在网上查了查，ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起了冲突，一般不建议两者套用。
    下面说说具体解决方案。方案的主要思路就是根据ListView子项重置其高度。
    首先，ListView不能直接用，要自定义一个，然后重写onMeasure（）方法：

 详情查看
 * */
public class ScrollListView extends ListView {

    boolean expanded = true;

    public ScrollListView(Context context) {
        super(context);
    }


    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListView(Context context, AttributeSet attrs,
                          int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            android.view.ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}