package com.wiseking.ray.beatboss.db;

import org.litepal.crud.DataSupport;

import java.util.Date;


/**
 * Created by Administrator on 2017/11/26.
 */

public class DataHistory extends DataSupport implements Comparable<DataHistory>{
    private int id;
    private String date;
    private int count;
    private int time;
    private int score;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getDate(){return date;}

    public void setDate(String date){this.date =date; }

    public int getCount(){
        return count;
    }

    public void setCount(int count){
        this.count=count;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int time){
        this.time=time;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score=score;
    }

    // 实现排序必须要重写的方法
    @Override
    public int compareTo(DataHistory o) {//按照密码的升序排列，想要按照名字排序就把password换成name就可以了
        return o.score-score;
    }
}
