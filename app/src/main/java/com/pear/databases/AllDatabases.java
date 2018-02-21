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
                                                setTitle("小说");
                                                setStyle(StyleFragmentFactory.TEXT_NEWS_STYLE);
                                                setDataType("raw");
                                                setData(JsonUtil.fromObjectToJson(CommunityDatabases.getAllData()));
                                            }}
                                    };
                            setData(JsonUtil.fromObjectToJson(data));

                        }}
                };

        String jsonData=JsonUtil.fromObjectToJson(data);
        System.out.println(jsonData);
        return jsonData;
    }

}
