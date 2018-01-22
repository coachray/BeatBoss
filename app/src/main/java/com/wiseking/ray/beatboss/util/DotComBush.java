package com.wiseking.ray.beatboss.util;

import java.util.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/29.
 */

public class DotComBush implements Serializable{
    public GameHelper helper=new GameHelper();
    public ArrayList<DotCom> dotComsList=new ArrayList<DotCom>();
//    public ArrayList<DotCom> dotComsListRun=new ArrayList<DotCom>();
    public  int dotComHit=0;                                                   //存放已消灭的老板个数
    public int NumOfGuesses=0;                                                //猜测的总次数
    public DotCom dotComToStore;                                              //用于存放在命中时是哪一个DotCom
    public boolean isMute=false;                                             //用于存放是否静音

    public void SetUpGame(String name1,String name2,String name3){
        //creat three DotCom Objects and give them names and locations
        DotCom one=new DotCom();
        one.setName(name1);
        DotCom two=new DotCom();
        two.setName(name2);
        DotCom three=new DotCom();
        three.setName(name3);
        dotComsList.add(one);
        dotComsList.add(two);
        dotComsList.add(three);

        setLocation();

    }//end setupgame

    public void setLocation(){
        for(DotCom dotComToSet:dotComsList){
            int [] newLocation= helper.placeDotCom(3);
            dotComToSet.setLocationCells(newLocation);
        }//end for
    }

    public String checkUserInput(int userGuess){
        NumOfGuesses++;
        String result="miss";

        for (DotCom dotComToTest : dotComsList) {
            result=dotComToTest.checkYourself(userGuess);
            if(result.equals("hit")){
                dotComToStore=dotComToTest;
                break;
            }
            if(result.equals("kill")){
                dotComToStore=dotComToTest;
                dotComHit++;
                //取消下一行代码是为了设置即使全部命中也不移除该对象
//                dotComsList.remove(dotComToTest);
                break;
            }
        }//close for
        return result;
    }

}
