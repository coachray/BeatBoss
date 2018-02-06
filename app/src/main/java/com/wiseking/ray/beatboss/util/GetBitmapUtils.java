package com.wiseking.ray.beatboss.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.wiseking.ray.beatboss.R;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.content.ContentValues.TAG;

/**
 * Created by guo on 2017/12/1.
 */

public class GetBitmapUtils {

    public static GetBitmapUtils getBitmapUtils;
    //上下文
//    static Activity myActivity;

    /**
     * 初始化
     *
     */
    public static GetBitmapUtils init() {
        if (getBitmapUtils == null) {
            getBitmapUtils = new GetBitmapUtils();
        }

//        myActivity=activity;
        return getBitmapUtils;
    }

    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = Bitmap.createBitmap(
                    bmp,
                    0,
                    statusBarHeight,
                    dm.widthPixels,
                    dm.heightPixels - statusBarHeight
            );
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        if (ret != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String filePath = sdCardPath + File.separator + "screenshot.jpeg";
                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                ret.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
                /*//保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));*/
                Log.d("GML", "存储完成");
            } catch (Exception e) {
            }
        }
        return ret;
    }

    /*public static Bitmap getShotBitmap(){
        View dView = myActivity.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        if (bitmap != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String filePath = sdCardPath + File.separator + "screenshot.jpeg";
                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
                Log.d("GML", "存储完成");
            } catch (Exception e) {
            }
        }
        return bitmap;
    }*/

    /**
     * 截图listview
     * **/
    public static Bitmap getListViewBitmap(ListView listView) {
        int h = 0;
        Bitmap bitmap=null;
        if (listView.getCount()==0){
            ToastHelper.showToast("没有历史成绩可以分享！");
            return bitmap;
        }
        // 获取listView实际高度
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
        }
        Log.d("GML", "实际高度:" + h);
        Log.d("GML", "list 高度:" + listView.getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        listView.draw(canvas);
        try {
            // 获取内置SD卡路径
            String sdCardPath = Environment.getExternalStorageDirectory().getPath();
            // 图片文件路径
            String filePath = sdCardPath + File.separator + "screenshot.jpeg";
            File file = new File(filePath);
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
            os.flush();
            os.close();
            Log.d("GML", "存储完成");
        } catch (Exception e) {
        }
        return bitmap;
    }

    public static boolean showShare(Context context,String title,String content) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("");//这里分享图片为空http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // 获取内置SD卡路径
        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        // 图片文件路径
        String filePath = sdCardPath + File.separator + "screenshot.jpeg";
        oks.setImagePath(filePath);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("EasyBrowsing");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("https://www.baidu.com/");

        // 启动分享GUI
        oks.show(context);
        return true;
    }


}
