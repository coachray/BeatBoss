package com.wiseking.ray.beatboss;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wiseking.ray.beatboss.util.MediaPlayUtils;
import com.wiseking.ray.beatboss.util.SoundPlayUtils;
import com.wiseking.ray.beatboss.util.ToastHelper;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by guo on 2017/12/28.
 */

public class SettingActivity extends AppCompatActivity implements TableRow.OnClickListener {
    private boolean isRemind=false;
    private boolean isMute=false;
    private int selectedLanguage=0;
    private String versionName;
    private int versionCode;
    final String[] content = {"中文-简体","中文-繁体","English"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        //取得版本号
        getVersion();
        //初始化媒体类


        //读取设定值
        SharedPreferences settings = getSharedPreferences("setting", 0);
        isRemind= settings.getBoolean("isremind",false);
        isMute= settings.getBoolean("ismute",false);
        selectedLanguage = settings.getInt("selectedLanguage", 0);

        //设置界面上选择的语言
        TextView myLanguageText=(TextView) findViewById(R.id.language_text);
        myLanguageText.setText("语言："+content[selectedLanguage]);

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
        titleText.setText("设置");

        Switch myRemind=(Switch) findViewById(R.id.switch_remind);
        Switch mySound=(Switch) findViewById(R.id.switch_sound);
        myRemind.setChecked(isRemind);
        mySound.setChecked(isMute);
        TableRow myLanguage=(TableRow) findViewById(R.id.more_page_language);
        TableRow myRefresh=(TableRow) findViewById(R.id.more_page_refresh);
        TableRow myAbout=(TableRow) findViewById(R.id.more_page_about);
        TableRow myrobot=(TableRow) findViewById(R.id.more_page_robot);
        TableRow myLogin=(TableRow) findViewById(R.id.more_page_login);
        TableRow myNetwork=(TableRow) findViewById(R.id.more_page_network);

        myRemind.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRemind=isChecked;
                //打开通知开关就注册定时通知
                if (isRemind){
                    startRemind();
                }else {
                    stopRemind();
                }
                //Use 0 or MODE_PRIVATE for the default operation
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                SharedPreferences settings = getSharedPreferences("setting", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("isremind", isRemind);

                // 提交本次编辑
                editor.commit();
            }
        });

        mySound.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                isMute=isChecked;

                if (isMute){
                    MediaPlayUtils.pause();
                }else {
                    MediaPlayUtils.play();
                }

                //Use 0 or MODE_PRIVATE for the default operation
                SharedPreferences settings = getSharedPreferences("setting", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("ismute", isMute);

                // 提交本次编辑
                editor.commit();
            }
        });
        myLanguage.setOnClickListener(this);
        myRefresh.setOnClickListener(this);
        myAbout.setOnClickListener(this);
        myrobot.setOnClickListener(this);
        myLogin.setOnClickListener(this);
        myNetwork.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //读取设定值
        SharedPreferences settings = getSharedPreferences("setting", 0);
        isMute= settings.getBoolean("ismute",false);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_page_language:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                showSettingLanguageAlertDialog();
                break;
            case R.id.more_page_refresh:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                Toast.makeText(this,"正在检查，请稍后……",Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"已是最新版本",Toast.LENGTH_SHORT).show();
                break;
            case R.id.more_page_about:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                Intent intent1=new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.more_page_robot:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                Intent intent2=new Intent(SettingActivity.this,ChatActivity.class);
                startActivity(intent2);
                break;

        }

    }

    public void showSettingLanguageAlertDialog() {
        //1, 得到普通对话框的构建者
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle("语言设置");
        //2, 设置多选列表
        /**
         * items 资源的ID
         * checkedItems 当前选中的Item
         * listener 监听器
         */
        //得到所有的数据
//        final String[] content = {"中文-简体","中文-繁体","English"};
        //选中的Item
        builder.setSingleChoiceItems(content, selectedLanguage, new DialogInterface.OnClickListener() {
            /**
             * DialogInterface dialog 当前对话框
             * int i 当前选择的Item的值
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                selectedLanguage=i;
//                Toast.makeText(getApplicationContext(), "You clicked "+content[i], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                //设置界面上选择的语言
//                final String[] content = {"中文-简体","中文-繁体","English"};
                TextView myLanguageText=(TextView) findViewById(R.id.language_text);
                myLanguageText.setText("语言："+content[selectedLanguage]);
                //保存语言设置
                SharedPreferences settings = getSharedPreferences("setting", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("selectedLanguage", selectedLanguage);

                // 提交本次编辑
                editor.commit();
            }
        });
        //3, 显示对话框
        builder.show();
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public void getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionName = info.versionName;
            versionCode=info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
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
        Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(SettingActivity.this, 0, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);

        /**
         * 重复提醒
         * 第一个参数是警报类型；下面有介绍
         * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，但是我用的刷了MIUI的三星手机的实际效果是与单次提醒的参数一样，即设置的13点25分的时间点毫秒值
         * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
         */
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
       ToastHelper.showToast("提醒通知功能开启成功，你会在每天12:30收到通知! ");

    }

    /**
     * 关闭提醒
     */
    public void stopRemind(){

        Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(SettingActivity.this, 0,
                intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
        ToastHelper.showToast("您关闭了提醒通知功能!");
    }


}
