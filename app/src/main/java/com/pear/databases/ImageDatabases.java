package com.pear.databases;

import com.pear.yellowthird.vo.databases.CommentVo;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.ImageSummary;
import com.pear.yellowthird.vo.databases.VideoIntroduceVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片相关的模拟数据库
 */

public class ImageDatabases {

    /**
     * 趣图
     */
    static ImageIntroduceVo funny
            = new ImageIntroduceVo(
            "这个美女很好看",
            "从前，有位县督学来到县立中学视察工作。他一进校门，便见到该校的壁报上写有杜牧的诗句：停车坐爱枫林晚，霜叶红于二月花。",
            new ArrayList<String>() {{
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509644254877&di=3420c3be66c8224d83daab4f5e5da241&imgtype=0&src=http%3A%2F%2Fimg.51ztzj.com%2Fupload%2Fimage%2F20141027%2Fsj201410241026_279x419.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640385290&di=637c8423eccde0332f7772bf1f648885&imgtype=0&src=http%3A%2F%2Fpic.yesky.com%2FuploadImages%2F2015%2F152%2F41%2F433DH9QD8A23.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509644282278&di=5c7672d364e286a23f855b84ae4f1501&imgtype=0&src=http%3A%2F%2Fi8.download.fd.pchome.net%2Ft_320x520%2Fg1%2FM00%2F0C%2F08%2FooYBAFRTGE-ILhbaAAJeRimfw2EAACC8gJrC2QAAl5e173.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513528415985&di=17b4b9ce6d83ea532da9797c531dbcd4&imgtype=0&src=http%3A%2F%2Fpic28.photophoto.cn%2F20130725%2F0036036371391750_b.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513528543414&di=e85137ea9967a7b57e4aaa8169580719&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F6159252dd42a28342ec52ce651b5c9ea14cebfff.jpg");
            }},
            100,
            "17分钟前",
            999
    );

    /**
     * 动物
     */
    static ImageIntroduceVo zoology
            = new ImageIntroduceVo(
            "天啊，这个马鞭好大",
            "",
            new ArrayList<String>() {{

                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640617338&di=8f1ac97c878d62dae20bdfda47674257&imgtype=0&src=http%3A%2F%2Fimg5.niutuku.com%2Fphone%2F1212%2F0244%2F0244-niutuku.com-86913.jpg");
                add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4285006730,1542956189&fm=27&gp=0.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510235301&di=d8f5fdf498fe6e3357f10c9eecc6edb9&imgtype=jpg&er=1&src=http%3A%2F%2Fpic43.nipic.com%2F20140710%2F19189698_105034122000_2.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640577631&di=60a0c0b76daeaf3abcc1b528a5042300&imgtype=0&src=http%3A%2F%2Fpic15.nipic.com%2F20110715%2F2473856_134011566000_2.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640572611&di=de58400e1216d51a0bf010ee97edc448&imgtype=0&src=http%3A%2F%2Fimg5.niutuku.com%2Fphone%2F1212%2F2801%2F2801-niutuku.com-94034.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640568297&di=ff640eeadd07b772fdc80f79aeb9b3d3&imgtype=0&src=http%3A%2F%2Fimg5.niutuku.com%2Fphone%2F1301%2F1448%2F1448-niutuku.com-298246.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640922509&di=f57a6d8f41943632b2e1b55f0e986e0a&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201312%2F16%2F20131216122956_2PMPW.thumb.700_0.jpeg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640923191&di=c80145729d97f06ae172c2a6c4aeafe7&imgtype=0&src=http%3A%2F%2Fpic27.photophoto.cn%2F20130612%2F0035035729475912_b.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640925370&di=46c0439a84ded883bd59a5b5d1ac2f27&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D1214375319%2C3446219398%26fm%3D214%26gp%3D0.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509645186205&di=798059d662da85a41325398960a56785&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201507%2F24%2F20150724203400_fBKdH.jpeg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640925297&di=7e288267404b64971b5fd437b1694fd2&imgtype=0&src=http%3A%2F%2Fimg-download.pchome.net%2Fdownload%2F1k0%2Fp7%2F2j%2Fo8y0oy-8dq.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513532748915&di=3670db575ab11acd38c50dba6a6de08b&imgtype=0&src=http%3A%2F%2Fscimg.jb51.net%2Fallimg%2F150708%2F14-150FQ15305155.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640617338&di=8f1ac97c878d62dae20bdfda47674257&imgtype=0&src=http%3A%2F%2Fimg5.niutuku.com%2Fphone%2F1212%2F0244%2F0244-niutuku.com-86913.jpg");
                add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4285006730,1542956189&fm=27&gp=0.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510235301&di=d8f5fdf498fe6e3357f10c9eecc6edb9&imgtype=jpg&er=1&src=http%3A%2F%2Fpic43.nipic.com%2F20140710%2F19189698_105034122000_2.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640577631&di=60a0c0b76daeaf3abcc1b528a5042300&imgtype=0&src=http%3A%2F%2Fpic15.nipic.com%2F20110715%2F2473856_134011566000_2.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640572611&di=de58400e1216d51a0bf010ee97edc448&imgtype=0&src=http%3A%2F%2Fimg5.niutuku.com%2Fphone%2F1212%2F2801%2F2801-niutuku.com-94034.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640568297&di=ff640eeadd07b772fdc80f79aeb9b3d3&imgtype=0&src=http%3A%2F%2Fimg5.niutuku.com%2Fphone%2F1301%2F1448%2F1448-niutuku.com-298246.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640922509&di=f57a6d8f41943632b2e1b55f0e986e0a&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201312%2F16%2F20131216122956_2PMPW.thumb.700_0.jpeg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640923191&di=c80145729d97f06ae172c2a6c4aeafe7&imgtype=0&src=http%3A%2F%2Fpic27.photophoto.cn%2F20130612%2F0035035729475912_b.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640925370&di=46c0439a84ded883bd59a5b5d1ac2f27&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D1214375319%2C3446219398%26fm%3D214%26gp%3D0.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509645186205&di=798059d662da85a41325398960a56785&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201507%2F24%2F20150724203400_fBKdH.jpeg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509640925297&di=7e288267404b64971b5fd437b1694fd2&imgtype=0&src=http%3A%2F%2Fimg-download.pchome.net%2Fdownload%2F1k0%2Fp7%2F2j%2Fo8y0oy-8dq.jpg");
                add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513532748915&di=3670db575ab11acd38c50dba6a6de08b&imgtype=0&src=http%3A%2F%2Fscimg.jb51.net%2Fallimg%2F150708%2F14-150FQ15305155.jpg");
            }},
            10,
            "18分钟前",
            89
    );


    public static ImageIntroduceVo getFunny() {
        return funny;
    }

    public static ImageIntroduceVo getZoology() {
        return zoology;
    }

    public static List<ImageIntroduceVo> getAll() {
        return new ArrayList<ImageIntroduceVo>(){{
            add(funny);
            add(zoology);
        }};
    }


}
