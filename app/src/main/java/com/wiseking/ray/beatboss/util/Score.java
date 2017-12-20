package com.wiseking.ray.beatboss.util;

import java.util.Date;

/**
 * Created by guo on 2017/12/12.
 */

//创建成绩类
public class Score implements Comparable<Score>{
    private String date;
    private int num;
    private int time;
    private int score;

    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}

    public String getNum(){return Integer.toString(num);}
    public void setNum(int num){this.num=num;}

    public String getTime(){return Integer.toString(time);}
    public void setTime(int time){this.time=time;}

    public String getScore(){return Integer.toString(score);}
    public void setScore(int score){this.score=score;}

    // 实现排序必须要重写的方法
    @Override
    public int compareTo(Score o) {//按照密码的升序排列，想要按照名字排序就把password换成name就可以了
        return score-o.score;
    }

}
