/**
 *
 */
package com.lzf.myhfuteducn.bean;


import java.io.Serializable;

/**
 * @author MJCoder
 */
public class Comment implements Serializable {
    private int commentId; // 评论ID
    private long commentTime; // 发布评论的时间
    private String commentTxt; // 评论文本
    private int commentLog; // 评论的是那条旅游日志的记录
    private String commentUserName;
    private String commentUserPhone;
    private String commentUserEmail;
    private String commentUserGender;
    private String commentUserBirthday;
    private String commentUserDepart;
    private String commentUserMajor;
    private String commentUserClass;
    private boolean commentIsAnonymity; // 是否匿名
    private String replyUserName;
    private String replyUserPhone;
    private String replyUserEmail;
    private String replyUserGender;
    private String replyUserBirthday;
    private String replyUserDepart;
    private String replyUserMajor;
    private String replyUserClass;

    public Comment() {
        super();
        // TODO Auto-generated constructor stub
    }

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

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
    }

    public int getCommentLog() {
        return commentLog;
    }

    public void setCommentLog(int commentLog) {
        this.commentLog = commentLog;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserPhone() {
        return commentUserPhone;
    }

    public void setCommentUserPhone(String commentUserPhone) {
        this.commentUserPhone = commentUserPhone;
    }

    public String getCommentUserEmail() {
        return commentUserEmail;
    }

    public void setCommentUserEmail(String commentUserEmail) {
        this.commentUserEmail = commentUserEmail;
    }

    public String getCommentUserGender() {
        return commentUserGender;
    }

    public void setCommentUserGender(String commentUserGender) {
        this.commentUserGender = commentUserGender;
    }

    public String getCommentUserBirthday() {
        return commentUserBirthday;
    }

    public void setCommentUserBirthday(String commentUserBirthday) {
        this.commentUserBirthday = commentUserBirthday;
    }

    public String getCommentUserDepart() {
        return commentUserDepart;
    }

    public void setCommentUserDepart(String commentUserDepart) {
        this.commentUserDepart = commentUserDepart;
    }

    public String getCommentUserMajor() {
        return commentUserMajor;
    }

    public void setCommentUserMajor(String commentUserMajor) {
        this.commentUserMajor = commentUserMajor;
    }

    public String getCommentUserClass() {
        return commentUserClass;
    }

    public void setCommentUserClass(String commentUserClass) {
        this.commentUserClass = commentUserClass;
    }

    public boolean isCommentIsAnonymity() {
        return commentIsAnonymity;
    }

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
