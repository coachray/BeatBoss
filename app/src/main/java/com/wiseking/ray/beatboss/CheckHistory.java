package com.wiseking.ray.beatboss;

import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

import android.widget.ListView;
import com.wiseking.ray.beatboss.db.DataHistory;
import com.wiseking.ray.beatboss.util.CommonAdapter;
import com.wiseking.ray.beatboss.util.Score;
import com.wiseking.ray.beatboss.util.SoundPlayUtils;
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
    private boolean isMute=false;  //是否静音

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);

        //读取设定值
        SharedPreferences settings = getSharedPreferences("setting", 0);
//        isRemind= settings.getBoolean("isremind",false);
        isMute= settings.getBoolean("ismute",false);
//        selectedLanguage = settings.getInt("selectedLanguage", 0);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                onBackPressed();
            }
        });

        setTitle("");
        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText("历史记录");

        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        Toast.makeText(CheckHistory.this,
                                "share !", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_clear:
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        showDeleteAlertDialog();
                        break;
                }
                return true;
            }
        });




        //读取数据库的历史成绩用来显示
        int numOfScore = DataSupport.count(DataHistory.class); //保存有多少历史成绩
        mDatas=(ArrayList<DataHistory>) DataSupport.findAll(DataHistory.class); //查询到所有数据放入List
        if (numOfScore>1){
            Collections.sort(mDatas);             //如果有两个历史成绩以上就排序
        }

        int j=1;          //为了给排名Chart赋值的临时变量
        //从数据库取出数据后赋值给Score实体类
        for (DataHistory mDataHistory:mDatas){
            Score scoreSet=new Score();
            scoreSet.setDate(mDataHistory.getDate());
            scoreSet.setNum(mDataHistory.getCount());
            scoreSet.setTime(mDataHistory.getTime());
            scoreSet.setScore(mDataHistory.getScore());
            mScore.add(scoreSet);
            scoreSet.setChart(j++);  //给Chart赋值
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

//        Log.d("GML","mPostion at onCreateout "+mPosition);

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
                helper.setText(R.id.tv_chart,item.getChart());

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar1, menu);
        return true;
    }

    public void showDeleteAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(CheckHistory.this)
                .setTitle("温馨提醒")
                .setMessage("       该操作将删除所有历史记录，确定要继续吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        DataSupport.deleteAll(DataHistory.class);
                        mScore.clear();
                        mListView.setAdapter(new CommonAdapter<Score>(CheckHistory.this, mScore, R.layout.item, mPosition)
                        {
                            @Override
                            public void convert(ViewHolder helper, Score item)
                            {
                                helper.setText(R.id.tv_date, item.getDate());
                                helper.setText(R.id.tv_num, item.getNum());
                                helper.setText(R.id.tv_time, item.getTime());
                                helper.setText(R.id.tv_score, item.getScore());
                                helper.setText(R.id.tv_chart,item.getChart());
                            }
                        });
                        dialog.dismiss();
                        Toast.makeText(CheckHistory.this,
                                "所有历史记录已删除 !", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

}
