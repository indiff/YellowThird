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
                    add(new VoteVo.SubSelect("美女",100));
                    add(new VoteVo.SubSelect("野兽",300));
                    add(new VoteVo.SubSelect("搞笑",0));
                    add(new VoteVo.SubSelect("动漫",30));
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
