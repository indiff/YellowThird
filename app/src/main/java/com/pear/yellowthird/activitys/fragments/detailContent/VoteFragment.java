package com.pear.yellowthird.activitys.fragments.detailContent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pear.android.utils.DensityUtils;
import com.pear.android.view.PColumn;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;
import com.pear.yellowthird.vo.databases.UserVo;
import com.pear.yellowthird.vo.databases.VoteVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 投票界面
 */
public class VoteFragment extends Fragment {

    /**
     * 内容数据
     */
    private List<VoteVo> datas=new ArrayList<>();

    private ScrollView mContentView;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     */
    public static VoteFragment newInstance() {
        VoteFragment fragment = new VoteFragment();
        return fragment;
    }

    public List<VoteVo> getDatas() {
        return datas;
    }

    public void setDatas(List<VoteVo> datas) {
        this.datas = datas;
        refreshViewByDataChange();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("VoteFragment onCreateView");
        if (null != mContentView) {
            System.out.println("VoteFragment onCreateView return cache view ");
            return mContentView;
        }

        /**万一投票很多，肯定需要滑动的*/
        mContentView = new ScrollView(getActivity());
        mContentView.setLayoutParams(
                new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));

        refreshViewByDataChange();
        return mContentView;
    }

    /**
     * 数据变动了，重新绘制视图
     * */
    private void refreshViewByDataChange()
    {
        mContentView.removeAllViews();
        LinearLayout rootView = new LinearLayout(getActivity());
        rootView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        rootView.setOrientation(LinearLayout.VERTICAL);
        addAllVoteView(rootView);
        mContentView.addView(rootView);
    }

    /**
     * 添加所有集合的投票视图
     */
    void addAllVoteView(LinearLayout rootView) {
        for (int i = 0; i < datas.size(); i++) {

            VoteVo voteVo = datas.get(i);
            LinearLayout voteView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.sub_vote_view, null);

            /**这里需要动态设置 米格投票的类别之间的间隔*/
            {
                voteView.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT) {{
                            setMargins(0, 0, 0,
                                    (int) getActivity().getResources().getDimension(R.dimen.vote_echo_margin_bottom));
                        }});
                ViewGroup.LayoutParams layoutParams = voteView.getLayoutParams();
            }

            /**投票结果的时间点*/
            {
                TextView timeView = voteView.findViewById(R.id.time);
                timeView.setText(voteVo.getCurrentResultTime()+" 投票情况");
            }

            /**标题*/
            {
                TextView titleView = voteView.findViewById(R.id.title);
                titleView.setText(voteVo.getTitle());
            }

            /**子投票选项界面*/
            {
                LinearLayout voteListView = voteView.findViewById(R.id.vote_list);

                List<VoteVo.SubSelect> allSubSelects = voteVo.getVoteSelect();
                addSubSelectVoteViewBySingleVote(voteListView, voteVo, allSubSelects);
            }

            rootView.addView(voteView);
        }
    }


    /**
     * 添加单个投票视图。
     */
    void addSubSelectVoteViewBySingleVote(LinearLayout rootLayout, VoteVo voteVo, List<VoteVo.SubSelect> allSubSelects) {
        /**找到最大的投票制，顺便上浮一点点。*/

        /**黄金比例 3 7*/
        double maxRatio=1.3;
        int currentMaxVote =(int)( Collections.max(allSubSelects).getCount() * maxRatio);
        for (int subSelectIndex = 0; subSelectIndex < allSubSelects.size(); subSelectIndex++) {
            VoteVo.SubSelect data = allSubSelects.get(subSelectIndex);

            LinearLayout lineView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.sub_vote_list_line, null);

            /**引入局部刷新，体验会好一点。不然屏幕闪络实在是太恶心了*/
            SubSelectVoteRefresh  subSelectVoteRefresh=new SubSelectVoteRefresh();
            subSelectVoteRefresh.currentMaxVote=currentMaxVote;
            subSelectVoteRefresh.data=data;

            /**下标*/
            {
                TextView indexView = lineView.findViewById(R.id.index);
                indexView.setText(String.valueOf(subSelectIndex + 1/**程序从0 开始，产品从1开始*/));
            }

            /**内容略缩图*/
            {
                ImageView contentIconView = lineView.findViewById(R.id.content_icon);
                Glide.with(getActivity())
                        .load(data.getImageUri())
                        /**圆形*/
                        .apply(bitmapTransform(new CropCircleTransformation()))
                        .into(contentIconView);
            }

            /**标题*/
            {
                TextView titleView = lineView.findViewById(R.id.title);
                titleView.setText(data.getTitle());
            }

            /**简介*/
            {
                TextView introduceView = lineView.findViewById(R.id.introduce);
                introduceView.setText(data.getKeyWord());
            }

            /**图形的投票百分比率*/
            {
                ImageView imageView = lineView.findViewById(R.id.percentage_chart);
                int level=(int)(( data.getCount()/(double)currentMaxVote) *10000);
                imageView.getDrawable().setLevel(level);

                subSelectVoteRefresh.percentageChartView=imageView;
            }

            /**文字表达的投票数量*/
            {
                TextView textView = lineView.findViewById(R.id.percentage_text);
                textView.setText(String.valueOf(data.getCount()));
                subSelectVoteRefresh.percentageTextView=textView;
            }

            /**点击投票*/
            {
                ImageView imageView = lineView.findViewById(R.id.vote_click);
                if(data.isAlreadyVote())
                    imageView.setSelected(true);
                onOnVoteClick(imageView,voteVo,subSelectVoteRefresh);
                subSelectVoteRefresh.clickView=imageView;
            }

            rootLayout.addView(lineView);
        }
    }

    /**
     * 监听投票事件
     * @param view 待监听投票的按钮
     * @param vote 投票值
     * */
    void onOnVoteClick(final ImageView view,final VoteVo vote,final SubSelectVoteRefresh subSelectVoteRefresh)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vote.isAlreadyVote())
                {
                    ServiceDisposeFactory.getInstance().getServiceDispose()
                            .clickVote(vote.getId(),subSelectVoteRefresh.data.getId())
                            .subscribe(
                                    /**投票正常*/
                                    new Action1<Boolean>() {
                        @Override
                        public void call(Boolean result) {
                            subSelectVoteRefresh.data.setAlreadyVote(true);
                            subSelectVoteRefresh.data.setCount(subSelectVoteRefresh.data.getCount()+1);
                            subSelectVoteRefresh.refreshView();
                            vote.setAlreadyVote(true);
                            Toast.makeText(getActivity(),"感谢亲的参与",Toast.LENGTH_SHORT).show();
                        }

                        /**投票出错*/
                    });
                }
                else
                {
                    Toast.makeText(getActivity(),"你已投过票咯，亲",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    /**子类投票局部刷新视图*/
    class SubSelectVoteRefresh
    {

        /**带点击图片视图*/
        public ImageView clickView;

        /**文字表达的投票数量*/
        TextView percentageTextView;

        /**图形的投票百分比率*/
        ImageView percentageChartView;

        /**子选项的数据*/
        VoteVo.SubSelect data;

        /**最大的投票量*/
        int currentMaxVote;


        /**重新刷新数据*/
        void refreshView()
        {
            if(data.isAlreadyVote())
                clickView.setSelected(true);
            percentageTextView.setText(String.valueOf(data.getCount()));

            int level=(int)(( data.getCount()/(double)currentMaxVote) *10000);
            percentageChartView.getDrawable().setLevel(level);
        }

    }

}
