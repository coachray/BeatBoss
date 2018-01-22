package com.wiseking.ray.beatboss;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.wiseking.ray.beatboss.util.SoundPlayUtils;

/**
 * Created by guo on 2017/12/28.
 */

public class HelpActivity extends AppCompatActivity{
    private boolean isMute=false;  //是否静音
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help);

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
        titleText.setText("帮助");

    }

}


