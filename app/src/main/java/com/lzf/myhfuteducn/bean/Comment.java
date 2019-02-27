/**
 *
 */
package com.lzf.myhfuteducn.bean;


import java.io.Serializable;

/**
 * 评论实体类
 *
 * @author MJCoder
 * @see java.io.Serializable
 */
public class Comment implements Serializable {
    /**
     * 发布评论ID（主键）
     */
    private int commentId;
    /**
     * 发布评论的时间
     */
    private long commentTime;
    /**
     * 发布的评论文本内容
     */
    private String commentTxt;
    /**
     * 评论的是哪条日志的记录（外键：对应Log的logId）
     */
    private int commentLog;
    /**
     * 发表该评论的用户的姓名
     */
    private String commentUserName;
    /**
     * 发表该评论的用户的手机
     */
    private String commentUserPhone;
    /**
     * 发表该评论的用户的邮箱
     */
    private String commentUserEmail;
    /**
     * 发表该评论的用户的性别
     */
    private String commentUserGender;
    /**
     * 发表该评论的用户的出生日期
     */
    private String commentUserBirthday;
    /**
     * 发表该评论的用户所在学院
     */
    private String commentUserDepart;
    /**
     * 发表该评论的用户的专业
     */
    private String commentUserMajor;
    /**
     * 发表该评论的用户的班级
     */
    private String commentUserClass;
    /**
     * 是否匿名评论
     */
    private boolean commentIsAnonymity;
    private String replyUserName;
    private String replyUserPhone;
    private String replyUserEmail;
    private String replyUserGender;
    private String replyUserBirthday;
    private String replyUserDepart;
    private String replyUserMajor;
    private String replyUserClass;

    /**
     * 评论实体类的无参数构造方法
     */
    public Comment() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 评论实体类的有参构造方法
     *
     * @param commentId           发布评论ID（主键）
     * @param commentTime         发布评论的时间
     * @param commentTxt          发布的评论文本内容
     * @param commentLog          评论的是哪条日志的记录（外键：对应Log的logId）
     * @param commentUserName     发表该评论的用户的姓名
     * @param commentUserPhone    发表该评论的用户的手机
     * @param commentUserEmail    发表该评论的用户的邮箱
     * @param commentUserGender   发表该评论的用户的性别
     * @param commentUserBirthday 发表该评论的用户的出生日期
     * @param commentUserDepart   发表该评论的用户所在学院
     * @param commentUserMajor    发表该评论的用户的专业
     * @param commentUserClass    发表该评论的用户的班级
     * @param commentIsAnonymity  是否匿名评论
     * @param replyUserName
     * @param replyUserPhone
     * @param replyUserEmail
     * @param replyUserGender
     * @param replyUserBirthday
     * @param replyUserDepart
     * @param replyUserMajor
     * @param replyUserClass
     */
    public Comment(int commentId, long commentTime, String commentTxt, int commentLog, String commentUserName, String commentUserPhone, String commentUserEmail,
                   String commentUserGender, String commentUserBirthday, String commentUserDepart, String commentUserMajor, String commentUserClass, boolean commentIsAnonymity,
                   String replyUserName, String replyUserPhone, String replyUserEmail, String replyUserGender, String replyUserBirthday, String replyUserDepart, String replyUserMajor,
                   String replyUserClass) {
        super();
        this.commentId = commentId;
        this.commentTime = commentTime;
        this.commentTxt = commentTxt;
        this.commentLog = commentLog;
        this.commentUserName = commentUserName;
        this.commentUserPhone = commentUserPhone;
        this.commentUserEmail = commentUserEmail;
        this.commentUserGender = commentUserGender;
        this.commentUserBirthday = commentUserBirthday;
        this.commentUserDepart = commentUserDepart;
        this.commentUserMajor = commentUserMajor;
        this.commentUserClass = commentUserClass;
        this.commentIsAnonymity = commentIsAnonymity;
        this.replyUserName = replyUserName;
        this.replyUserPhone = replyUserPhone;
        this.replyUserEmail = replyUserEmail;
        this.replyUserGender = replyUserGender;
        this.replyUserBirthday = replyUserBirthday;
        this.replyUserDepart = replyUserDepart;
        this.replyUserMajor = replyUserMajor;
        this.replyUserClass = replyUserClass;
    }

    /**
     * 获取评论ID（主键）
     *
     * @return 返回评论ID（主键）
     */
    public int getCommentId() {
        return commentId;
    }

    /**
     * 设置评论ID（主键）
     *
     * @param commentId 评论ID（主键）
     */
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    /**
     * 获取评论的时间
     *
     * @return 评论的时间
     */
    public long getCommentTime() {
        return commentTime;
    }

    /**
     * 设置评论的时间
     *
     * @param commentTime 评论的时间
     */
    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    /**
     * 获取评论文本内容
     *
     * @return 评论文本内容
     */
    public String getCommentTxt() {
        return commentTxt;
    }

