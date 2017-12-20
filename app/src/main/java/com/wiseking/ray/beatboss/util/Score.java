package com.wiseking.ray.beatboss.util;

import java.util.Date;

/**
 * Created by guo on 2017/12/12.
 */

//创建成绩类
public class Score implements Comparable<Score>{
    private String date;  //日期
    private int num;     //步数
    private int time;    //用时
    private int score;   //成绩
    private int chart;  //排名

    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}

    public String getNum(){return Integer.toString(num);}
    public void setNum(int num){this.num=num;}

    public String getTime(){return Integer.toString(time);}
    public void setTime(int time){this.time=time;}

    public String getScore(){return Integer.toString(score);}
    public void setScore(int score){this.score=score;}

    public String getChart() {
        return Integer.toString(chart);
    }

    public void setChart(int chart) {
        this.chart = chart;
    }

    // 实现排序必须要重写的方法
    @Override
    public int compareTo(Score o) {//按照密码的升序排列，想要按照名字排序就把password换成name就可以了
        return score-o.score;
    }

}
