package com.wiseking.ray.beatboss;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wiseking.ray.beatboss.util.AppInfo;
import com.wiseking.ray.beatboss.util.MediaPlayUtils;
import com.wiseking.ray.beatboss.util.SoundPlayUtils;
import com.wiseking.ray.beatboss.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guo on 2017/12/28.
 */

public class AboutActivity extends AppCompatActivity implements TableRow.OnClickListener {
    private boolean isRemind=false;
    private boolean isMute=false;
    private int selectedLanguage=0;
    private List<AppInfo> appInfos= new ArrayList<AppInfo>();;
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
            case R.id.more_page_evaluation:
                if (!isMute){                   //不静音就播放音效
                    SoundPlayUtils.play(1);  //播放按键音效
                }
//                systemLaunchAppDetail();    //调用系统评分
//                systemLaunchAppDetailBySearchPackageName();   //调用系统按搜索包名评分
                customLaunchAPPDetail();   //调用自定义评分
//                Toast.makeText(AboutActivity.this,
//                        "评分 !", Toast.LENGTH_SHORT).show();
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

    /**
     * 获取已安装应用商店的包名列表
     * 获取有在AndroidManifest 里面注册<category android:name="android.intent.category.APP_MARKET" />的app
     * @param context
     * @return
     */
    public ArrayList<String> getInstallAppMarkets(Context context) {
        //默认的应用市场列表，有些应用市场没有设置APP_MARKET通过隐式搜索不到
        ArrayList<String> pkgList = new ArrayList<>();
        pkgList.add("com.xiaomi.market");
        pkgList.add("com.qihoo.appstore");
        pkgList.add("com.wandoujia.phoenix2");
        pkgList.add("com.tencent.android.qqdownloader");
        pkgList.add("com.taptap");
        ArrayList<String> pkgs = new ArrayList<String>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if (infos == null || infos.size() == 0)
            return pkgs;
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;


            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName))
                pkgs.add(pkgName);

        }
        //取两个list并集,去除重复
        pkgList.removeAll(pkgs);
        pkgs.addAll(pkgList);
        return pkgs;
    }

    /**
     * 过滤出已经安装的包名集合
     * @param context
     * @param pkgs 待过滤包名集合
     * @return 已安装的包名集合
     */
    public ArrayList<String> getFilterInstallMarkets(Context context,ArrayList<String> pkgs) {
        appInfos.clear();
        ArrayList<String> appList = new ArrayList<String>();
        if (context == null || pkgs == null || pkgs.size() == 0)
            return appList;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                PackageInfo packageInfo = installedPkgs.get(i);
                try {
                    installPkg = packageInfo.packageName;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    // 如果非系统应用，则添加至appList,这个会过滤掉系统的应用商店，如果不需要过滤就不用这个判断
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        //将应用相关信息缓存起来，用于自定义弹出应用列表信息相关用
                        AppInfo appInfo = new AppInfo();
                        appInfo.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                        appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
                        appInfo.setPackageName(packageInfo.packageName);
                        appInfo.setVersionCode(packageInfo.versionCode);
                        appInfo.setVersionName(packageInfo.versionName);
                        appInfos.add(appInfo);
                        appList.add(installPkg);
                    }
                    break;
                }

            }
        }
        return appList;
    }

    /**
     * 跳转到应用市场app详情界面
     * @param appPkg App的包名
     * @param marketPkg 应用市场包名
     */
    public void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
//            ToastHelper.showToast("你的手机没有按照Android应用市场");
            e.printStackTrace();
        }
    }

    public void customLaunchAPPDetail(){
        ArrayList<String> pkgs = getInstallAppMarkets(AboutActivity.this);
        ArrayList<String> marketList = getFilterInstallMarkets(AboutActivity.this, pkgs);
        String myAppPackageName=getPackageName();
        if (marketList.size()!=0){
            launchAppDetail(myAppPackageName,marketList.get(2));  //目前是去第一个评分
            /*for (int i=0;i<marketList.size();i++){
                launchAppDetail(myAppPackageName,marketList.get(i));  //全部都去评分 有点过分
            }*/
        }else {
            ToastHelper.showToast("你的手机没有按照Android应用市场");
        }

    }

    public void systemLaunchAppDetail() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            ToastHelper.showToast("你的手机没有按照Android应用市场");
            e.printStackTrace();
        }
    }

    public void systemLaunchAppDetailBySearchPackageName(){
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://search?q="+getPackageName()));
            startActivity(i);
        } catch (Exception e) {
            ToastHelper.showToast("你的手机没有按照Android应用市场");
            e.printStackTrace();
        }
    }
}
