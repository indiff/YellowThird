package com.pear.databases;

import android.provider.MediaStore;

import com.pear.yellowthird.vo.databases.CommentVo;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 2017/11/5.
 */

public class VideoDatabases {

    /**
     * 美女
     */
    static VideoIntroduceVo gBelle
            = new VideoIntroduceVo(
    1,
    "这里是美女的标题",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509872597686&di=8ab2a3ca1d78f45a435cdc32e9bf4dc8&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2Fmonth_1012%2F1012031259f9429490db5bb091.jpg",
    "美女有谁不喜欢呢美女有谁不喜欢呢美女有谁不喜欢呢",
    "http://192.168.0.104:10086/redbook/movie/redbook_20171001.mp4",
    "老湿",
    "50:05：05",
    "9.7",
    new ArrayList<String>(){{
        add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509870782336&di=e954113c80ab60c2da5a63e746206b8b&imgtype=0&src=http%3A%2F%2Fimg.anzow.com%2Fpicture%2F2012815%2F2012081534555862.jpg");
        add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509870782722&di=c7f95ff8d11552d44b5a6081ec19508d&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D1558352802%2C1733036048%26fm%3D214%26gp%3D0.jpg");
    }},
    new ArrayList<TalkComment>(){{
        add(new TalkComment("以前一直在用BaseAdapter，对于其中的getview方法的重写一直不太清楚。今天终于得以有空来探究它的详细机制。" ,4511, 0));
        add(new TalkComment("这部电影的主角超级好看" ,231, 0));
        add(new TalkComment("主角很普通而已，没有那么好看" ,11, 0));
        add(new TalkComment("你们是瞎的吗，这么好看的主角" ,0, 0));
    }});


    /**
     * 美女
     */
    static VideoIntroduceVo gBelle2
            = new VideoIntroduceVo(
            2,
            "我的女神梦",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640389658&di=3da0199dd35a3630b2726e2951e41627&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2013%2F184%2FV0U893197305.jpg",
            "我的女神梦我的女神梦我的女神梦我的女神梦",
            "http://192.168.0.104:10086/redbook/movie/redbook_20171001.mp4",
            "女神",
            "22:05：05",
            "9.8",
            new ArrayList<String>(){{
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640385290&di=637c8423eccde0332f7772bf1f648885&imgtype=0&src=http%3A%2F%2Fpic.yesky.com%2FuploadImages%2F2015%2F152%2F41%2F433DH9QD8A23.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509644254877&di=3420c3be66c8224d83daab4f5e5da241&imgtype=0&src=http%3A%2F%2Fimg.51ztzj.com%2Fupload%2Fimage%2F20141027%2Fsj201410241026_279x419.jpg");
            }},
            new ArrayList<TalkComment>(){{
        add(new TalkComment("女神女神，这么好看的主角" ,121, 0));
        add(new TalkComment("这部电影的女神女神超级好看" ,311, 0));
        add(new TalkComment("女神很普通而已，女神没有那么好看" ,411, 0));

    }});


    public static List<VideoIntroduceVo> getBelle() {
        return new ArrayList<VideoIntroduceVo>(){{
            add(gBelle);
            add(gBelle2);
        }};
    }


    /**
     * 搞笑
     */
    static VideoIntroduceVo gFunny
            = new VideoIntroduceVo(
            2,
            "我的搞笑集合",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510077510437&di=2128a3620f00606f4516446d29a777a6&imgtype=0&src=http%3A%2F%2Fpic25.photophoto.cn%2F20121120%2F0005018351815636_b.jpg",
            "我的搞笑集合搞笑集合",
            "http://192.168.0.101:8080/movie/redbook_20171004.mp4",
            "猪头",
            "26:05：05",
            "9.9",
            new ArrayList<String>(){{
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510077568298&di=311388c6f7a754a5473fd631f940d470&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F5243fbf2b2119313d87139a46f380cd791238df0.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510077573599&di=c00b25567bb2775a5c59bd876d41ba07&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fca1349540923dd546306f168db09b3de9c8248bc.jpg");
            }},
            new ArrayList<TalkComment>(){{
                add(new TalkComment("居然笑话网搞笑图片栏目提供最新搞笑图片" ,1, 0));
                add(new TalkComment("史上最雷人的穿帮镜头，让人哭笑不得" ,311, 0));
            }});

    /**
     * 搞笑
     */
    static VideoIntroduceVo gFunny2
            = new VideoIntroduceVo(
            2,
            "搞笑资源大全。",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510077998047&di=5da3253762a3929c51b34b00ed06e50c&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D760af07249c2d562f25dd8e9d221bcd9%2F91529822720e0cf318f810e90c46f21fbe09aa91.jpg",
            "开心一刻笑话大全",
            "http://192.168.0.101:8080/movie/redbook_20171004.mp4",
            "呵呵",
            "06:05：05",
            "10.0",
            new ArrayList<String>(){{
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510078029094&di=4f9de9a83d3ec7811c93d8a0ae784a6c&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fkidsbbs6%2F1303%2F19%2Fc0%2F19051858_1363683169742_1024x1024.jpg");
                add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2239977829,860543533&fm=27&gp=0.jpg");
            }},
            new ArrayList<TalkComment>(){{
                add(new TalkComment("逗B变损货了,损到哭(40900)2015-1" ,3, 0));
                add(new TalkComment("逗女孩子开心的短笑话" ,1, 60));
            }});


    public static List<VideoIntroduceVo> getFunny() {
        return new ArrayList<VideoIntroduceVo>(){{
            add(gFunny);
            add(gFunny2);
        }};
    }
}
