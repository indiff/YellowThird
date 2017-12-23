package com.pear.yellowthird.style.factory;

import android.support.v4.app.Fragment;

import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.activitys.fragments.detailContent.CommonContentFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.ImageIntroduceFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.NewsFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VideoIntroducePageFragment;
import com.pear.yellowthird.activitys.fragments.detailContent.VoteFragment;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.AccountInfoFragment;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.FriendFragment;
import com.pear.yellowthird.activitys.fragments.mainSubFragments.commonAbstract.CommonSubTabSubFragmentAbstract;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.style.vo.StyleType;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;
import com.pear.yellowthird.vo.databases.NewsVo;
import com.pear.yellowthird.vo.databases.FriendsVo;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.UserVo;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;
import com.pear.yellowthird.vo.databases.VoteVo;

import java.util.Arrays;

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


    public static Fragment create(StyleType styleType) {
        String style = styleType.getStyle();
        String data = styleType.getData();

        switch (style) {
            case SUB_TAB_MENU_STYLE: {
                final StyleSubTabSubMenuFragment fragment=StyleSubTabSubMenuFragment.newInstance(
                        StyleFragmentFactory.StyleSubTabSubMenuFragment.class);

                JsonUtil.write2ClassAsync(data, SubTabMenuStyleDataVo[].class)
                .subscribe(new Action1<SubTabMenuStyleDataVo[]>() {
                                    @Override
                                    public void call(SubTabMenuStyleDataVo[] subs) {
                                        fragment.setMenuData(Arrays.asList(subs));
                                    }
                                });
                return fragment;
            }
            case VIDEO_INTRODUCE_STYLE: {
                VideoIntroduceVo[] vo = JsonUtil.write2Class(data, VideoIntroduceVo[].class);
                return VideoIntroducePageFragment.newInstance(Arrays.asList(vo));
            }
            case IMAGE_INTRODUCE_STYLE: {
                final ImageIntroduceFragment fragment=ImageIntroduceFragment.newInstance();
                JsonUtil.write2ClassAsync(data, ImageIntroduceVo.class)
                        .subscribe(new Action1<ImageIntroduceVo>() {
                            @Override
                            public void call(ImageIntroduceVo data) {
                                fragment.setData(data);
                            }
                        });
                return fragment;
            }
            case TEXT_NEWS_STYLE: {
                NewsVo[] vo = JsonUtil.write2Class(data, NewsVo[].class);
                return NewsFragment.newInstance(data);
            }
            case VOTE_STYLE: {
                final VoteFragment fragment=VoteFragment.newInstance();
                JsonUtil.write2ClassAsync(data, VoteVo[].class)
                        .subscribe(new Action1<VoteVo[]>() {
                            @Override
                            public void call(VoteVo[] subs) {
                                fragment.setDatas(Arrays.asList(subs));
                            }
                        });
                return fragment;
            }
            case ACCOUNT_INFO_STYLE: {
                UserVo vo = JsonUtil.write2Class(data, UserVo.class);
                return AccountInfoFragment.newInstance(vo);
            }
            case FRIEND_STYLE: {
                FriendsVo[] datas= JsonUtil.write2Class(data, FriendsVo[].class);
                return FriendFragment.newInstance(Arrays.asList(datas));
            }
            default:
                return CommonContentFragment.newInstance(style, data);
        }
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

