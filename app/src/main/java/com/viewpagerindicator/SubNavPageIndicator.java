/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.pear.yellowthird.activitys.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class SubNavPageIndicator extends ScrollView implements PageIndicator {

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabTextView tabView = (TabTextView)view;
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
        }
    };

    private final LinearLayout mTabLayout;

    private ViewPager mViewPager;
    private OnPageChangeListener mListener;

    /**当前选中的下标*/
    private int mSelectedTabIndex;

    protected TabListView mTabListView;

    public SubNavPageIndicator(Context context) {
        this(context, null);
    }

    public SubNavPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mTabListView=new TabListView();
        mTabLayout = new LinearLayout(getContext());
        mTabLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorNavBackground));

        /**
         * 子菜单采用横向滑动
         * 没弄成功，后面再弄
         * */
        /*
        HorizontalScrollView scrollView=new HorizontalScrollView(getContext());
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setLayoutParams(
                new HorizontalScrollView.LayoutParams(
                        HorizontalScrollView.LayoutParams.MATCH_PARENT,
                        HorizontalScrollView.LayoutParams.MATCH_PARENT));
        scrollView.addView(mTabLayout);
        addView(scrollView);
        */
        addView(mTabLayout);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * tab 集合视图
     * */
    class TabListView
    {
        /**大类的菜单缓存集合*/
        List<TypeLineView> mView;

        /**
         * 根据菜单适配器数据 创建菜单的视图
         * */
        View build(PagerAdapter adapter)
        {
            LinearLayout allTabView=new LinearLayout(getContext());
            allTabView.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

            mView=new ArrayList<TypeLineView>();
            final int count = adapter.getCount();
            for (int i = 0; i < count; i++) {
                String title = adapter.getPageTitle(i).toString();

                TypeLineView typeView=new TypeLineView();
                allTabView.addView(typeView.buildView(i,title));

                mView.add(typeView);
            }
            return allTabView;
        }

        /**
         设置指定下标未选中状态
         */
        public void setCurrentViewStyle(int index)
        {
            if(mView.isEmpty())
                return;
            //清除上一个菜单的选中状态
            mView.get(mSelectedTabIndex).setSelect(false);

            mView.get(index).setSelect(true);
        }

        /**
         * 每一个子菜单的视图
         * */
        class TypeLineView{

            /**
             * 菜单
             * */
            TabTextView mTitleView;

            public TypeLineView() {
            }

            /**
             * 创建当前菜单项的视图
             * @param index 当前菜单项的下标位置
             * @param title 显示的标题名称
             * */
            public View buildView(int index,String title)
            {
                mTitleView=new TabTextView(getContext());
                mTitleView.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                mTitleView.setText(title);
                mTitleView.setTextSize(17);
                mTitleView.setPadding(15,13,12,17);

                mTitleView.mIndex = index;
                mTitleView.setFocusable(true);
                mTitleView.setOnClickListener(mTabClickListener);
                mTitleView.setTextColor(getResources().getColor(R.color.colorMainNavTitle));
                return mTitleView;
            }

            /**
             * 刷新当前菜单项为选中状态
             * */
            public void setSelect(boolean select)
            {
                if(select)
                    mTitleView.setTextColor(getResources().getColor(R.color.colorSelect));
                else
                    mTitleView.setTextColor(getResources().getColor(R.color.colorMainNavTitle));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();

        PagerAdapter adapter = mViewPager.getAdapter();
        View tabView=mTabListView.build(adapter);
        mTabLayout.addView(tabView);
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }

        mTabListView.setCurrentViewStyle(item);
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    protected class TabTextView extends android.support.v7.widget.AppCompatTextView {
        protected int mIndex;

        public TabTextView(Context context) {
            super(context);
        }

        public int getIndex() {
            return mIndex;
        }
    }

}