    /**
     * 设置评论文本内容
     *
     * @param commentTxt 评论文本内容
     */
    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
    }

    /**
     * 获取评论的日志的logId（外键：对应Log的logId）
     *
     * @return 日志的logId（外键：对应Log的logId）
     * @see Log
     */
    public int getCommentLog() {
        return commentLog;
    }

    /**
     * 设置评论的日志的logId（外键：对应Log的logId）
     *
     * @param commentLog 日志的logId（外键：对应Log的logId）
     * @see Log
     */
    public void setCommentLog(int commentLog) {
        this.commentLog = commentLog;
    }

    /**
     * 获取发表该评论的用户的姓名
     *
     * @return 发表该评论的用户的姓名
     */
    public String getCommentUserName() {
        return commentUserName;
    }

    /**
     * 设置发表该评论的用户的姓名
     *
     * @param commentUserName 发表该评论的用户的姓名
     */
    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    /**
     * 获取发表该评论的用户的手机
     *
     * @return 发表该评论的用户的手机
     */
    public String getCommentUserPhone() {
        return commentUserPhone;
    }

    /**
     * 设置发表该评论的用户的手机
     *
     * @param commentUserPhone 发表该评论的用户的手机
     */
    public void setCommentUserPhone(String commentUserPhone) {
        this.commentUserPhone = commentUserPhone;
    }

    /**
     * 获取发表该评论的用户的邮箱
     *
     * @return 发表该评论的用户的邮箱
     */
    public String getCommentUserEmail() {
        return commentUserEmail;
    }

    /**
     * 设置发表该评论的用户的邮箱
     *
     * @param commentUserEmail 发表该评论的用户的邮箱
     */
    public void setCommentUserEmail(String commentUserEmail) {
        this.commentUserEmail = commentUserEmail;
    }

    public String getCommentUserGender() {
        return commentUserGender;
    }

    public void setCommentUserGender(String commentUserGender) {
        this.commentUserGender = commentUserGender;
    }

    /**
     * 获取发表该评论的用户的出生日期
     *
     * @return 发表该评论的用户的出生日期
     */
    public String getCommentUserBirthday() {
        return commentUserBirthday;
    }

    /**
     * 设置发表该评论的用户的出生日期
     *
     * @param commentUserBirthday 发表该评论的用户的出生日期
     */
    public void setCommentUserBirthday(String commentUserBirthday) {
        this.commentUserBirthday = commentUserBirthday;
    }

    /**
     * 获取发表该评论的用户的学院
     *
     * @return 发表该评论的用户的学院
     */
    public String getCommentUserDepart() {
        return commentUserDepart;
    }

    /**
     * 设置发表该评论的用户的学院
     *
     * @param commentUserDepart 发表该评论的用户的学院
     */
    public void setCommentUserDepart(String commentUserDepart) {
        this.commentUserDepart = commentUserDepart;
    }

    /**
     * 获取发表该评论的用户的专业
     *
     * @return 发表该评论的用户的专业
     */
    public String getCommentUserMajor() {
        return commentUserMajor;
    }

    /**
     * 设置发表该评论的用户的专业
     *
     * @param commentUserMajor 发表该评论的用户的专业
     */
    public void setCommentUserMajor(String commentUserMajor) {
        this.commentUserMajor = commentUserMajor;
    }

    /**
     * 获取发表该评论的用户的班级
     *
     * @return 发表该评论的用户的班级
     */
    public String getCommentUserClass() {
        return commentUserClass;
    }

    /**
     * 设置发表该评论的用户的班级
     *
     * @param commentUserClass 发表该评论的用户的班级
     */
    public void setCommentUserClass(String commentUserClass) {
        this.commentUserClass = commentUserClass;
    }

    /**
     * 获取用户是否匿名发表该评论
     *
     * @return 用户是否匿名发表该评论（true：匿名发表；false：实名发表）
     */
    public boolean isCommentIsAnonymity() {
        return commentIsAnonymity;
    }

    /**
     * 设置用户是否匿名发表该评论
     *
     * @param commentIsAnonymity 用户是否匿名发表该评论（true：匿名发表；false：实名发表）
     */
    public void setCommentIsAnonymity(boolean commentIsAnonymity) {
        this.commentIsAnonymity = commentIsAnonymity;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public String getReplyUserPhone() {
        return replyUserPhone;
    }

    public void setReplyUserPhone(String replyUserPhone) {
        this.replyUserPhone = replyUserPhone;
    }

    public String getReplyUserEmail() {
        return replyUserEmail;
    }

    public void setReplyUserEmail(String replyUserEmail) {
        this.replyUserEmail = replyUserEmail;
    }

    public String getReplyUserGender() {
        return replyUserGender;
    }

    public void setReplyUserGender(String replyUserGender) {
        this.replyUserGender = replyUserGender;
    }

    public String getReplyUserBirthday() {
        return replyUserBirthday;
    }

    public void setReplyUserBirthday(String replyUserBirthday) {
        this.replyUserBirthday = replyUserBirthday;
    }

    public String getReplyUserDepart() {
        return replyUserDepart;
    }

    public void setReplyUserDepart(String replyUserDepart) {
        this.replyUserDepart = replyUserDepart;
    }

    public String getReplyUserMajor() {
        return replyUserMajor;
    }

    public void setReplyUserMajor(String replyUserMajor) {
        this.replyUserMajor = replyUserMajor;
    }

    public String getReplyUserClass() {
        return replyUserClass;
    }

    public void setReplyUserClass(String replyUserClass) {
        this.replyUserClass = replyUserClass;
    }

    @Override
    public String toString() {
        return "Comment [commentId=" + commentId + ", commentTime=" + commentTime + ", commentTxt=" + commentTxt + ", commentLog=" + commentLog + ", commentUserName="
                + commentUserName + ", commentUserPhone=" + commentUserPhone + ", commentUserEmail=" + commentUserEmail + ", commentUserGender=" + commentUserGender
                + ", commentUserBirthday=" + commentUserBirthday + ", commentUserDepart=" + commentUserDepart + ", commentUserMajor=" + commentUserMajor + ", commentUserClass="
                + commentUserClass + ", commentIsAnonymity=" + commentIsAnonymity + ", replyUserName=" + replyUserName + ", replyUserPhone=" + replyUserPhone + ", replyUserEmail="
                + replyUserEmail + ", replyUserGender=" + replyUserGender + ", replyUserBirthday=" + replyUserBirthday + ", replyUserDepart=" + replyUserDepart
                + ", replyUserMajor=" + replyUserMajor + ", replyUserClass=" + replyUserClass + "]";
    }

}
