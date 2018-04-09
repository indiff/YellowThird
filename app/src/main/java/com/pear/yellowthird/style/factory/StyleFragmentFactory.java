package com.pear.yellowthird.style.factory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.fragments.detailContent.CommonContentFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.CommonPageFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.FastShakeFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.ImagePageFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.NewsFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VideoIntroduceFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VoteFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.WebFragment;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.AccountInfoFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.FriendFragment;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.commonAbstract.CommonSubTabSubFragmentAbstract;
import com.pear.yellowthird.adapter.CommonPageAdapter;
import com.pear.yellowthird.style.vo.StyleType;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;
import com.pear.yellowthird.vo.databases.NewsVo;
import com.pear.yellowthird.vo.databases.FriendsVo;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.UserVo;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;
import com.pear.yellowthird.vo.databases.VoteVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;


/**
 * 样式视图工厂
 */

public class StyleFragmentFactory {

    /**
     * 子菜单
     */
    public static final String SUB_TAB_MENU_STYLE = "sub_tab_menu";

    /**
     * 电影介绍
     */
    public static final String VIDEO_INTRODUCE_STYLE = "video_introduce";

    /**
     * 图片介绍
     */
    public static final String IMAGE_INTRODUCE_STYLE = "image_introduce";

    /**
     * 文字新闻
     */
    public static final String TEXT_NEWS_STYLE = "news";


    /**
     * 投票界面
     */
    public static final String VOTE_STYLE = "vote";

    /**
     * 账户界面
     */
    public static final String ACCOUNT_INFO_STYLE = "account_info";

    /**
     * 朋友圈界面
     */
    public static final String FRIEND_STYLE = "friend";

    /**
     * 网页的界面
     * */
    public static final String WEB_STYLE = "web";

    /**
     * 快手的界面
     * */
    public static final String FAST_SHAKE_STYLE = "fast_shake";

    public static Fragment create(StyleType styleType) {
        String style = styleType.getStyle();
        final String data = styleType.getData();

        switch (style) {
            case SUB_TAB_MENU_STYLE: {
                final StyleSubTabSubMenuFragment fragment=StyleSubTabSubMenuFragment.newInstance(
                        StyleFragmentFactory.StyleSubTabSubMenuFragment.class);

                JsonUtil.write2ClassAsync(data, SubTabMenuStyleDataVo[].class)
                .subscribe(new Action1<SubTabMenuStyleDataVo[]>() {
                                    @Override
                                    public void call(SubTabMenuStyleDataVo[] subs) {
                                        if(null==subs||subs.length==0)
                                        {
                                            fragment.setContentEmptyTip();
                                            return;
                                        }
                                        fragment.setMenuData(new ArrayList<>(Arrays.asList(subs)));
                                    }
                                });
                return fragment;
            }
            case VIDEO_INTRODUCE_STYLE: {
                VideoIntroduceVo[] vo = JsonUtil.write2Class(data, VideoIntroduceVo[].class);
                if(null==vo||vo.length==0)
                   return getEmptyDataTip(style);

                return CommonPageFragment.newInstance(new CommonPageFragment.BuildAdapterInteger<VideoIntroduceVo>() {
                    @Override
                    public CommonPageAdapter buildAdapter(FragmentManager manager) {
                        return new CommonPageAdapter(manager) {
                            @Override
                            public Fragment buildItem(int position) {
                                return VideoIntroduceFragment.newInstance((VideoIntroduceVo)mData.get(position));
                            }
                        };
                    }
                },new ArrayList<>(Arrays.asList(vo)));
            }
            case IMAGE_INTRODUCE_STYLE: {
                ImageIntroduceVo[] vo = JsonUtil.write2Class(data, ImageIntroduceVo[].class);
                if(null==vo||vo.length==0)
                    return getEmptyDataTip(style);
                return ImagePageFragment.newInstance(new ArrayList<>(Arrays.asList(vo)));
            }
            case TEXT_NEWS_STYLE: {
                NewsVo[][] vo = JsonUtil.write2Class(data, NewsVo[][].class);
                if(null==vo||vo.length==0)
                    return getEmptyDataTip(style);
                return CommonPageFragment.newInstance(new CommonPageFragment.BuildAdapterInteger<NewsVo[]>() {
                    @Override
                    public CommonPageAdapter buildAdapter(FragmentManager manager) {
                        return new CommonPageAdapter(manager) {
                            @Override
                            public Fragment buildItem(int position) {
                                return NewsFragment.newInstance((NewsVo[])mData.get(position));
                            }
                        };
                    }
                },new ArrayList<>(Arrays.asList(vo)));
            }
            case VOTE_STYLE: {
                final VoteFragment fragment=VoteFragment.newInstance();
                JsonUtil.write2ClassAsync(data, VoteVo[].class)
                        .subscribe(new Action1<VoteVo[]>() {
                            @Override
                            public void call(VoteVo[] subs) {
                                fragment.setDatas(new ArrayList<>(Arrays.asList(subs)));
                            }
                        });
                return fragment;
            }
            case ACCOUNT_INFO_STYLE: {
                UserVo vo = JsonUtil.write2Class(data, UserVo.class);
                return AccountInfoFragment.newInstance(vo);
            }
            case FRIEND_STYLE:
            {
                final FriendFragment fragment=FriendFragment.newInstance();
                JsonUtil.write2ClassAsync(data, FriendsVo[].class)
                        .subscribe(new Action1<FriendsVo[]>() {
                            @Override
                            public void call(FriendsVo[] datas) {
                                fragment.setDatas(new ArrayList<>(Arrays.asList(datas)));
                            }
                        });
                return fragment;
            }
            case WEB_STYLE:
            {
                WebFragment fragment= WebFragment.newInstance(data);
                return fragment;
            }
            case FAST_SHAKE_STYLE:
            {
                Fragment fragment= FastShakeFragment.newInstance();
                return fragment;
            }
            default:
                return CommonContentFragment.newInstance(style, data);
        }
    }

    /**
     * 空资源的友好提示
     * */
    private static Fragment getEmptyDataTip(String style)
    {
        return CommonContentFragment.newInstance("","资源正在上线中，\n等一下再来看看吧。");
    }

    /**
     * 第二级子菜单视图下的子视图创建
     */
    public static class StyleSubTabSubMenuFragment extends CommonSubTabSubFragmentAbstract {

        @Override
        public Fragment buildItem(int position, SubTabMenuStyleDataVo vo) {
            return StyleFragmentFactory.create(vo);
        }

    }


}

