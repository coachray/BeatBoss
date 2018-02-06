package com.wiseking.ray.beatboss;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wiseking.ray.beatboss.util.MediaPlayUtils;
import com.wiseking.ray.beatboss.util.SoundPlayUtils;

/**
 * Created by guo on 2017/12/28.
 */

public class SupportActivity extends AppCompatActivity{
    private boolean isMute=false;  //是否静音
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_support);

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
        titleText.setText("支持");

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

}


