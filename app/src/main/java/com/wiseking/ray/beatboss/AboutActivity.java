package com.wiseking.ray.beatboss;

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

import com.wiseking.ray.beatboss.util.SoundPlayUtils;

/**
 * Created by guo on 2017/12/28.
 */

public class AboutActivity extends AppCompatActivity implements TableRow.OnClickListener {
    private boolean isRemind=false;
    private boolean isMute=false;
    private int selectedLanguage=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);

        //读取设定值
        SharedPreferences settings = getSharedPreferences("setting", 0);
        isRemind= settings.getBoolean("isremind",false);
        isMute= settings.getBoolean("ismute",false);
        selectedLanguage = settings.getInt("selectedLanguage", 0);

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
        titleText.setText("关于我们");

        TextView versionNameText = (TextView) findViewById(R.id.versionName_text);
        versionNameText.setText(getVersion());

        TableRow myEvaluation=(TableRow) findViewById(R.id.more_page_evaluation);
        TableRow myFeedback=(TableRow) findViewById(R.id.more_page_feedback);
        TableRow mySupport=(TableRow) findViewById(R.id.more_page_support);
        TableRow myHelp=(TableRow) findViewById(R.id.more_page_help);

        myEvaluation.setOnClickListener(this);
        myFeedback.setOnClickListener(this);
        mySupport.setOnClickListener(this);
        myHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_page_evaluation:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                Toast.makeText(AboutActivity.this,
                        "评分 !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more_page_feedback:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                Intent intent_feedback=new Intent(AboutActivity.this,FeedbackActivity.class);
                startActivity(intent_feedback);
                break;
            case R.id.more_page_support:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                Intent intent_support=new Intent(AboutActivity.this,SupportActivity.class);
                startActivity(intent_support);
                break;
            case R.id.more_page_help:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
                Intent intent_help=new Intent(AboutActivity.this,HelpActivity.class);
                startActivity(intent_help);
                break;

        }

    }

     /**
      * 获取版本号
      * @return 当前应用的版本号
      */
      public String getVersion() {
             try {
                    PackageManager manager = this.getPackageManager();
                     PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                     String version = info.versionName;
                     return this.getString(R.string.version_name) + version;
                 } catch (Exception e) {
                     e.printStackTrace();
                     return this.getString(R.string.can_not_find_version_name);
                 }
         }

}
