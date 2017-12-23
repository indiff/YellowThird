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
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pear.yellowthird.activitys.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class MainNavPageIndicator extends ScrollView implements PageIndicator {

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabLayout tabView = (TabLayout)view;
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

    public MainNavPageIndicator(Context context) {
        this(context, null);
    }

    public MainNavPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mTabListView=new TabListView();
        mTabLayout = new LinearLayout(getContext());
        mTabLayout.setOrientation(LinearLayout.VERTICAL);
        mTabLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorNavBackground));
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
                int iconResId = ((IconPagerAdapter)adapter).getIconResId(i);

                TypeLineView typeView=new TypeLineView();
                allTabView.addView(typeView.buildView(i,iconResId,title));

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

            /**图标*/
            ImageView mIconView;

            /**
             * 菜单
             * */
            TextView mTitleView;

            public TypeLineView() {
            }

            /**
             * 创建当前菜单项的视图
             * @param index 当前菜单项的下标位置
             * @param iconId 图标对应的资源Id
             * @param title 显示的标题名称
             * */
            public View buildView(int index,int iconId,String title)
            {
                TabLayout tabLinear=new TabLayout(getContext());
                tabLinear.setOrientation(LinearLayout.VERTICAL);
                tabLinear.setLayoutParams(
                        new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1));
                {
                    {
                        mIconView=new ImageView(getContext());
                        mIconView.setLayoutParams(
                                new LinearLayout.LayoutParams(95,95
                                ));
                        mIconView.setImageDrawable(getResources().getDrawable(iconId));
                        mIconView.setPadding(0,20,0,15);
                        tabLinear.addView(mIconView);
                    }

                    {
                        mTitleView=new TextView(getContext());
                        mTitleView.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        mTitleView.setText(title);
                        mTitleView.setTextSize(9);
                        mTitleView.setPadding(0,0,0,5);
                        tabLinear.addView(mTitleView);
                    }
                }

                tabLinear.setGravity(Gravity.CENTER);
                tabLinear.mIndex = index;
                tabLinear.setFocusable(true);
                tabLinear.setOnClickListener(mTabClickListener);
                return tabLinear;
            }

            /**
             * 刷新当前菜单项为选中状态
             * */
            public void setSelect(boolean select)
            {
                if(select)
                {
                    mIconView.setSelected(true);
                    mTitleView.setTextColor(getResources().getColor(R.color.colorSelect));
                }
                else
                {
                    mIconView.setSelected(false);
                    mTitleView.setTextColor(getResources().getColor(R.color.colorMinorTitle));
                }
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


        LinearLayout topLineView=new LinearLayout(getContext());
        topLineView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));
        topLineView.setBackgroundColor(getResources().getColor(R.color.colorMinorTitle));

        mTabLayout.addView(topLineView);
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

    protected class TabLayout extends LinearLayout {
        protected int mIndex;

        public TabLayout(Context context) {
            super(context);
        }

        public int getIndex() {
            return mIndex;
        }
    }

}
