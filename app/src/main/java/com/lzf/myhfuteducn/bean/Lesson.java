package com.lzf.myhfuteducn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 教务系统返回的课程实体类
 *
 * @author MJCoder
 * @see java.io.Serializable
 */
public class Lesson implements Serializable {
    /**
     * 教务系统返回的该课程代码的完整编号
     */
    private String code;
    /**
     * 教务系统返回的该课程代码的简略编号
     */
    private String course_code;
    /**
     * 教务系统返回的该课程的学分
     */
    private String course_credit;
    /**
     * 教务系统返回的该课程的绩点
     */
    private String course_gp;
    /**
     * 教务系统返回的该课程的名称
     */
    private String course_name;
    /**
     * 教务系统返回的该课程相关的一系列考试成绩
     */
    private List<Examgrade> exam_grades;
    /**
     * 教务系统返回的该课程是否通过考试
     */
    private boolean passed;
    /**
     * 教务系统返回的该课程的总分数
     */
    private String score;
    /**
     * 教务系统返回的该课程的总分数对应的文本评价
     */
    private String score_text;

    /**
     * 课程实体类的无参构造方法
     */
    public Lesson() {
    }

    /**
     * 课程实体类的有参构造方法
     *
     * @param code          教务系统返回的该课程代码的完整编号
     * @param course_code   教务系统返回的该课程代码的简略编号
     * @param course_credit 教务系统返回的该课程的学分
     * @param course_gp     教务系统返回的该课程的绩点
     * @param course_name   教务系统返回的该课程的名称
     * @param exam_grades   教务系统返回的该课程相关的一系列考试成绩
     * @param passed        教务系统返回的该课程是否通过考试
     * @param score         教务系统返回的该课程的总分数
     * @param score_text    教务系统返回的该课程的总分数对应的文本评价
     */
    public Lesson(String code, String course_code, String course_credit, String course_gp, String course_name, List<Examgrade> exam_grades, boolean passed, String score, String score_text) {
        this.code = code;
        this.course_code = course_code;
        this.course_credit = course_credit;
        this.course_gp = course_gp;
        this.course_name = course_name;
        this.exam_grades = exam_grades;
        this.passed = passed;
        this.score = score;
        this.score_text = score_text;
    }

    /**
     * 获取教务系统返回的该课程代码的完整编号
     *
     * @return 教务系统返回的该课程代码的完整编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置教务系统返回的该课程代码的完整编号
     *
     * @param code 教务系统返回的该课程代码的完整编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取教务系统返回的该课程代码的简略编号
     *
     * @return 教务系统返回的该课程代码的简略编号
     */
    public String getCourse_code() {
        return course_code;
    }

    /**
     * 设置教务系统返回的该课程代码的简略编号
     *
     * @param course_code 教务系统返回的该课程代码的简略编号
     */
    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    /**
     * 获取教务系统返回的该课程的学分
     *
     * @return 教务系统返回的该课程的学分
     */
    public String getCourse_credit() {
        return course_credit;
    }

    /**
     * 设置教务系统返回的该课程的学分
     *
     * @param course_credit 教务系统返回的该课程的学分
     */
    public void setCourse_credit(String course_credit) {
        this.course_credit = course_credit;
    }

    /**
     * 获取教务系统返回的该课程的绩点
     *
     * @return 教务系统返回的该课程的绩点
     */
    public String getCourse_gp() {
        return course_gp;
    }

    /**
     * 设置教务系统返回的该课程的绩点
     *
     * @param course_gp 教务系统返回的该课程的绩点
     */
    public void setCourse_gp(String course_gp) {
        this.course_gp = course_gp;
    }

    /**
     * 获取教务系统返回的该课程的名称
     *
     * @return 教务系统返回的该课程的名称
     */
    public String getCourse_name() {
        return course_name;
    }

    /**
     * 设置教务系统返回的该课程的名称
     *
     * @param course_name 教务系统返回的该课程的名称
     */
    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    /**
     * 获取教务系统返回的该课程相关的一系列考试成绩
     *
     * @return 教务系统返回的该课程相关的一系列考试成绩
     */
    public List<Examgrade> getExam_grades() {
        return exam_grades;
    }

    /**
     * 设置教务系统返回的该课程相关的一系列考试成绩
     *
     * @param exam_grades 教务系统返回的该课程相关的一系列考试成绩
     */
    public void setExam_grades(List<Examgrade> exam_grades) {
        this.exam_grades = exam_grades;
    }

    /**
     * 获取教务系统返回的该课程是否通过考试
     *
     * @return 教务系统返回的该课程是否通过考试（true：通过；false：挂科）
     */
    public boolean isPassed() {
        return passed;
    }

