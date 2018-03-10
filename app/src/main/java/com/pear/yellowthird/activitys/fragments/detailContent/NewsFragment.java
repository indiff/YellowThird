package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.NewsAdapter;
import com.pear.yellowthird.vo.databases.NewsVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 新闻样式
 */

public class NewsFragment extends Fragment {

    /**
     * 内容数据
     */
    private List<NewsVo> datas=new ArrayList<>();

    /**
     * root 视图
     */
    private View mRootView;

    /**新闻的数据的适配器*/
    NewsAdapter adapter;

    /**上下拉刷新*/
    SuperRecyclerView recyclerView;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     */
    public static NewsFragment newInstance(NewsVo[] data) {
        final NewsFragment fragment = new NewsFragment();
        fragment.datas=new ArrayList<>(Arrays.asList(data));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mRootView)
            return mRootView;

        mRootView = inflater.inflate(R.layout.sub_recycler, null);

        recyclerView = mRootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NewsAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setDatas(datas);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
