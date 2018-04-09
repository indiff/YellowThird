package com.pear.databases;

import com.pear.yellowthird.vo.databases.FastShakeVo;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 快抖的模拟数据库
 */

public class FastShakeDatabases {

    public static List<FastShakeVo> getAll() {
        return new ArrayList<FastShakeVo>() {{
            add(new FastShakeVo(
                    "小嫩逼叶非非",
                    new UserVo("非非","http://t1.mmonly.cc/uploads/tu/201708/9999/230e7fd491.jpg",1),
                    365,
                    "https://km.97kuaimao.com/uploads/2018-02-19/7008592/6aecb0a2148272f5a26e20a6f2ba4abd_wm.mp4"
            ));
            add(new FastShakeVo(
                    "小嫩逼叶非非1",
                    new UserVo("非非1","http://t1.mmonly.cc/uploads/tu/201708/9999/230e7fd491.jpg",1),
                    3651,
                    "https://km.97kuaimao.com/uploads/2018-03-17/6994917/3f279bc816c286d8f161b1fbf5ea22e1_wm.mp4"
            ));
            add(new FastShakeVo(
                    "小嫩逼叶非非2",
                    new UserVo("非非2","http://t1.mmonly.cc/uploads/tu/201708/9999/230e7fd491.jpg",1),
                    3652,
                    "https://km.97kuaimao.com/uploads/2018-02-19/7131910/53450fa85c8fbdd16ce08ff4857124e7_wm.mp4"
            ));
            add(new FastShakeVo(
                    "小嫩逼叶非非3",
                    new UserVo("非非3","http://t1.mmonly.cc/uploads/tu/201708/9999/230e7fd491.jpg",1),
                    3653,
                    "https://km.97kuaimao.com/uploads/2018-04-04/7103211/280de9d43afeb7d909660768d899cbc5_wm.mp4"
            ));
        }};
    }

    public static List<TalkComment> getCommentAll() {
        return new ArrayList<TalkComment>() {{
            add(new TalkComment("以前一直在用BaseAdapter，对于其中的getview方法的重写一直不太清楚。今天终于得以有空来探究它的详细机制。", 4511, 0,
                    new UserVo("非非3","http://t1.mmonly.cc/uploads/tu/201708/9999/230e7fd491.jpg",1)));
            add(new TalkComment("这部电影的主角超级好看", 231, 0,
                    new UserVo("非非3","http://t1.mmonly.cc/uploads/tu/201708/9999/230e7fd491.jpg",1)));
            add(new TalkComment("主角很普通而已，没有那么好看", 11, 0,
                    new UserVo("非非3","http://t1.mmonly.cc/uploads/tu/201708/9999/230e7fd491.jpg",1)));
            add(new TalkComment("你们是瞎的吗，这么好看的主角", 0, 0,
                    new UserVo("非非3","http://t1.mmonly.cc/uploads/tu/201708/9999/230e7fd491.jpg",1)));
        }};
    }
}
