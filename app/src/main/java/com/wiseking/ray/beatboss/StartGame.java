package com.wiseking.ray.beatboss;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wiseking.ray.beatboss.db.DataHistory;
import com.wiseking.ray.beatboss.util.DotComBush;
import com.wiseking.ray.beatboss.util.DrawTextImageView;
import com.wiseking.ray.beatboss.util.GetBitmapUtils;
import com.wiseking.ray.beatboss.util.MediaPlayUtils;
import com.wiseking.ray.beatboss.util.SoundPlayUtils;
import com.wiseking.ray.beatboss.util.ToastHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/11/27.
 */

public class StartGame extends AppCompatActivity implements View.OnClickListener{

    public DotComBush mGame;              //声明一个全局实例
    private boolean finishGame=false;   //初始设定未完成game
    private int totalblocks=9;          //设定初始总块数为9
    private int mtime = 0;              //设定初始时间为0秒
    private int mFinalScore=100;       //设定初始分数为100
    String mDate="";                    //设定初设时间为空
    private MyThread myThread;          //声明一个线程类实例
    private boolean isMute=false;    //设定是不是静音
    private GridView gv;
    private TextView countNum;
    private TextView countTime;
    private TextView leftNum;
    private TextView finalScore;
    private MyAdapter myAdapter;
    private TextView titleText;
    private MenuItem menuItem=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grid);

       /* //读取设定值
        SharedPreferences settings = getSharedPreferences("setting", 0);
//        isRemind= settings.getBoolean("isremind",false);
        isMute= settings.getBoolean("ismute",false);
//        selectedLanguage = settings.getInt("selectedLanguage", 0);*/

        //初始化音效类
