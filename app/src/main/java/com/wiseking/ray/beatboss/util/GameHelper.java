package com.wiseking.ray.beatboss.util;

import java.util.*;
import java.io.Serializable;
/**
 * Created by Administrator on 2017/11/29.
 */

public class GameHelper implements Serializable{

    public int comCount =0;                     //处理的是第几个DotCom
    public int gridLength=7;                   //每行7列
    public int gridSize=49;                    //一共49个位置
    public int [] grid =new int[gridSize];   //建立int 数组

    /**
     * 设置DotCom
     * @param comSize　占用的格子数
     * @return　　　返回int []
     */

    public int [] placeDotCom(int comSize){
        int [] coords= new int[comSize];
        int attempts=0;
        boolean success =false;
        int location =0;

        comCount++;
        int incr=1;                                     //水平增量为1
        int random=(int) (Math.random()*2);            //取得0或1的随机数
        if((comCount % 2)==random) {                  //随机设置是奇偶个DotCom时
            incr = gridLength;                        //就垂直放置
        }

        while (!success & attempts++ <200){              //主要搜索循环
            location= (int) (Math.random() * gridSize); //随机起点
            int x=0;                                     //DotCom中的三个第n个位置
            success = true;                              //假定成功
            while (success && x< comSize){               //查找未使用的点
                if(grid[location] == 0){                 //如果该点还未使用
                  coords[x++]= location;                  //就储存该位置
                  location+=incr;                         //尝试下一点
                  if(location >=gridSize){              //超出下边缘
                      success = false;                   //失败
                  }
                  if(x>0 && (location % gridLength == 0)){ //超出右边缘
                      success = false;                      //失败
                  }
                }else {                                     //找到的是已经使用过的位置
                    success =false;                         //失败
                }
            }
        }                                                    //while结束，该DotCom位置已全部找好

        for (int x=0;x<comSize;x++){
            grid[coords[x]]=1;              //将临时位置coords对应的位置处的grid数值赋值为1
        }
        return  coords;
    }


}
