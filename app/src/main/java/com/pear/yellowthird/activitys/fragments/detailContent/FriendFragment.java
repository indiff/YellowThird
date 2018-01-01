package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.adapter.FriendsAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.FriendsVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;


/**
 * 朋友圈界面
 */
public class FriendFragment extends Fragment {

    private View mRootView;

    FriendsAdapter adapter;

    /**
     * 上下拉刷新
     */
    SuperRecyclerView recyclerView;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     */
    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        return fragment;
    }

    public void setDatas(List<FriendsVo> datas) {
        adapter.setDatas(datas);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mRootView)
            return mRootView;

        mRootView = inflater.inflate(R.layout.sub_friends, null);

        recyclerView = mRootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        adapter = new FriendsAdapter(getContext());
        recyclerView.setAdapter(adapter);

        onListenerPullRefresh();
        onListenerMoreView();

        return mRootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        refreshPullData();
    }

    /**
     * 上拉刷新
     */
    void onListenerPullRefresh() {
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPullData();
            }
        });
    }

    /**
     * 刷新上拉数据
     * */
    void refreshPullData()
    {
        final List<FriendsVo> datas = adapter.getDatas();
        if (null != datas && !datas.isEmpty()) {
            FriendsVo firstData = datas.get(0);
            ServiceDisposeFactory.getInstance().getServiceDispose()
                    .refreshNewHeadFriendList(firstData.getId())
                    .subscribe(new Action1<FriendsVo[]>() {
                        @Override
                        public void call(FriendsVo[] list) {
                            recyclerView.setRefreshing(false);
                            if(null==list||list.length==0)
                                return;
                            List<FriendsVo> appendData = new ArrayList(Arrays.asList(list));
                            datas.addAll(0,appendData);
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    /**
     * 监听下拉刷新
     */
    void onListenerMoreView() {
        /**下拉刷新*/
        recyclerView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                refreshMoreData();
            }
        }, 1);
    }

    /**
     * 刷新更多数据
     * */
    void refreshMoreData()
    {
        final List<FriendsVo> datas = adapter.getDatas();
        if (null != datas && !datas.isEmpty()) {
            FriendsVo lastData = datas.get(datas.size() - 1);
            ServiceDisposeFactory.getInstance().getServiceDispose()
                    .queryMoreFriendList(lastData.getId())
                    .subscribe(new Action1<FriendsVo[]>() {
                        @Override
                        public void call(FriendsVo[] list) {
                            if (null == list || list.length == 0)
                                recyclerView.hideMoreProgress();
                            else {
                                        /*
                                        java.lang.UnsupportedOperationException
                                        http://blog.csdn.net/ztzry1234/article/details/51141341
                                        Arrays.asList()返回的List是Arrays工具类的内部类，是只读的，不能新增和删除。
                                        */
                                List<FriendsVo> appendData = new ArrayList(Arrays.asList(list));
                                datas.addAll(appendData);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }




}