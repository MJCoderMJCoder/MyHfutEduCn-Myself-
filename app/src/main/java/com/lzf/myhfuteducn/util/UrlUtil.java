package com.lzf.myhfuteducn.util;

/**
 * 服务端接口
 *
 * @author MJCoder
 */
public interface UrlUtil {
    /**
     * 教务系统主机域名
     */
    String EDU_HOST = "http://jxglstu.hfut.edu.cn:7070/appservice/";
    /**
     * 阿里云主机域名
     */
    String MY_HOST = "http://47.97.199.20:8080/MyHfutEduCn/";

    /**
     * 登录
     */
    String LOGIN = EDU_HOST + "home/appLogin/login.action";
    /**
     * 查询学历信息API
     */
    String GET_PROJECT_INFO = EDU_HOST + "home/publicdata/getProjectInfo.action";
    /**
     * 查询所有学期以及按照学期分的周相关信息API
     */
    String GET_SEMESTER_WEEK_LIST = EDU_HOST + "home/publicdata/getSemesterAndWeekList.action";
    /**
     * 获取一周的课程表API
     */
    String GET_WEEK_SCHEDULE = EDU_HOST + "home/schedule/getWeekSchedule.action";
    /**
     * 查询成绩API
     */
    String GET_SEMESTER_SCORE = EDU_HOST + "home/course/getSemesterScoreList.action";
    /**
     * 修改手机邮箱API
     */
    String EDIT_PHONE_EMAIL = EDU_HOST + "home/appLogin/editPhoneOrEmail.action";
    /**
     * 上传发表日志API
     */
    String LOG_INSERT = MY_HOST + "log/insert";
    /**
     * 模糊查询已经发布的日志API
     */
    String LOG_DIMSELECT = MY_HOST + "log/dimSelect";
    /**
     * 查询已经发布的日志API
     */
    String LOG_SELECT = MY_HOST + "log/select";
    /**
     * 日志点赞API
     */
    String LOG_PRAISE = MY_HOST + "log/praise";
    /**
     * 日志删除API
     */
    String LOG_DELETE = MY_HOST + "log/delete";
    /**
     * 新建评论API
     */
    String COMMENT_INSERT = MY_HOST + "comment/insert";
}