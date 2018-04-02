package com.idophin.farmingtong.entity;

/**
 * Created by liyanchuan on 2017/5/31.
 */

public class News {
    public String id;
    public String name;//标题
    public String liulanliang;//浏览量
    public String jianjie;//"简介
    public String narong;//"内容
    //public String suoluetu;//"缩略图
    public String createtime;//
    public String narong_replace;//
    public String createtime_replace;//
    public String pinglun_num;//评论数
    public String faburen;//发布人
    public String laiyuan;//消息来源
    public String shifuzhiding;//是否置顶
    public String leixing;// 1.快讯，2.法律法规
    public String shipindizhi;//视频
    public String type;//消息类型
    public String liebiaozhanshifangsh;//图片显示方式 1 无图，2 单图 3 多图
    public String shifouyunxupinglun;//是否允许评论

    public boolean isAllowComment(){
        return "1".equals(shifouyunxupinglun);
    }


    public boolean isSinglePicMode(){
        return "2".equals(liebiaozhanshifangsh);
        //return false;
    }

    public boolean isMultiPicMode(){
        return "3".equals(liebiaozhanshifangsh);
        //return true;
    }

    public boolean isLayregulations(){
        return "2".equals(leixing);
    }

    public String getSender() {
        return faburen;
    }

    public String getNewsComeFrom() {
        return laiyuan;
    }


    public boolean isTop() {
        return "1".equals(shifuzhiding);
    }

    public String getCustCreateTime() {
        return createtime_replace;
    }

    public void setCustCreateTime(String time) {
        createtime_replace = time;
    }

}
