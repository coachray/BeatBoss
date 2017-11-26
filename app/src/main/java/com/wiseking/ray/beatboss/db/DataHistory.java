package com.wiseking.ray.beatboss.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/26.
 */

public class DataHistory extends DataSupport {
    private int id;
    private int count;
    private int time;
    private int score;

    public int getId(){
        return id;
    }

    public void setId(){
        this.id=id;
    }

    public int getCount(){
        return count;
    }

    public void setCount(){
        this.count=count;
    }

    public int getTime(){
        return time;
    }

    public void setTime(){
        this.time=time;
    }

    public int getScore(){
        return score;
    }

    public void setScore(){
        this.score=score;
    }
}
