/**
 *
 */
package com.lzf.myhfuteducn.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 用户在社区发布的日子实体类
 *
 * @author MJCoder
 * @see java.io.Serializable
 */
public class Log implements Serializable {
    /**
     * 用户在社区发布的日志ID
     */
    private int logId;
    /**
     * 社区日志的发布时间
     */
    private long logTime;
    /**
     * 用户在社区发布的日志图片
     */
    private String logImg;
    /**
     * 用户在社区发布的日志文本内容
     */
    private String logTxt;
    /**
     * 用户在社区发布的日志的点赞数
     */
    private int logPraise;
    /**
     * 发布该日志的用户的姓名
     */
    private String logUserName;
    /**
     * 发布该日志的用户的手机
     */
    private String logUserPhone;
    /**
     * 发布该日志的用户的邮箱
     */
    private String logUserEmail;
    /**
     * 发布该日志的用户的性别
     */
    private String logUserGender;
    /**
     * 发布该日志的用户的出生日期
     */
    private String logUserBirthday;
    /**
     * 发布该日志的用户所在的学院
     */
    private String logUserDepart;
    /**
     * 发布该日志的用户的专业
     */
    private String logUserMajor;
    /**
     * 发布该日志的用户的班级
     */
    private String logUserClass;
    /**
     * 该日志是否匿名发表
     */
    private boolean logIsAnonymity;
    /**
     * 与该日志相关的所有评论
     */
    private List<Comment> comments;

    /**
     * 用户在社区发布的日子实体的无参构造方法
     */
    public Log() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 用户在社区发布的日子实体的有参构造方法
     *
     * @param logId           用户在社区发布的日志ID
     * @param logTime         社区日志的发布时间
     * @param logImg          用户在社区发布的日志图片
     * @param logTxt          用户在社区发布的日志文本内容
     * @param logPraise       用户在社区发布的日志的点赞数
     * @param logUserName     发布该日志的用户的姓名
     * @param logUserPhone    发布该日志的用户的手机
     * @param logUserEmail    发布该日志的用户的邮箱
     * @param logUserGender   发布该日志的用户的性别
     * @param logUserBirthday 发布该日志的用户的出生日期
     * @param logUserDepart   发布该日志的用户所在的学院
     * @param logUserMajor    发布该日志的用户的专业
     * @param logUserClass    发布该日志的用户的班级
     * @param logIsAnonymity  该日志是否匿名发表
     * @param comments        与该日志相关的所有评论
     */
    public Log(int logId, long logTime, String logImg, String logTxt, int logPraise, String logUserName, String logUserPhone, String logUserEmail, String logUserGender,
               String logUserBirthday, String logUserDepart, String logUserMajor, String logUserClass, boolean logIsAnonymity, List<Comment> comments) {
        super();
        this.logId = logId;
        this.logTime = logTime;
        this.logImg = logImg;
        this.logTxt = logTxt;
        this.logPraise = logPraise;
        this.logUserName = logUserName;
        this.logUserPhone = logUserPhone;
        this.logUserEmail = logUserEmail;
        this.logUserGender = logUserGender;
        this.logUserBirthday = logUserBirthday;
        this.logUserDepart = logUserDepart;
        this.logUserMajor = logUserMajor;
        this.logUserClass = logUserClass;
        this.logIsAnonymity = logIsAnonymity;
        this.comments = comments;
    }

    /**
     * 获取用户在社区发布的日志ID
     *
     * @return 用户在社区发布的日志ID
     */
    public int getLogId() {
        return logId;
    }

    /**
     * 设置用户在社区发布的日志ID
     *
     * @param logId 用户在社区发布的日志ID
     */
    public void setLogId(int logId) {
        this.logId = logId;
    }

    /**
     * 获取社区日志的发布时间
     *
     * @return 社区日志的发布时间
     */
    public long getLogTime() {
        return logTime;
    }

