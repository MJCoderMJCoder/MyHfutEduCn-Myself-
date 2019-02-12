package com.lzf.myhfuteducn.bean;

/**
 * Created by MJCoder on 2019-02-11.
 */

public class Week {
    private String begin_on;
    private String end_on;
    private int index;

    public Week(String begin_on, String end_on, int index) {
        this.begin_on = begin_on;
        this.end_on = end_on;
        this.index = index;
    }

    public String getBegin_on() {
        return begin_on;
    }

    public void setBegin_on(String begin_on) {
        this.begin_on = begin_on;
    }

    public String getEnd_on() {
        return end_on;
    }

    public void setEnd_on(String end_on) {
        this.end_on = end_on;
    }

    public int getIndex() {
        return index;
    }

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
