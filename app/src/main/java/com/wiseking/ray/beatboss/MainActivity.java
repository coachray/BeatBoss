package com.wiseking.ray.beatboss;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wiseking.ray.beatboss.util.DotComBush;
import com.wiseking.ray.beatboss.util.MediaPlayUtils;
import com.wiseking.ray.beatboss.util.SoundPlayUtils;

import java.util.Calendar;
import java.util.TimeZone;

import static android.media.MediaPlayer.create;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DotComBush game;
    private boolean isMute=false;  //是否静音
    int numOfToastRemind=0; //打开应用时，

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

        numOfToastRemind=0; //打开应用时，重置为0


        /*//取得改变系统设置的权限（不知道为什么没有提示授权，待解决）已经不需要这个授权了
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_SETTINGS)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_SETTINGS);
        }
        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }*/


        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        Toast.makeText(MainActivity.this,
                                "share !", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        showSettingLayout();
                        break;
                }
                return true;
            }
        });


        EditText text1= (EditText) findViewById(R.id.boss1);
        text1.setSelection(text1.getText().length());      //光标移动到编辑框的行末
        Button button_startgame = (Button) findViewById(R.id.startGame);
        Button button_checkhistory = (Button) findViewById(R.id.checkHistory);
        button_startgame.setOnClickListener(this);
        button_checkhistory.setOnClickListener(this);

        //初始化音效类
        SoundPlayUtils.init(this);
//        //初始化媒体类
        MediaPlayUtils.init(this);

    }
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case  R.id.startGame :
                        if (!isMute){                   //不静音就播放音效
                        SoundPlayUtils.play(1);  //播放按键音效
                        }
                        EditText text1= (EditText) findViewById(R.id.boss1);
                        text1.setSelection(text1.getText().length());
                        EditText text2= (EditText) findViewById(R.id.boss2);
                        EditText text3= (EditText) findViewById(R.id.boss3);
                        String name1=text1.getText().toString();
                        String name2=text2.getText().toString();
                        String name3=text3.getText().toString();

                        if(name1.length()!=3 || name2.length()!=3 || name3.length()!=3 ){
                            showAlertDialog();
                            return;
                        }
                        //创建DotComBush类实例
                        game= new DotComBush();
                        game.SetUpGame(name1,name2,name3);
                        game.isMute=isMute;
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,StartGame.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("game",game);
                    intent.putExtras(bundle);
                    this.startActivity(intent);
                    break;
                    case  R.id.checkHistory :
                        if (!isMute){                   //不静音就播放音效
                            SoundPlayUtils.play(1);  //播放按键音效
                        }
                        Intent intent1=new Intent(MainActivity.this,CheckHistory.class);
                        startActivity(intent1);
                        break;
                        default:
                            break;
                }

            }

    @Override
    protected void onResume() {
        super.onResume();
        //读取设定值
        SharedPreferences settings = getSharedPreferences("setting", 0);
        boolean isRemind= settings.getBoolean("isremind",false);
        isMute= settings.getBoolean("ismute",false);
//        selectedLanguage = settings.getInt("selectedLanguage", 0);

        if (numOfToastRemind==0){  //设定只在首次打开应用时起作用
            //播放背景音乐
            if (isMute)
            {
                MediaPlayUtils.stop();
            }else {
                MediaPlayUtils.play();
            }
            //如果设定开启通知且是首次打开应用就注册定时通知
            if (isRemind){
                startRemind();
            }else {
                stopRemind();
            }
            numOfToastRemind++;
        }


        //通过AudioManager来设置了系统声音的静音,进入本游戏直接将系统声音静音
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        // 设定调整音量为媒体音量,当暂停播放的时候调整音量就不会再默认调整铃声音量了，
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM,true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时释放音频资源
        MediaPlayUtils.stop();


        //通过AudioManager来设置了系统声音的静音,进入本游戏直接将系统声音静音
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        // 重新恢复设定默认调整音量为铃声音量
        setVolumeControlStream(AudioManager.STREAM_SYSTEM);
        mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    public void showAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("温馨提示")
                .setMessage("请输入三个中文或三个英文字母，两个字的中文名请中间加空格！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showSettingLayout(){
        Intent intent2=new Intent();
        intent2.setClass(MainActivity.this,SettingActivity.class);
        startActivity(intent2);
    }

    /*public void startRemindOnce(){
        // 进行闹铃注册
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        // 过10s 执行这个闹铃
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }*/

    /**
     * 开启提醒
     */
    public void startRemind(){

        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar = Calendar.getInstance();
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为13点
        mCalendar.set(Calendar.HOUR_OF_DAY, 12);
        //设置在几分提醒  设置的为25分
        mCalendar.set(Calendar.MINUTE, 30);
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        //上面设置的就是12点30分的时间点

        //获取上面设置的12点30分的毫秒值
        long selectTime = mCalendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);

        /**
         * 重复提醒
         * 第一个参数是警报类型；下面有介绍
         * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，但是我用的刷了MIUI的三星手机的实际效果是与单次提醒的参数一样，即设置的13点25分的时间点毫秒值
         * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
         */
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
//        Toast.makeText(MainActivity.this,"提醒通知功能处于开启状态! ", Toast.LENGTH_SHORT).show();

    }

    /**
     * 关闭提醒
     */
    public void stopRemind(){

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0,
                intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
//        Toast.makeText(this, "提醒通知功能处于关闭状态", Toast.LENGTH_SHORT).show();

    }
}