    /**
     * 设置教务系统返回的该课程是否通过考试
     *
     * @param passed 教务系统返回的该课程是否通过考试
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    /**
     * 获取教务系统返回的该课程的总分数
     *
     * @return 教务系统返回的该课程的总分数
     */
    public String getScore() {
        return score;
    }

    /**
     * 设置教务系统返回的该课程的总分数
     *
     * @param score 教务系统返回的该课程的总分数
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * 获取教务系统返回的该课程的总分数对应的文本评价
     *
     * @return 教务系统返回的该课程的总分数对应的文本评价
     */
    public String getScore_text() {
        return score_text;
    }

    /**
     * 设置教务系统返回的该课程的总分数对应的文本评价
     *
     * @param score_text 教务系统返回的该课程的总分数对应的文本评价
     */
    public void setScore_text(String score_text) {
        this.score_text = score_text;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "code='" + code + '\'' +
                ", course_code='" + course_code + '\'' +
                ", course_credit='" + course_credit + '\'' +
                ", course_gp='" + course_gp + '\'' +
                ", course_name='" + course_name + '\'' +
                ", exam_grades=" + exam_grades +
                ", passed=" + passed +
                ", score='" + score + '\'' +
                ", score_text='" + score_text + '\'' +
                '}';
    }

    /**
     * 教务系统返回的课程相关的考试成绩
     *
     * @author MJCoder
     * @see java.io.Serializable
     */
    public class Examgrade implements Serializable {
        /**
         * 教务系统返回的该成绩是否及格
         */
        private String passed;
        /**
         * 教务系统返回的该成绩的具体分数
         */
        private String score;
        /**
         * 教务系统返回的该成绩的具体分数对应的文本评价
         */
        private String score_text;
        /**
         * 教务系统返回的该成绩的状态：正常？
         */
        private String state;
        /**
         * 教务系统返回的该成绩对应的类型
         */
        private String type;

        /**
         * 教务系统返回的课程相关的考试成绩的无参构造方法
         */
        public Examgrade() {
        }

        /**
         * 教务系统返回的课程相关的考试成绩的有参构造方法
         *
         * @param passed     教务系统返回的该成绩是否及格
         * @param score      教务系统返回的该成绩的具体分数
         * @param score_text 教务系统返回的该成绩的具体分数对应的文本评价
         * @param state      教务系统返回的该成绩的状态：正常？
         * @param type       教务系统返回的该成绩对应的类型
         */
        public Examgrade(String passed, String score, String score_text, String state, String type) {
            this.passed = passed;
            this.score = score;
            this.score_text = score_text;
            this.state = state;
            this.type = type;
        }

        /**
         * 获取教务系统返回的该成绩是否及格
         *
         * @return 教务系统返回的该成绩是否及格
         */
        public String getPassed() {
            return passed;
        }

        /**
         * 设置教务系统返回的该成绩是否及格
         *
         * @param passed 教务系统返回的该成绩是否及格
         */
        public void setPassed(String passed) {
            this.passed = passed;
        }

        /**
         * 获取教务系统返回的该成绩的具体分数
         *
         * @return 教务系统返回的该成绩的具体分数
         */
        public String getScore() {
            return score;
        }

        /**
         * 设置教务系统返回的该成绩的具体分数
         *
         * @param score 教务系统返回的该成绩的具体分数
         */
        public void setScore(String score) {
            this.score = score;
        }

        /**
         * 获取教务系统返回的该成绩的具体分数对应的文本评价
         *
         * @return 教务系统返回的该成绩的具体分数对应的文本评价
         */
        public String getScore_text() {
            return score_text;
        }

        /**
         * 设置教务系统返回的该成绩的具体分数对应的文本评价
         *
         * @param score_text 教务系统返回的该成绩的具体分数对应的文本评价
         */
        public void setScore_text(String score_text) {
            this.score_text = score_text;
        }

        /**
         * 获取教务系统返回的该成绩的状态：正常？
         *
         * @return 教务系统返回的该成绩的状态：正常？
         */
        public String getState() {
            return state;
        }

        /**
         * 设置教务系统返回的该成绩的状态：正常？
         *
         * @param state 教务系统返回的该成绩的状态：正常？
         */
        public void setState(String state) {
            this.state = state;
        }

        /**
         * 获取教务系统返回的该成绩对应的类型
         *
         * @return 教务系统返回的该成绩对应的类型
         */
        public String getType() {
            return type;
        }

        /**
         * 设置教务系统返回的该成绩对应的类型
         *
         * @param type 教务系统返回的该成绩对应的类型
         */
        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Examgrade{" +
                    "passed='" + passed + '\'' +
                    ", score='" + score + '\'' +
                    ", score_text='" + score_text + '\'' +
                    ", state='" + state + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
