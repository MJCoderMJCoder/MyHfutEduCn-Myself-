package com.lzf.myhfuteducn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MJCoder on 2019-02-12.
 */

public class Lesson implements Serializable {

    private String code;
    private String course_code;
    private String course_credit;
    private String course_gp;
    private String course_name;
    private List<Examgrade> exam_grades;
    private boolean passed;
    private String score;
    private String score_text;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCourse_credit() {
        return course_credit;
    }

    public void setCourse_credit(String course_credit) {
        this.course_credit = course_credit;
    }

    public String getCourse_gp() {
        return course_gp;
    }

    public void setCourse_gp(String course_gp) {
        this.course_gp = course_gp;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public List<Examgrade> getExam_grades() {
        return exam_grades;
    }

    public void setExam_grades(List<Examgrade> exam_grades) {
        this.exam_grades = exam_grades;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore_text() {
        return score_text;
    }

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

    public class Examgrade {
        private String passed;
        private String score;
        private String score_text;
        private String state;
        private String type;

        public Examgrade(String passed, String score, String score_text, String state, String type) {
            this.passed = passed;
            this.score = score;
            this.score_text = score_text;
            this.state = state;
            this.type = type;
        }

        public String getPassed() {
            return passed;
        }

        public void setPassed(String passed) {
            this.passed = passed;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getScore_text() {
            return score_text;
        }

        public void setScore_text(String score_text) {
            this.score_text = score_text;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

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
