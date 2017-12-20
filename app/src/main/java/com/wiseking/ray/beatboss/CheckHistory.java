package com.wiseking.ray.beatboss;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.widget.ListView;

import com.wiseking.ray.beatboss.R;
import com.wiseking.ray.beatboss.db.DataHistory;
import com.wiseking.ray.beatboss.util.CommonAdapter;
import com.wiseking.ray.beatboss.util.Score;
import com.wiseking.ray.beatboss.util.ViewHolder;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/29.
 */

public class CheckHistory extends AppCompatActivity {

    private ListView mListView;                               //ListView实例
    private ArrayList<DataHistory> mDatas=new ArrayList<>();  //用于从数据库中取得所有数据
    private ArrayList<Score> mScore=new ArrayList<>();        //用于列表的适配器
    int mPosition=-1;       //没有进行游戏则默认位置设置为-1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        //读取数据库的历史成绩用来显示
        int numOfScore = DataSupport.count(DataHistory.class); //保存有多少历史成绩
        mDatas=(ArrayList<DataHistory>) DataSupport.findAll(DataHistory.class); //查询到所有数据放入List
        if (numOfScore>1){
            Collections.sort(mDatas);             //如果有两个历史成绩以上就排序
        }

        //从数据库取出数据后赋值给Score实体类
        for (DataHistory mDataHistory:mDatas){
            Score scoreSet=new Score();
            scoreSet.setDate(mDataHistory.getDate());
            scoreSet.setNum(mDataHistory.getCount());
            scoreSet.setTime(mDataHistory.getTime());
            scoreSet.setScore(mDataHistory.getScore());
            mScore.add(scoreSet);
        }

        //接受从游戏页面传来的数据，如果是游戏结束来看成绩就高亮显示此次成绩，
        // 若没有进行游戏则不高亮任何一个成绩
        Intent intent = getIntent();
        boolean flagFinishGame=intent.getBooleanExtra("Extra_finishGame",false);
        String lastDate=intent.getStringExtra("Extra_date");
        if (flagFinishGame){
            for (int i=0;i<mScore.size();i++){
                if ((mScore.get(i).getDate()).equals(lastDate)){
                    mPosition=i;
                    break;
                }
            }
        }

        Log.d("GML","mPostion at onCreateout "+mPosition);

        mListView = (ListView) findViewById(R.id.LV_history);


//        mPosition=50;
        //设置适配器
        mListView.setAdapter(new CommonAdapter<Score>(this, mScore, R.layout.item, mPosition)
        {
            @Override
            public void convert(ViewHolder helper, Score item)
            {
                helper.setText(R.id.tv_date, item.getDate());
                helper.setText(R.id.tv_num, item.getNum());
                helper.setText(R.id.tv_time, item.getTime());
                helper.setText(R.id.tv_score, item.getScore());

//              helper.getView(R.id.tv_title).setOnClickListener(l)
            }

        });

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPosition!=-1) {
                    int childCount=mListView.getChildCount();   //得到一屏幕最多可以显示的item的个数
                    mListView.requestFocusFromTouch();//获取焦点
                    mListView.setSelection(mListView.getHeaderViewsCount() + mPosition-childCount+2);//mPosition-childCount+2是需要定位的位置

                }
            }
        },500 );


        /*mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int firstVisiblePosition=mListView.getFirstVisiblePosition();
                Log.d("GML","firstPostion at click "+firstVisiblePosition);
                Log.d("GML","lastPosition at click "+mListView.getLastVisiblePosition());
                Log.d("GML","TotalchildCoung at click "+mListView.getCount());
                int childCount=mListView.getChildCount();
                mListView.requestFocusFromTouch();//获取焦点
                mListView.setSelection(mListView.getHeaderViewsCount() + mPosition-childCount+2);//mPosition是你需要定位的位置
                view = mListView.getChildAt(childCount-1);
                if (view!=null){
                    view.setBackgroundResource(R.drawable.boderyes);
//                                mPosition=-1;
                    Log.d("GML","index2 at post");
                }else {Log.d("GML","index2 at post failed");}

            }
        });*/
    }


    /**
     * 通过位置找到ListView中的某个item的View
     * @param pos
     * @param listView
     * @return
     */
    /*public View getViewByPosition(int pos, ListView listView) {
        int firstListItemPosition = listView.getFirstVisiblePosition();
        int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }*/


}
