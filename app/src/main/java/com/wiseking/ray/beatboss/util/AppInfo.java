package com.wiseking.ray.beatboss.util;

import android.graphics.drawable.Drawable;

/**
 * Created by guo on 2018/2/2.
 */

public class AppInfo {
    private String apkpath;


    public String getApkpath() {
        return apkpath;
    }

    public void setApkpath(String apkpath) {
        this.apkpath = apkpath;
    }

    /**
     * 应用程序的图标
     */
    private Drawable icon;

    /**
     * 应用程序名称
     */
    private String name;

    /**
     * 应用程序安装的位置，true手机内存 ，false外部存储卡
     */
    private boolean inRom;

    /**
     * 应用程序的大小
     */
    private long appSize;

    /**
     * 是否是用户程序  true 用户程序 false 系统程序
     */
    private boolean userApp;

    /**
     * 应用程序的包名
     */
    private String packname;

    /**
     * 版本码
     */
    private int versionCode;

    /**
     * 版本名
     */
    private String versionName;


    public Drawable getAppIcon() {
        return icon;
    }

    public void setAppIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return name;
    }

    public void setAppName(String name) {
        this.name = name;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public String getPackageName() {
        return packname;
    }

    public void setPackageName(String packname) {
        this.packname = packname;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {

        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "AppInfo [name=" + name + ", inRom=" + inRom + ", appSize="
                + appSize + ", userApp=" + userApp + ", packname=" + packname
                + "]";
    }
}
