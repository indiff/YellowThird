package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pear.android.view.LGNineGrideView;
import com.pear.yellowthird.activitys.FullImagePageActivity;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;

import rx.functions.Action1;


/**
 * 图片介绍视图
 */

public class ImageIntroduceFragment extends Fragment {

    /**
     * 图片数据
     */
    private ImageIntroduceVo data = new ImageIntroduceVo();

    /**
     * root 视图
     */
    private ScrollView mRootView;

    /**
     * 多图实例
     */
    LGNineGrideView multiImageView;

    /**
     * 监听点赞
     * */
    LinearLayout clickGoodLinear;

    /**点赞图标*/
    ImageView goodCountIconView;

    /**总赞量*/
    TextView goodCountView;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     */
    public static ImageIntroduceFragment newInstance() {
        ImageIntroduceFragment fragment = new ImageIntroduceFragment();
        return fragment;
    }

    public ImageIntroduceVo getData() {
        return data;
    }

    public void setData(ImageIntroduceVo data) {
        this.data = data;
        if(null!=data)
            refreshViewByDataData();
        //else
        //    showNoResourceTipView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mRootView) {
            //log.debug("onCreateView return cache view ");
            return mRootView;
        }
        mRootView = (ScrollView)LayoutInflater.from(getActivity()).inflate(R.layout.image_introduce_advanced, null);
        multiImageView = mRootView.findViewById(R.id.multi_image);
        refreshViewByDataData();
        return mRootView;
    }

    /**
     * 显示没有资源的提示界面
     * 没有起效果,因为获取失败，http get的时候就忽略了，根本来不到这里
     * */
    /*
    void showNoResourceTipView()
    {
        mRootView.removeAllViews();
        View noResourceTipView= LayoutInflater.from(getActivity()).inflate(R.layout.common_no_resource_tip, null);
        mRootView.addView(noResourceTipView);
    }
    */

    /**
     * 根据data来刷新界面
     */
    void refreshViewByDataData() {

        /**标题*/
        {
            TextView titleView = mRootView.findViewById(R.id.title);
            titleView.setText(data.getTitle());
            if (TextUtils.isEmpty(data.getTitle()))
                titleView.setVisibility(View.GONE);
            else
                titleView.setVisibility(View.VISIBLE);
        }

        /**发表时间*/
        {
            TextView publishTimeView = mRootView.findViewById(R.id.publish_time);
            publishTimeView.setText(data.getPublishTime());

            if (TextUtils.isEmpty(data.getPublishTime()))
                publishTimeView.setVisibility(View.GONE);
            else
                publishTimeView.setVisibility(View.VISIBLE);
        }

        /**内容介绍*/
        {
            TextView contentView = mRootView.findViewById(R.id.content);
            contentView.setText(data.getContent());
            if (TextUtils.isEmpty(data.getContent()))
                contentView.setVisibility(View.GONE);
            else
                contentView.setVisibility(View.VISIBLE);
        }

        /**很多的图片略缩图*/
        {
            multiImageView.setUrls(data.getImages());
            multiImageView.setOnItemClickListener(new LGNineGrideView.OnItemClickListener() {
                @Override
                public void onClickItem(int position, View view) {
                    //添加浏览次数
                    ServiceDisposeFactory.getInstance().getServiceDispose().addImageShowCount(data.getId());
                    FullImagePageActivity.ImageSize imageSize = new FullImagePageActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                    FullImagePageActivity.startImagePagerActivity(getActivity(), data.getImages(), position, imageSize);
                }
            });
        }

        /**浏览次数*/
        {
            TextView startView = mRootView.findViewById(R.id.browse_count);
            startView.setText("浏览" + String.valueOf(data.getBrowseCount()) + "次");
        }

        /**监听点赞*/
        {
            clickGoodLinear = mRootView.findViewById(R.id.click_good);
            onImageClickGood(clickGoodLinear);
            if(data.getAlreadyClickGood())
                setClickImageGoodIsSelect(data.getGoodCount());
        }

        /**点赞图标*/
        {
            goodCountIconView = mRootView.findViewById(R.id.good_count_icon);
        }

        /**总赞量*/
        {
            goodCountView = mRootView.findViewById(R.id.good_count);
            goodCountView.setText(String.valueOf(data.getGoodCount()));
        }
    }


    /**
     * 点赞
     * */
    void onImageClickGood(final View view)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(0>data.getId())
                    return;
                /**先改样式，后通知服务器接口即可。体验好*/
                setClickImageGoodIsSelect(data.getGoodCount()+1);
                ServiceDisposeFactory.getInstance().getServiceDispose()
                        .addImageClickGood(data.getId())
                        .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean result) {
                    }
                });
            }
        });
    }


    /**
     * 设置图片点赞为已选状态
     * */
    public void setClickImageGoodIsSelect(int goodCount)
    {
        goodCountIconView.setSelected(true);
        goodCountView.setText(String.valueOf(goodCount));
        goodCountView.setTextColor(getResources().getColor(R.color.colorSelect));
        clickGoodLinear.setOnClickListener(null);
    }


}
