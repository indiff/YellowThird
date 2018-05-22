package com.pear.yellowthird.interfaces;

import com.pear.yellowthird.vo.databases.BillVo;
import com.pear.yellowthird.vo.databases.CommentVo;
import com.pear.yellowthird.vo.databases.FastShakeVo;
import com.pear.yellowthird.vo.databases.FriendsVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;

import java.util.List;

import rx.Observable;


/**
 * 服务器交互处理的接口
 */

public interface ServiceDisposeInterface {

    /**
     * 查询主菜单的数据
     */
    Observable<String> queryMainMenu();

    /**
     * 查询电影的评论
     * */
    Observable<String> queryVideoComment(Integer id);

    /**
     * 给电影添加评论
     * */
    Observable<Boolean> addVideoComment(String id,String content);

    /**
     * 给当前电影点赞
     * */
    Observable<Boolean> addVideoClickGoodById(Integer id);

    /**
     * 给电影下的评论点赞
     * */
    Observable<Boolean> addVideoUserCommentClickGood(Integer id);

    /**
     * 请求播放电影
     * */
    Observable<String> requestPlayVideo(Integer id);

    /**
     * 请求跳跃播放电影
     * */
    Observable<String> requestJumpPlayVideo(Integer id);

    /**
     * 根据Url 获取对应数据
     * @pram url 待查询的url
     * */
    String queryByUrl(String url);

    /**
     * 添加图片浏览次数
     * */
    Boolean addImageShowCount(Integer id);


    /**用户信息相关*/
    /**
     * 获取用户信息，可以去缓存的数据
     * */
    Observable<UserVo> getUser();

    /**
     *  强制刷新用户的数据
     * */
    Observable<UserVo> refreshUser();

    /**
     *  随机更换用户头像
     *  @return 返回最新头像地址
     * */
    Observable<String> randomUserIcon();

    /**
     *  用户更换名称
     *  @return
     * */
    Observable<Boolean> changeUserName(String name);

    /**
     *  获取充值的网页地址
     */
    String getRechargeWebUrl();

    /**
     *  获取分享的网页地址
     */
    String getSharedWebUrl();

    /**
     *  获取账单信息
     *  @return
     * */
    Observable<BillVo[]> getBilli();

    /**
     *  点击投票
     *  @return
     * */
    Observable<Boolean> clickVote(int voteId,int subSelectId);





    /**
     * 给图片下点赞
     * */
    Observable<Boolean> addImageClickGood(Integer id);


    /**************************************朋友圈*************************************/


    /**
     *  查询朋友圈数据
     *  @return
     * */
    Observable<FriendsVo[]> queryFriendList();

    /**
     *  下拉刷新更多的朋友圈数据
     *  @return
     * */
    Observable<FriendsVo[]> queryMoreFriendList(Integer lastId);


    /**
     * 上拉刷新最新的朋友圈数据
     *  @return
     * */
    Observable<FriendsVo[]> refreshNewHeadFriendList(Integer firstId);

    /**
     * 发表说说
     * */
    Observable<Boolean> publishFriendTalk(String content,List<String> images);

    /**
     * 对某条朋友圈点赞
     * */
    Observable<Boolean> friendClickGood(Integer id);


    /**
     * 给朋友圈添加评论
     * */
    Observable<Boolean> addFriendComment(Integer id,String content);

    /**
     * 添加朋友圈浏览次数
     * */
    Boolean addFriendShowCount(Integer id);

    /**
     *  获取最新版本的更新包
     *  @return
     * */
    Observable<String> getNewsVersion();

    /**
     * 程序奔溃了，把日记发送到服务器
     * */
    void sendAppCrashServer(String content);

    /**
     * 自动选择最好的服务器线路
     * */
    void autoChooseGoodService(Runnable success);






    /**获取快抖资源*/
    /**
     *  获取快抖集合
     *  @return
     * */
    Observable<FastShakeVo[]> getFastShakeList();

    /**
     *  获取快抖指定视频的评论
     *  @return
     * */
    Observable<String> getFastShakeCommentById(Integer id);

    /**
     * 给快抖指定视频添加评论
     * */
    Observable<Boolean> addFastShakeComment(Integer userId,Integer pid,String content);

    /**
     * 对某条快抖添加爱心
     * */
    Observable<Boolean> addFastShakeLove(Integer id);

    /**
     * 对某条快抖的评论点赞添加爱心
     * */
    Observable<Boolean> addFastShakeCommentGoodCount(Integer id);

    /**
     * 添加快抖浏览次数
     * */
    Observable<Boolean> addFastShakeShowCount(Integer id);


}
