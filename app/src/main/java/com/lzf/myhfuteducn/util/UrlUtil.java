package com.lzf.myhfuteducn.util;

/**
 * 服务端接口类
 * Created by MJCoder on 2018-08-01.
 */
public interface UrlUtil {
    String EDU_HOST = "http://jxglstu.hfut.edu.cn:7070/appservice/";
    String MY_HOST = "http://47.97.199.20:8080/MyHfutEduCn/";
    //登录
    String LOGIN = EDU_HOST + "home/appLogin/login.action";
    //查询学历信息
    String GET_PROJECT_INFO = EDU_HOST + "home/publicdata/getProjectInfo.action";
    //查询所有学期以及按照学期分的周相关信息
    String GET_SEMESTER_WEEK_LIST = EDU_HOST + "home/publicdata/getSemesterAndWeekList.action";
    //获取一周的课程表
    String GET_WEEK_SCHEDULE = EDU_HOST + "home/schedule/getWeekSchedule.action";
    //查询成绩
    String GET_SEMESTER_SCORE = EDU_HOST + "home/course/getSemesterScoreList.action";
    //修改手机邮箱
    String EDIT_PHONE_EMAIL = EDU_HOST + "home/appLogin/editPhoneOrEmail.action";
    //上传发表日志
    String LOG_INSERT = MY_HOST + "log/insert";
    //查询已经发布的日志
    String LOG_DIMSELECT = MY_HOST + "log/dimSelect";
    //日志点赞
    String LOG_PRAISE = MY_HOST + "log/praise";
    //新建评论
    String COMMENT_INSERT = MY_HOST + "comment/insert";
}