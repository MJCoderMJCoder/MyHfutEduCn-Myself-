package com.lzf.myhfuteducn.util;

/**
 * 服务端接口类
 * Created by MJCoder on 2018-08-01.
 */
public interface UrlUtil {
    //登录
    String LOGIN = "http://jxglstu.hfut.edu.cn:7070/appservice/home/appLogin/login.action";
    //查询学历信息
    String GET_PROJECT_INFO = "http://jxglstu.hfut.edu.cn:7070/appservice/home/publicdata/getProjectInfo.action";
    //查询所有学期以及按照学期分的周相关信息
    String GET_SEMESTER_WEEK_LIST = "http://jxglstu.hfut.edu.cn:7070/appservice/home/publicdata/getSemesterAndWeekList.action";
    //获取一周的课程表
    String GET_WEEK_SCHEDULE = "http://jxglstu.hfut.edu.cn:7070/appservice/home/schedule/getWeekSchedule.action";
    //查询成绩
    String GET_SEMESTER_SCORE = "http://jxglstu.hfut.edu.cn:7070/appservice/home/course/getSemesterScoreList.action";
}