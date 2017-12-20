package com.wiseking.ray.beatboss.util;

import java.util.*;
import java.io.Serializable;
/**
 * Created by Administrator on 2017/11/29.
 */

public class DotCom implements Serializable{
    public int [] locationCells;
    public String name;
    public int countNum=0;           //记录该DotCom中已经被找出的位置数
    public int locNum=0;             //命中时记录位置
    private int [] flag= new int[3]; //已经命中的设置标志
    public boolean hitAgain;       //重复点击已命中的设定为true

    public void setLocationCells(int [] loc){
        locationCells=loc;
    }

    public void setName(String n){  name=n;  }

    public String checkYourself(int userInput){
        String result="miss";                              //首先假定没有命中
        hitAgain=false;                                   //首先假定没有重复
        int temp=countNum;
      for (locNum=0;locNum<3;locNum++){                 //遍历数组
            if (locationCells[locNum]==userInput){      //如果命中
                if (flag[locNum]==0){                    //如果没有命中过
                    countNum++;                           //命中的位置的次数+1
                    flag[locNum]=1;                      //设置该位置已命中
                    break;
                }else {
                    hitAgain=true;                      //设置已命中过的标志
                    break;
                }
            }
        }
        if(countNum>temp || hitAgain){                  //如果命中则条件成立
           if (countNum==3){                            //命中次数为3则该DotCom已经全部命中
            result="kill";                               //返回“被打死”
            }else {
                result="hit";                            //否则返回命中
            }
        }//close if
            return result;
    }//close method
}//close class


