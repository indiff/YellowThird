package com.pear.databases;

import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.style.factory.StyleFragmentFactory;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.style.vo.SubTabMenuStyleDataVo;
import com.pear.yellowthird.vo.databases.FriendsVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;

import java.util.ArrayList;

/**
 * Created by su on 2017/11/15.
 */

public class AllDatabases {


    public static String getData() {
        BottomNavigationMenuVo[] data = new BottomNavigationMenuVo[]
                {
                        /**公告*/
                        /**社区中的大类*/
                        new BottomNavigationMenuVo() {{
                            setTitle("社区");
                            setStyle(StyleFragmentFactory.SUB_TAB_MENU_STYLE);
                            setIcon("community");
                            setDataType("raw");

                            SubTabMenuStyleDataVo[] data = new SubTabMenuStyleDataVo[]
                                    {
                                            new SubTabMenuStyleDataVo() {{
                                                setTitle("色友圈");
                                                setStyle(StyleFragmentFactory.FRIEND_STYLE);
                                                setDataType("raw");
                                                setData(JsonUtil.fromObjectToJson(
                                                        new ArrayList<FriendsVo>() {{
                                                            add(new FriendsVo(
                                                                    new UserVo("呵呵","https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514602780&di=f8600b268ee6afce8b0ce1c04cbbad52&src=http://img01.taopic.com/160929/318761-16092Z9225648.jpg",0),
                                                                    "你猜猜我是谁",
                                                                    "7分钟前",
                                                                    68,
                                                                    true,
                                                                    666,

                                                                    new ArrayList<String>() {{

                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=20b67c1c12720a71d8ea59c67b5317f4&src=http://pic6.nipic.com/20100405/3017209_061106405171_2.jpg");

                                                            }},
                                                                    new ArrayList<TalkComment>() {{
                                                                        add(new TalkComment("这么好看的主角", 121, 0){{setUser(new UserVo("呵呵"));}});
                                                                        add(new TalkComment("这么好看的主角", 121, 0){{setUser(new UserVo("呵呵"));}});
                                                                        add(new TalkComment("这么好看的主角", 121, 0){{setUser(new UserVo("呵呵"));}});
                                                                        add(new TalkComment("这么好看的主角", 121, 0){{setUser(new UserVo("呵呵"));}});
                                                                    }}
                                                            ));

                                                            add(new FriendsVo(
                                                                    new UserVo("呵呵","https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514602780&di=f8600b268ee6afce8b0ce1c04cbbad52&src=http://img01.taopic.com/160929/318761-16092Z9225648.jpg",0),
                                                                    "",
                                                                    "7分钟前",
                                                                    68,
                                                                    true,
                                                                    666,

                                                                    new ArrayList<String>() {{

                                                                        add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1516270015256&di=2147ddcae9dfb0669105ceb78b1f9a75&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201204%2F20120412123915795.jpg");

                                                                    }},
                                                                    new ArrayList<TalkComment>() {{
                                                                    }}
                                                            ));
                                                            add(new FriendsVo(
                                                                    new UserVo("呵呵","https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514602780&di=f8600b268ee6afce8b0ce1c04cbbad52&src=http://img01.taopic.com/160929/318761-16092Z9225648.jpg",0),
                                                                    "",
                                                                    "7分钟前",
                                                                    68,
                                                                    true,
                                                                    666,

                                                                    new ArrayList<String>() {{

                                                                        add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=20b67c1c12720a71d8ea59c67b5317f4&src=http://pic6.nipic.com/20100405/3017209_061106405171_2.jpg");
                                                                        add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=467184516,4129069465&fm=27&gp=0.jpg");
                                                                        add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2634944918,1248270971&fm=27&gp=0.jpg");


                                                                    }},
                                                                    new ArrayList<TalkComment>() {{
                                                                        add(new TalkComment("这么好看的主角", 121, 0));
                                                                    }}
                                                            ));
                                                            add(new FriendsVo(new ArrayList<String>()
                                                            {{
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512836446174&di=5ddbe9797e4fc479b9c61ad1abf1cb0e&imgtype=0&src=http%3A%2F%2Fdynamic-image.yesky.com%2F740x-%2FuploadImages%2F2014%2F064%2F914577T2G105.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512836447470&di=6c94c1854c46ec1872564fb04465ec15&imgtype=0&src=http%3A%2F%2Fpic48.nipic.com%2Ffile%2F20140912%2F7487939_223919315000_2.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827284&di=699795e647ca7fb639b3b8d24e0035e7&src=http://image.tianjimedia.com/uploadImages/2014/218/37/E2W54W0QWP54.jpg");

                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=f57621951d968c12018697d96fcab326&src=http://img0.ph.126.net/zN7kMau3wtHszgGXZN6AoA==/845550830138875689.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=7a452b455b5f3006e868e79eb6a8e64f&src=http://www.bz55.com/uploads/allimg/150330/140-1503301A401.jpg");

                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=0f21b036f4f03891648c0cd1a9970a75&src=http://pic31.nipic.com/20130704/7447430_092141818000_2.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=20b67c1c12720a71d8ea59c67b5317f4&src=http://pic6.nipic.com/20100405/3017209_061106405171_2.jpg");
                                                                add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=467184516,4129069465&fm=27&gp=0.jpg");
                                                                add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2634944918,1248270971&fm=27&gp=0.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=33c7eb887f04a0f8e4905c409649da99&src=http://img0.ph.126.net/IH5ZHTDMTP---vQVlNIXQg==/6608505787097222175.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=e014aad547277c1c1880253d89793d1c&src=http://pic20.nipic.com/20120412/8599156_141607111000_2.jpg");
                                                                add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4076246999,2281468155&fm=27&gp=0.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=cf70b5ac87c9fbcf70e9f779cd540eb4&src=http://pic34.nipic.com/20131020/7447430_080654292000_2.jpg");
                                                                add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2507154938,2299824569&fm=27&gp=0.jpg");

                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512837330304&di=684425c22a2e4e29b0bc73cbacd400b3&imgtype=0&src=http%3A%2F%2Fc11.eoemarket.com%2Fupload%2F2012%2F0414%2Fapps%2F87835%2Fscreenshots%2F30745.png");


                                                            }}));

                                                            add(new FriendsVo(new ArrayList<String>() {{
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512836446174&di=5ddbe9797e4fc479b9c61ad1abf1cb0e&imgtype=0&src=http%3A%2F%2Fdynamic-image.yesky.com%2F740x-%2FuploadImages%2F2014%2F064%2F914577T2G105.jpg");


                                                            }},
                                                                    new ArrayList<TalkComment>() {{
                                                                        add(new TalkComment("女神女神，这么好看的主角1", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角2", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角3", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角4", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角5", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角6", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角7", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角8", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角9", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角0", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角1", 121, 0));
                                                                        add(new TalkComment("女神女神，这么好看的主角2", 121, 0));
                                                                    }}));


                                                            add(new FriendsVo(new ArrayList<String>() {{
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512836447470&di=6c94c1854c46ec1872564fb04465ec15&imgtype=0&src=http%3A%2F%2Fpic48.nipic.com%2Ffile%2F20140912%2F7487939_223919315000_2.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827284&di=699795e647ca7fb639b3b8d24e0035e7&src=http://image.tianjimedia.com/uploadImages/2014/218/37/E2W54W0QWP54.jpg");
                                                            }},
                                                            new ArrayList<TalkComment>() {{
                                                                add(new TalkComment("这么好看的主角", 121, 0));
                                                                add(new TalkComment("女神没有那么好看", 411, 0));
                                                            }}));

                                                            add(new FriendsVo(new ArrayList<String>() {{

                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=20b67c1c12720a71d8ea59c67b5317f4&src=http://pic6.nipic.com/20100405/3017209_061106405171_2.jpg");
                                                                add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=467184516,4129069465&fm=27&gp=0.jpg");
                                                                add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2634944918,1248270971&fm=27&gp=0.jpg");


                                                                }},
                                                                new ArrayList<TalkComment>() {{
                                                                    add(new TalkComment("这么好看的主角", 121, 0));
                                                                }}
                                                            ));


                                                            add(new FriendsVo(new ArrayList<String>() {{

                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=33c7eb887f04a0f8e4905c409649da99&src=http://img0.ph.126.net/IH5ZHTDMTP---vQVlNIXQg==/6608505787097222175.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=e014aad547277c1c1880253d89793d1c&src=http://pic20.nipic.com/20120412/8599156_141607111000_2.jpg");
                                                                add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4076246999,2281468155&fm=27&gp=0.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=cf70b5ac87c9fbcf70e9f779cd540eb4&src=http://pic34.nipic.com/20131020/7447430_080654292000_2.jpg");
                                                                add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2507154938,2299824569&fm=27&gp=0.jpg");

                                                            }}));

                                                            add(new FriendsVo(new ArrayList<String>() {{

                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=f57621951d968c12018697d96fcab326&src=http://img0.ph.126.net/zN7kMau3wtHszgGXZN6AoA==/845550830138875689.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=7a452b455b5f3006e868e79eb6a8e64f&src=http://www.bz55.com/uploads/allimg/150330/140-1503301A401.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=0f21b036f4f03891648c0cd1a9970a75&src=http://pic31.nipic.com/20130704/7447430_092141818000_2.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=20b67c1c12720a71d8ea59c67b5317f4&src=http://pic6.nipic.com/20100405/3017209_061106405171_2.jpg");
                                                                add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=467184516,4129069465&fm=27&gp=0.jpg");
                                                                add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2634944918,1248270971&fm=27&gp=0.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=33c7eb887f04a0f8e4905c409649da99&src=http://img0.ph.126.net/IH5ZHTDMTP---vQVlNIXQg==/6608505787097222175.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=e014aad547277c1c1880253d89793d1c&src=http://pic20.nipic.com/20120412/8599156_141607111000_2.jpg");
                                                                add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4076246999,2281468155&fm=27&gp=0.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1512827253&di=cf70b5ac87c9fbcf70e9f779cd540eb4&src=http://pic34.nipic.com/20131020/7447430_080654292000_2.jpg");
                                                                add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2507154938,2299824569&fm=27&gp=0.jpg");
                                                                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512837330304&di=684425c22a2e4e29b0bc73cbacd400b3&imgtype=0&src=http%3A%2F%2Fc11.eoemarket.com%2Fupload%2F2012%2F0414%2Fapps%2F87835%2Fscreenshots%2F30745.png");
                                                            }}));
                                                        }}
                                                ));
                                            }} ,
                                            new SubTabMenuStyleDataVo() {{
                                                setTitle("投票");
                                                setStyle(StyleFragmentFactory.VOTE_STYLE);
                                                setDataType("raw");
                                                setData(JsonUtil.fromObjectToJson(VoteDatabases.getData()));
                                            }},
                                            new SubTabMenuStyleDataVo() {{
                                                setTitle("简介");
                                                setStyle(StyleFragmentFactory.TEXT_NEWS_STYLE);
                                                setDataType("raw");
                                                setData(JsonUtil.fromObjectToJson(CommunityDatabases.getIntroductionData()));
                                            }},
                                            new SubTabMenuStyleDataVo() {{
                                                setTitle("小说");
                                                setStyle(StyleFragmentFactory.TEXT_NEWS_STYLE);
                                                setDataType("raw");
                                                setData(JsonUtil.fromObjectToJson(CommunityDatabases.getIntroductionData()));
                                            }},
                                            new SubTabMenuStyleDataVo() {{
                                                setTitle("什么鬼");
                                                setStyle(StyleFragmentFactory.TEXT_NEWS_STYLE);
                                                setDataType("raw");
                                                setData(JsonUtil.fromObjectToJson(CommunityDatabases.getIntroductionData()));
                                            }}

                                    };
                            setData(JsonUtil.fromObjectToJson(data));

                        }},
                        //账户中的大类
                        new BottomNavigationMenuVo() {{
                            setTitle("账户");
                            setStyle(StyleFragmentFactory.ACCOUNT_INFO_STYLE);
                            setIcon("account");
                            setDataType("raw");
                            setData(JsonUtil.fromObjectToJson(new UserVo("小鸡鸡", "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514602780&di=03e30ce632c3bd0029518388bd2a9356&src=http://dynamic-image.yesky.com/740x-/uploadImages/2013/312/LINK05KBD8T9.jpg", 210)));
                        }},
                        //图片中的大类
                        new BottomNavigationMenuVo() {{
                            setTitle("图片");
                            setStyle(StyleFragmentFactory.SUB_TAB_MENU_STYLE);
                            setIcon("image");
                            setDataType("raw");

                            setData(
                                    JsonUtil.fromObjectToJson(
                                            new SubTabMenuStyleDataVo[]
                                                    {
                                                            //
                                                            new SubTabMenuStyleDataVo() {{
                                                                setTitle("写真");
                                                                setStyle(StyleFragmentFactory.IMAGE_INTRODUCE_STYLE);
                                                                setDataType("raw");
                                                                setData(JsonUtil.fromObjectToJson(ImageDatabases.getFunny()));
                                                            }}
                                                            ,
                                                            //
                                                            new SubTabMenuStyleDataVo() {{
                                                                setTitle("动物");
                                                                setStyle(StyleFragmentFactory.IMAGE_INTRODUCE_STYLE);
                                                                setDataType("raw");
                                                                setData(JsonUtil.fromObjectToJson(ImageDatabases.getZoology()));
                                                            }}

                                                    }));
                        }},
                        //电影大类
                        new BottomNavigationMenuVo() {{
                            setTitle("电影");
                            setStyle(StyleFragmentFactory.SUB_TAB_MENU_STYLE);
                            setIcon("video");
                            setDataType("raw");

                            //电影中的推荐类别
                            SubTabMenuStyleDataVo data = new SubTabMenuStyleDataVo() {{
                                setTitle("推荐");
                                setStyle(StyleFragmentFactory.VIDEO_INTRODUCE_STYLE);
                                setDataType("raw");
                                setData(JsonUtil.fromObjectToJson(VideoDatabases.getBelle()));
                            }};

                            setData(
                                    JsonUtil.fromObjectToJson(
                                            new SubTabMenuStyleDataVo[]
                                                    {
                                                            data,
                                                            new SubTabMenuStyleDataVo() {{
                                                                setTitle("分享");
                                                                setStyle(StyleFragmentFactory.TEXT_NEWS_STYLE);
                                                                setDataType("raw");
                                                                setData(JsonUtil.fromObjectToJson(CommunityDatabases.getShareData()));
                                                            }}
                                                    }));
                        }} ,

                        new BottomNavigationMenuVo() {{
                            setTitle("百度");
                            setStyle(StyleFragmentFactory.WEB_STYLE);
                            setIcon("111");
                            setDataType("raw");
                            setData("https://www.baidu.com/");
                        }},
                        new BottomNavigationMenuVo() {{
                            setTitle("简介");
                            setStyle(StyleFragmentFactory.TEXT_NEWS_STYLE);
                            setIcon("111");
                            setDataType("raw");
                            setData(JsonUtil.fromObjectToJson(CommunityDatabases.getIntroductionData()));
                        }}
                };

        String jsonData=JsonUtil.fromObjectToJson(data);
        System.out.println(jsonData);
        return jsonData;
    }

}
