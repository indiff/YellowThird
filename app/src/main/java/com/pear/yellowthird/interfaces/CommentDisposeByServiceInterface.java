package com.pear.yellowthird.interfaces;

import rx.Observable;

/**
 * 跟服务器交互的评论的接口
 */

public interface CommentDisposeByServiceInterface {

    /**
     *  获取指定资源的评论
     *  @return
     * */
    Observable<String> getCommentById(Integer id);

    /**
     * 添加评论
     * */
    Observable<Boolean> addComment(Integer userId,Integer pid, String content);

    /**
     * 给指定评论点赞
     * */
    Observable<Boolean> addCommentClickGood(Integer id);

    /**
     * 最新的评论总量
     * */
    void onCommentChange(int count);

}
