package com.pear.databases;

import com.pear.yellowthird.vo.databases.VoteVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟投票数据
 */

public class VoteDatabases {

    static List<VoteVo> gData
            = new ArrayList<VoteVo>() {{
        add(new VoteVo(
                1,
                "明天的电影主题",
                new ArrayList<VoteVo.SubSelect>(){{
                    add(new VoteVo.SubSelect(
                            "美女",
                            "清纯",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1514602780&di=f8600b268ee6afce8b0ce1c04cbbad52&src=http://img01.taopic.com/160929/318761-16092Z9225648.jpg",
                            100,
                            true
                            ));
                    /*
                    add(new VoteVo.SubSelect("美女",100));
                    add(new VoteVo.SubSelect("野兽",300));
                    add(new VoteVo.SubSelect("搞笑",0));
                    add(new VoteVo.SubSelect("动漫",30));*/
                }},
                true,
                "欢迎你的投票哦"
                )
        );

        add(new VoteVo(
                        1,
                        "明天的图片类别",
                        new ArrayList<VoteVo.SubSelect>(){{
                            add(new VoteVo.SubSelect("美景",200));
                            add(new VoteVo.SubSelect("动物",100));
                            add(new VoteVo.SubSelect("意境",30));
                        }},
                        false,
                        "你已经投过票了"
                )
        );

    }};

    public static List<VoteVo> getData() {
        return gData;
    }
}
