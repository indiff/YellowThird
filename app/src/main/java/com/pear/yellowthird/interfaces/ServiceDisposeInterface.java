package com.pear.yellowthird.interfaces;

import com.pear.yellowthird.vo.databases.BillVo;
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


    /**电影相关*/

    /**
     * 查询电影集合
     * @param sql 查询的语句
     * */
    String queryVideoList(String sql);

    /**
     * 查询电影的评论
     * */
    String queryVideoComment(Integer id);

    /**
     * 给电影添加评论
     * */
    boolean addVideoComment(String id,String content);

    /**
     * 给当前电影点赞
     * */
    boolean addVideoClickGoodById(Integer id);

    /**
     * 给电影下的评论点赞
     * */
    boolean addVideoUserCommentClickGood(Integer id);

    /**
     * 根据Url 获取对应数据
     * @pram url 待查询的url
     * */
    String queryByUrl(String url);




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
     * 发表说说
     * */
    Observable<Boolean> publishFriendTalk(String content,List<String> images);



}
