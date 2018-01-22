package com.wiseking.ray.beatboss.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.widget.Toast;

import com.wiseking.ray.beatboss.R;

/**
 * Created by guo on 2017/12/1.
 */

public class MediaPlayUtils {
    // SoundPool对象
    public static MediaPlayer mMediaPlayer = new MediaPlayer();
    public static MediaPlayUtils mediaPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static MediaPlayUtils init(Context context) {
        if (mediaPlayUtils == null) {
            mediaPlayUtils = new MediaPlayUtils();
        }

        // 初始化声音
        mContext = context;
        mMediaPlayer=MediaPlayer.create(mContext, R.raw.background);
        mMediaPlayer.setLooping(true);
        return mediaPlayUtils;
    }

    /**
     * 播放背景音乐
     *
     */
    public static void play() {
        if (mMediaPlayer==null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer=MediaPlayer.create(mContext, R.raw.background);
            mMediaPlayer.setLooping(true);
        }
        mMediaPlayer.start();

    }

    public static void stop(){
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
    }

}
