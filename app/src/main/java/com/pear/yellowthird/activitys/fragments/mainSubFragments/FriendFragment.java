package com.pear.yellowthird.activitys.fragments.mainSubFragments;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.pear.android.view.LinearLayoutLikeListView;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.FriendSimpleCommentAdapter;
import com.pear.yellowthird.adapter.FriendsAdapter;
import com.pear.yellowthird.vo.databases.FriendsVo;

import java.util.List;


/**
 * 朋友圈界面
 */
public class FriendFragment extends Fragment {

    List<FriendsVo> datas;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     */
    public static Fragment newInstance(List<FriendsVo> datas) {
        FriendFragment fragment = new FriendFragment();
        fragment.datas=datas;
        return fragment;
    }

    private View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mRootView)
            return mRootView;

        mRootView=inflater.inflate(R.layout.sub_friends,null);

        SuperRecyclerView recyclerView=mRootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        FriendsAdapter adapter= new FriendsAdapter(getContext());
        adapter.setDatas(datas);

        recyclerView.setAdapter(adapter);

        return mRootView;
    }

}