//        SoundPlayUtils.init(this);

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

        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText("游戏进行中……");

        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        GetBitmapUtils.screenShot(StartGame.this, true);
                        GetBitmapUtils.showShare(StartGame.this,"游戏界面分享","我发现了一个小游戏，这是游戏界面。");
                        break;
                    case R.id.action_play:
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        if (!finishGame){
                            if (!myThread.pause)
                            {
                                item.setIcon(R.mipmap.ic_play);
                                myThread.pause=true;
                                titleText.setText("游戏暂停中……");
                            }else {
                                    item.setIcon(R.mipmap.ic_pause);
                                    myThread.pause=false;
                                    titleText.setText("游戏进行中……");
                            }
                        }else
                        {
                            ToastHelper.showToast("游戏都结束了，就不要再按暂停了吧！");
                        }
                        break;
                }
                return true;
            }
        });

        gv = (GridView)findViewById(R.id.GridView1);
        Button restartGame=(Button) findViewById(R.id.restartGame);
        Button restartPosition=(Button)findViewById(R.id.resetPosition);
        countNum = (TextView) findViewById(R.id.countNum);
        countTime = (TextView)findViewById(R.id.countTime);
        leftNum = (TextView)findViewById(R.id.leftNum);
        finalScore =(TextView) findViewById(R.id.finalScore);

        //接收Intent传过来DotComBush的对象实例
        Intent intent= this.getIntent();
        mGame=(DotComBush) intent.getSerializableExtra("game");
        myThread=new MyThread();  //开启计时器线程
        myThread.start();
        //为GridView设置适配器
        myAdapter=new MyAdapter(this);
        gv.setAdapter(myAdapter);

        //设置是不是静音
        isMute=mGame.isMute;
        //按钮注册监听
        restartGame.setOnClickListener(this);
        restartPosition.setOnClickListener(this);



        //注册GridView监听事件
        gv.setOnItemClickListener(new OnItemClickListener()
        {
            int numOfClickAfterFinish=0;
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                //游戏结束点击无效
                if (finishGame){
                    if (numOfClickAfterFinish==0){
                        ToastHelper.showToast("敬告：游戏结束后再次点击无效哦~~");
                    }else if (numOfClickAfterFinish==1){
                        showAlertDialog();
                    }
                    numOfClickAfterFinish++;
                    return;
                }
                DrawTextImageView view= (DrawTextImageView) v;  //转换为DrawTextImageView
                String result = mGame.checkUserInput(position); //获得结果
                if (myThread.pause){                           //如果暂停就不再计数
                    return;
                }
                //如果游戏未结束就更新步数
                if (!finishGame){
                    String countNametext=String.format("已用步数:%d步",mGame.NumOfGuesses+1);
                    countNum.setText(countNametext);

                 //创建淡入淡出动画
                //创建一个AnimationSet对象，参数为Boolean型，
                //true表示使用Animation的interpolator，false则是使用自己的
                AnimationSet animationSet = new AnimationSet(true);
                //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                //设置动画执行的时间
                alphaAnimation.setDuration(180);
                //将alphaAnimation对象添加到AnimationSet当中
                animationSet.addAnimation(alphaAnimation);
                //使用ImageView的startAnimation方法执行动画
                view.startAnimation(animationSet);

                }

                //每点击一次扣除1分
                mFinalScore--;

//                String result = mGame.checkUserInput(position);
                if (result.equals("hit") || result.equals("kill")){
                    String leftNametext="还剩未中:";
                    StringBuilder builder1=new StringBuilder(leftNametext);
                    int mPosition= mGame.dotComToStore.locNum;             //取得命中的名字的编号
                    char hanzi=mGame.dotComToStore.name.charAt(mPosition); //取得该汉字
                    String mHanzi=String.valueOf(hanzi);                      //char转换成String
                    view.setDrawTextSize(80.0f);                             //设置字体大小
                    view.setDrawText(mHanzi);                                 //绘制汉字
                    view.setImageResource(R.mipmap.pic_bomb);    //设定图片为爆炸图标
                    if (result.equals("hit")){                                   //如果命中
                        if (mGame.dotComToStore.hitAgain){                     //如果是重复点击
                            ToastHelper.showToast("哦噢~~“"+hanzi+"”这个字你已经打过了哦，重复点击会扣分的哦~");
                            if (!isMute){
                                SoundPlayUtils.play(2);  //播放重复音效
                            }
                        }else {
//                            Toast.makeText(StartGame.this, "恭喜你击中了"+"“"+hanzi+"”字，棒棒哒~~", Toast.LENGTH_SHORT).show();
                            if (!isMute) {
                                SoundPlayUtils.play(3);  //播放命中音效
                            }
                            //更新块数
                            totalblocks--;
                            builder1.append(totalblocks).append("块");
                            leftNum.setText(builder1);
                            //每命中一次增加5分
                            mFinalScore+=5;
                        }
                    }
                    if (result.equals("kill")){                                // 如果全部命中
                        String mName= mGame.dotComToStore.name;              //取得老板的姓名
                        if (mGame.dotComToStore.hitAgain){                  //如果是重复点击
                            ToastHelper.showToast("哦噢~~“"+mName+"”这个“老板”已经被你成功消灭了哦，重复点击会扣分的哦~");
                            if (!isMute) {
                                SoundPlayUtils.play(2);  //播放重复音效
                            }
                        }else {
                            //更新块数
                            totalblocks--;
                            builder1.append(totalblocks).append("块");
                            leftNum.setText(builder1);
                            if (!isMute) {
                                SoundPlayUtils.play(4);  //播放消灭音效
                            }
                            //每命中一次增加5分
                            mFinalScore+=5;
                            if (mGame.dotComHit==3){           //如果全部消灭
                                titleText.setText("游戏结束");
                                ToastHelper.showToast("恭喜你成功消灭了所有“老板”，过关啦~~");
                                if (!isMute) {
                                    SoundPlayUtils.play(5);  //播放胜利音效
                                }
                                myThread.stop=true;              //设置定时器停止标志
                                myThread=null;                   //线程指向空
                                finishGame = true;              //game 完成
                                //存储游戏结束时的时间
                                Date date = new Date();                // 获取一个Date对象
                                SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA); //获得格式化实例
                                mDate = f.format(date);                 //将Date对象格式化为String
                                //使用数据库保存为历史成绩
                                DataHistory score= new DataHistory();    //创建实例
                                score.setCount(mGame.NumOfGuesses+1);  //存储步数到数据库
                                score.setDate(mDate);                   //存储日期到数据库
                                score.setTime(mtime);                  //存储时间到数据库
                                score.setScore(mFinalScore-1);        //存储成绩到数据库时成绩多记一分，故减去1
                                score.save();                          //执行保存
                            }
                            else {
                                ToastHelper.showToast("恭喜你成功消灭了"+"“"+mName+"”这个“老板”，棒棒哒~~");
                            }
                        }
                    }
                }else {
                    if (!isMute) {
                        SoundPlayUtils.play(2);  //播放未命中音效
                    }
                }
//                Toast.makeText(StartGame.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //按钮的监听响应
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.restartGame :
                if (!isMute) {
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                //老板名字不变，位置重新布置,首先取得当前的名字
                String name1 = null;
                String name2 = null;
                String name3 = null;
                name1=mGame.dotComsList.get(0).name;
                name2=mGame.dotComsList.get(1).name;
                name3=mGame.dotComsList.get(2).name;
                //新建对象
                mGame=new DotComBush();
                mGame.SetUpGame(name1,name2,name3);
                //重新为GridView设置适配器
                myAdapter=new MyAdapter(this);
                gv.setAdapter(myAdapter);

                //重新开启计时器线程
                mtime=0;                   //时间显示重置为0
                if (myThread!=null)       //若定时器的子线程还存在则执行以下操作
                {
                    myThread.stop=true;              //设置定时器停止标志
                    myThread=null;                   //线程指向空
                }
                myThread=new MyThread();
                myThread.start();

                //设置菜单中图片和标题文字
                menuItem.setIcon(R.mipmap.ic_pause);
                myThread.pause=false;
                titleText.setText("游戏进行中……");

                //重置未完成game的标志
                finishGame=false;
                //还剩的块数需要重置为9
                totalblocks=9;
                //综合分数重置为100
                mFinalScore=100;
                //设定初设时间为空
                mDate="";

                //更新TextView
                countNum.setText("已用步数:0步");
                leftNum.setText("还剩未中:9块");
                countTime.setText("已用时间:0秒");
                finalScore.setText("综合评分:100分");
                break;
            case  R.id.resetPosition :
                if (!isMute) {
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                //利用Intent将游戏结束和最终结果传送到历史成绩页面
                Intent intent1=new Intent(StartGame.this,CheckHistory.class);
                intent1.putExtra("Extra_finishGame", finishGame);
                intent1.putExtra("Extra_date",mDate );
                startActivity(intent1);
                break;
            default:
                break;
        }

    }

    // handler类接收数据
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String mcountTime=String.format("已用时间:%d秒",mtime++);
                countTime.setText(mcountTime);
                mFinalScore--;
                String mfinalScore=String.format("综合评分:%d分",mFinalScore);
                finalScore.setText(mfinalScore);
//                countTime.setText(Integer.toString(i++));
//                System.out.println("receive....");
            }
        }
    };

    // 线程类
    class MyThread extends Thread {

        public boolean stop=false;
        public boolean pause=false;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!stop) {
                if (!pause){
                    try {
                        Thread.sleep(1000);
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
//                    System.out.println("send...");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
//                    System.out.println("thread error...");
                    }
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //读取设定值
        SharedPreferences settings = getSharedPreferences("setting", 0);
//        isRemind= settings.getBoolean("isremind",false);
        isMute= settings.getBoolean("ismute",false);
//        selectedLanguage = settings.getInt("selectedLanguage", 0);
        //通过AudioManager来设置了系统声音的静音,进入本游戏直接将系统声音静音
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        // 设定调整音量为媒体音量,当暂停播放的时候调整音量就不会再默认调整铃声音量了，
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM,true);
        if (isMute)
        {
            MediaPlayUtils.pause();
        }else {
            MediaPlayUtils.play();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menuItem = menu.findItem(R.id.action_play);
        return true;
    }

    public void showAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(StartGame.this)
                .setTitle("警告")
                .setMessage("都说了不要再点了，再点就不理你了")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
//自定义适配器
class MyAdapter extends BaseAdapter{
    //上下文对象
    private Context context;
    //设置图片选中前为-1
    private int selectPic = -1;
    //设置是否为击中
    private String result="";

    //图片数组
    private Integer[] imgs = {
            R.mipmap.pic1, R.mipmap.pic2,R.mipmap.pic3,
            R.mipmap.pic4, R.mipmap.pic5, R.mipmap.pic6,
            R.mipmap.pic7, R.mipmap.pic8, R.mipmap.pic9,
           R.mipmap.pic10, R.mipmap.pic11, R.mipmap.pic12,
            R.mipmap.pic13, R.mipmap.pic14, R.mipmap.pic15,
            R.mipmap.pic16, R.mipmap.pic17,R.mipmap.pic18,
            R.mipmap.pic19, R.mipmap.pic20,R.mipmap.pic21,
            R.mipmap.pic22, R.mipmap.pic23,R.mipmap.pic24,
            R.mipmap.pic25, R.mipmap.pic26,R.mipmap.pic27,
            R.mipmap.pic28, R.mipmap.pic29,R.mipmap.pic30,
            R.mipmap.pic31, R.mipmap.pic32,R.mipmap.pic33,
            R.mipmap.pic34, R.mipmap.pic35,R.mipmap.pic36,
            R.mipmap.pic37, R.mipmap.pic38,R.mipmap.pic39,
            R.mipmap.pic40, R.mipmap.pic41,R.mipmap.pic42,
            R.mipmap.pic43, R.mipmap.pic44,R.mipmap.pic45,
            R.mipmap.pic46, R.mipmap.pic47,R.mipmap.pic48,
            R.mipmap.pic49
    };
    MyAdapter(Context context){
        this.context = context;
    }
    public int getCount() {
        return imgs.length;
    }

    public Object getItem(int item) {
        return item;
    }

    public long getItemId(int id) {
        return id;
    }

    public void setNotifyDataChange(int id,String result) {
        selectPic = id;
        this.result=result;
        super.notifyDataSetChanged();
    }

    //创建View方法
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawTextImageView imageView;
        if (convertView == null) {
            imageView = new DrawTextImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(145, 145));//设置ImageView对象布局
            imageView.setAdjustViewBounds(false);//设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
            imageView.setPadding(7, 8, 7, 8);//设置间距
        }
        else {
            imageView = (DrawTextImageView) convertView;
        }
        imageView.setImageResource(imgs[position]);//为ImageView设置图片资源
        return imageView;
    }
}
