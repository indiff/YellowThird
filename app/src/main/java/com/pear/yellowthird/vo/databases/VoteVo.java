package com.pear.yellowthird.vo.databases;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * 投票属性
 */

public class VoteVo {

    /**数据库id*/
    Integer id;

    /**标题*/
    String title;

    /**子选项*/
    List<SubSelect> voteSelect;

    /**是否已经投票*/
    boolean isAlreadyVote;

    /**当前已投票结果的计算时间*/
    String currentResultTime;

    public VoteVo() {
    }

    public VoteVo(Integer id, String title, List<SubSelect> subSelects, boolean isCanVote, String tip) {
        this.id = id;
        this.title = title;
        this.voteSelect = subSelects;
        this.isAlreadyVote = isCanVote;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SubSelect> getVoteSelect() {
        return voteSelect;
    }

    public void setVoteSelect(List<SubSelect> voteSelect) {
        this.voteSelect = voteSelect;
    }

    public boolean isAlreadyVote() {
        return isAlreadyVote;
    }

    public void setAlreadyVote(boolean alreadyVote) {
        isAlreadyVote = alreadyVote;
    }

    public String getCurrentResultTime() {
        return currentResultTime;
    }

    public void setCurrentResultTime(String currentResultTime) {
        this.currentResultTime = currentResultTime;
    }

    /**
     * 待投票的子选项
     * */
    public static class SubSelect implements Comparable<SubSelect>
    {

        /**数据库的ID*/
        Integer id;

        /**选项的标题*/
        String title;

        /**选项的关键词*/
        String keyWord;

        /**图片的地址*/
        String imageUri;

        /**当前多少人选的总量*/
        Integer count;

        /**是否选了这个选项*/
        boolean isAlreadyVote;

        public SubSelect() {
        }

        public SubSelect(String content, Integer count) {
            this.title = content;
            this.count = count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public String getImageUri() {
            return imageUri;
        }

        public void setImageUri(String imageUri) {
            this.imageUri = imageUri;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public boolean isAlreadyVote() {
            return isAlreadyVote;
        }

        public void setAlreadyVote(boolean alreadyVote) {
            isAlreadyVote = alreadyVote;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public int compareTo(SubSelect o) {
            if(count==o.getCount())
                return 0;
            if(count>o.getCount())
                return 1;
            return -1;
        }
    }

}
