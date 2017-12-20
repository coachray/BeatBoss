package com.wiseking.ray.beatboss.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.wiseking.ray.beatboss.R;

/**
 * Created by guo on 2017/12/1.
 */

public class SoundPlayUtils {
    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtils soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }

        // 初始化声音
        mContext = context;

        mSoundPlayer.load(mContext, R.raw.de, 1);// 1 按钮按键音效
        mSoundPlayer.load(mContext, R.raw.bubu, 1);// 2 未命中或重复音效
        mSoundPlayer.load(mContext, R.raw.bigbomb, 1);// 3 命中音效
        mSoundPlayer.load(mContext, R.raw.bomb2s, 1);// 4  消灭音效
        mSoundPlayer.load(mContext, R.raw.victory, 1);// 5  胜利音效
//        mSoundPlayer.load(mContext, R.raw.popup, 1);// 6
//        mSoundPlayer.load(mContext, R.raw.water, 1);// 7
//        mSoundPlayer.load(mContext, R.raw.ying, 1);// 8

        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

}
