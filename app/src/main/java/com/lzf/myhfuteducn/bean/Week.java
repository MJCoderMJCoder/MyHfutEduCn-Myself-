package com.lzf.myhfuteducn.bean;

import java.io.Serializable;

/**
 * 教务系统返回的某学期对应的周实体类
 *
 * @author MJCoder
 * @see java.io.Serializable
 */
public class Week implements Serializable {
    /**
     * 教务系统返回的某学期对应的周的开始时间
     */
    private String begin_on;
    /**
     * 教务系统返回的某学期对应的周的结束时间
     */
    private String end_on;
    /**
     * 教务系统返回的某学期对应的周的索引（可用于查询具体某周的所有课程表）
     */
    private int index;

    /**
     * 教务系统返回的某学期对应的周实体类的无参构造方法
     */
    public Week() {
    }

    /**
     * 教务系统返回的某学期对应的周实体类的有参构造方法
     *
     * @param begin_on 教务系统返回的某学期对应的周的开始时间
     * @param end_on   教务系统返回的某学期对应的周的结束时间
     * @param index    教务系统返回的某学期对应的周的索引（可用于查询具体某周的所有课程表）
     */
    public Week(String begin_on, String end_on, int index) {
        this.begin_on = begin_on;
        this.end_on = end_on;
        this.index = index;
    }

    /**
     * 获取教务系统返回的某学期对应的周的开始时间
     *
     * @return 教务系统返回的某学期对应的周的开始时间
     */
    public String getBegin_on() {
        return begin_on;
    }

    /**
     * 设置教务系统返回的某学期对应的周的开始时间
     *
     * @param begin_on 教务系统返回的某学期对应的周的开始时间
     */
    public void setBegin_on(String begin_on) {
        this.begin_on = begin_on;
    }

    /**
     * 获取教务系统返回的某学期对应的周的结束时间
     *
     * @return 教务系统返回的某学期对应的周的结束时间
     */
    public String getEnd_on() {
        return end_on;
    }

    /**
     * 设置教务系统返回的某学期对应的周的结束时间
     *
     * @param end_on 教务系统返回的某学期对应的周的结束时间
     */
    public void setEnd_on(String end_on) {
        this.end_on = end_on;
    }

    /**
     * 获取教务系统返回的某学期对应的周的索引（可用于查询具体某周的所有课程表）
     *
     * @return 教务系统返回的某学期对应的周的索引（可用于查询具体某周的所有课程表）
     */
    public int getIndex() {
        return index;
    }

    /**
     * 设置教务系统返回的某学期对应的周的索引（可用于查询具体某周的所有课程表）
     *
     * @param index 教务系统返回的某学期对应的周的索引（可用于查询具体某周的所有课程表）
     */
    public void setIndex(int index) {
        this.index = index;
    }


    @Override
    public String toString() {
        return "Week{" +
                "begin_on='" + begin_on + '\'' +
                ", end_on='" + end_on + '\'' +
                ", index=" + index +
                '}';
    }
}