    /**
     * 设置社区日志的发布时间
     *
     * @param logTime 社区日志的发布时间
     */
    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }

    /**
     * 获取用户在社区发布的日志图片
     *
     * @return 用户在社区发布的日志图片
     */
    public String getLogImg() {
        return logImg;
    }

    /**
     * 设置用户在社区发布的日志图片
     *
     * @param logImg 用户在社区发布的日志图片
     */
    public void setLogImg(String logImg) {
        this.logImg = logImg;
    }

    /**
     * 获取用户在社区发布的日志文本内容
     *
     * @return 用户在社区发布的日志文本内容
     */
    public String getLogTxt() {
        return logTxt;
    }

    /**
     * 设置用户在社区发布的日志文本内容
     *
     * @param logTxt 用户在社区发布的日志文本内容
     */
    public void setLogTxt(String logTxt) {
        this.logTxt = logTxt;
    }

    /**
     * 获取用户在社区发布的日志的点赞数
     *
     * @return 用户在社区发布的日志的点赞数
     */
    public int getLogPraise() {
        return logPraise;
    }

    /**
     * 设置用户在社区发布的日志的点赞数
     *
     * @param logPraise 用户在社区发布的日志的点赞数
     */
    public void setLogPraise(int logPraise) {
        this.logPraise = logPraise;
    }

    /**
     * 获取发布该日志的用户的姓名
     *
     * @return 发布该日志的用户的姓名
     */
    public String getLogUserName() {
        return logUserName;
    }

    /**
     * 设置发布该日志的用户的姓名
     *
     * @param logUserName 发布该日志的用户的姓名
     */
    public void setLogUserName(String logUserName) {
        this.logUserName = logUserName;
    }

    /**
     * 获取发布该日志的用户的手机
     *
     * @return 发布该日志的用户的手机
     */
    public String getLogUserPhone() {
        return logUserPhone;
    }

    /**
     * 设置发布该日志的用户的手机
     *
     * @param logUserPhone 发布该日志的用户的手机
     */
    public void setLogUserPhone(String logUserPhone) {
        this.logUserPhone = logUserPhone;
    }

    /**
     * 获取发布该日志的用户的邮箱
     *
     * @return 发布该日志的用户的邮箱
     */
    public String getLogUserEmail() {
        return logUserEmail;
    }

    /**
     * 设置发布该日志的用户的邮箱
     *
     * @param logUserEmail 发布该日志的用户的邮箱
     */
    public void setLogUserEmail(String logUserEmail) {
        this.logUserEmail = logUserEmail;
    }

    /**
     * 获取发布该日志的用户的性别
     *
     * @return 发布该日志的用户的性别
     */
    public String getLogUserGender() {
        return logUserGender;
    }

    /**
     * 设置发布该日志的用户的性别
     *
     * @param logUserGender 发布该日志的用户的性别
     */
    public void setLogUserGender(String logUserGender) {
        this.logUserGender = logUserGender;
    }

    /**
     * 获取发布该日志的用户的出生日期
     *
     * @return 发布该日志的用户的出生日期
     */
    public String getLogUserBirthday() {
        return logUserBirthday;
    }

    /**
     * 设置发布该日志的用户的出生日期
     *
     * @param logUserBirthday 发布该日志的用户的出生日期
     */
    public void setLogUserBirthday(String logUserBirthday) {
        this.logUserBirthday = logUserBirthday;
    }

    /**
     * 获取发布该日志的用户所在的学院
     *
     * @return 发布该日志的用户所在的学院
     */
    public String getLogUserDepart() {
        return logUserDepart;
    }

    /**
     * 设置发布该日志的用户所在的学院
     *
     * @param logUserDepart 发布该日志的用户所在的学院
     */
    public void setLogUserDepart(String logUserDepart) {
        this.logUserDepart = logUserDepart;
    }

    /**
     * 获取发布该日志的用户的专业
     *
     * @return 发布该日志的用户的专业
     */
    public String getLogUserMajor() {
        return logUserMajor;
    }

    /**
     * 设置发布该日志的用户的专业
     *
     * @param logUserMajor 发布该日志的用户的专业
     */
    public void setLogUserMajor(String logUserMajor) {
        this.logUserMajor = logUserMajor;
    }

    /**
     * 获取发布该日志的用户的班级
     *
     * @return 发布该日志的用户的班级
     */
    public String getLogUserClass() {
        return logUserClass;
    }

    /**
     * 设置发布该日志的用户的班级
     *
     * @param logUserClass 发布该日志的用户的班级
     */
    public void setLogUserClass(String logUserClass) {
        this.logUserClass = logUserClass;
    }

    /**
     * 获取该日志是否匿名发表
     *
     * @return 该日志是否匿名发表（true：匿名发表；false：实名发表）
     */
    public boolean isLogIsAnonymity() {
        return logIsAnonymity;
    }

    /**
     * 设置该日志是否匿名发表
     *
     * @param logIsAnonymity 该日志是否匿名发表（true：匿名发表；false：实名发表）
     */
    public void setLogIsAnonymity(boolean logIsAnonymity) {
        this.logIsAnonymity = logIsAnonymity;
    }

    /**
     * 获取与该日志相关的所有评论
     *
     * @return 与该日志相关的所有评论
     * @see Comment
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * 设置与该日志相关的所有评论
     *
     * @param comments 与该日志相关的所有评论
     * @see Comment
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Log [logId=" + logId + ", logTime=" + logTime + ", logImg=" + logImg + ", logTxt=" + logTxt + ", logPraise=" + logPraise + ", logUserName=" + logUserName
                + ", logUserPhone=" + logUserPhone + ", logUserEmail=" + logUserEmail + ", logUserGender=" + logUserGender + ", logUserBirthday=" + logUserBirthday
                + ", logUserDepart=" + logUserDepart + ", logUserMajor=" + logUserMajor + ", logUserClass=" + logUserClass + ", logIsAnonymity=" + logIsAnonymity + ", comments="
                + comments + "]";
    }
}
