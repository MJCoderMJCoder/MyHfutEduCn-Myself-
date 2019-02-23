/**
 *
 */
package com.lzf.myhfuteducn.bean;


import java.io.Serializable;
import java.util.List;

/**
 * @author MJCoder
 */
public class Log implements Serializable {
    private int logId; // 旅游日志ID
    private long logTime; // 旅游日志发布时间
    private String logImg; // 旅游日志的图片
    private String logTxt; // 旅游日志的文本
    private int logPraise; // 旅游日志的点赞数
    private String logUserName;
    private String logUserPhone;
    private String logUserEmail;
    private String logUserGender;
    private String logUserBirthday;
    private String logUserDepart;
    private String logUserMajor;
    private String logUserClass;
    private boolean logIsAnonymity; // 是否匿名
    private List<Comment> comments;

    public Log() {
        super();
        // TODO Auto-generated constructor stub
    }

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

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public long getLogTime() {
        return logTime;
    }

    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }

    public String getLogImg() {
        return logImg;
    }

    public void setLogImg(String logImg) {
        this.logImg = logImg;
    }

    public String getLogTxt() {
        return logTxt;
    }

    public void setLogTxt(String logTxt) {
        this.logTxt = logTxt;
    }

    public int getLogPraise() {
        return logPraise;
    }

    public void setLogPraise(int logPraise) {
        this.logPraise = logPraise;
    }

    public String getLogUserName() {
        return logUserName;
    }

    public void setLogUserName(String logUserName) {
        this.logUserName = logUserName;
    }

    public String getLogUserPhone() {
        return logUserPhone;
    }

    public void setLogUserPhone(String logUserPhone) {
        this.logUserPhone = logUserPhone;
    }

    public String getLogUserEmail() {
        return logUserEmail;
    }

    public void setLogUserEmail(String logUserEmail) {
        this.logUserEmail = logUserEmail;
    }

    public String getLogUserGender() {
        return logUserGender;
    }

    public void setLogUserGender(String logUserGender) {
        this.logUserGender = logUserGender;
    }

    public String getLogUserBirthday() {
        return logUserBirthday;
    }

    public void setLogUserBirthday(String logUserBirthday) {
        this.logUserBirthday = logUserBirthday;
    }

    public String getLogUserDepart() {
        return logUserDepart;
    }

    public void setLogUserDepart(String logUserDepart) {
        this.logUserDepart = logUserDepart;
    }

    public String getLogUserMajor() {
        return logUserMajor;
    }

    public void setLogUserMajor(String logUserMajor) {
        this.logUserMajor = logUserMajor;
    }

    public String getLogUserClass() {
        return logUserClass;
    }

    public void setLogUserClass(String logUserClass) {
        this.logUserClass = logUserClass;
    }

    public boolean isLogIsAnonymity() {
        return logIsAnonymity;
    }

    public void setLogIsAnonymity(boolean logIsAnonymity) {
        this.logIsAnonymity = logIsAnonymity;
    }

    public List<Comment> getComments() {
        return comments;
    }

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
