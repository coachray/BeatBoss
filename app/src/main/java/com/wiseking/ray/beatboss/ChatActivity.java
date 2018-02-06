package com.wiseking.ray.beatboss;

/**
 * Created by guo on 2018/2/4.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wiseking.ray.beatboss.util.ChatMessage;
import com.wiseking.ray.beatboss.util.ChatMessageAdapter;
import com.wiseking.ray.beatboss.util.HttpUtils;
import com.wiseking.ray.beatboss.util.MediaPlayUtils;
import com.wiseking.ray.beatboss.util.ToastHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class ChatActivity extends Activity {
    private List<ChatMessage> list;
    private ListView chat_listview;
    private EditText chat_input;
    private Button chat_send;
    private ChatMessageAdapter chatAdapter;
    private ChatMessage chatMessage = null;
    private boolean isMute=false;  //是否静音

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        initView();
        initListener();
        initData();
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

    // 1.初始视图
    private void initView() {
        // 1.初始化
        chat_listview = (ListView) findViewById(R.id.chat_listview);
        chat_input = (EditText) findViewById(R.id.chat_input_message);
        chat_send = (Button) findViewById(R.id.chat_send);
    }

    // 2.设置监听事件
    private void initListener() {
        chat_send.setOnClickListener(onClickListener);
    }

    // 3.初始化数据
    private void initData() {
        list = new ArrayList<ChatMessage>();
        String[] sayHi={"小主人，你来啦！","呜呜~，小主人你终于来啦，我好想你！",
                "呜呜~，小主人你终于来啦，我一个人好无聊哦！","小主人，你来啦，快和我聊天吧！",
                "小主人，你来啦，今天聊点什么？","啦啦啦~我是一只小蜜蜂~，小主人你来啦！心情好吗？",
                "一个人好无聊，谁来陪我聊天啊~~~","我是可爱漂亮温柔善良充满无限活力的小爱，小主人今天开心吗？",
                "小主人，我是你的小爱，我会的可多了，讲故事、讲笑话、猜脑筋急转弯、预报天气、差飞机火车班次……样样精通，我很厉害吧：）",
                "我是女生，快乐的女生……小主人你来啦，和我一起唱歌吧！"};
        int num=(int) (Math.random()*10);
        list.add(new ChatMessage(sayHi[num], ChatMessage.Type.INCOUNT, new Date()));
        chatAdapter = new ChatMessageAdapter(list);
        chat_listview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    // 4.发送消息聊天
    private void chat() {
        // 1.判断是否输入内容
        final String send_message = chat_input.getText().toString().trim();
        if (TextUtils.isEmpty(send_message)) {
            ToastHelper.showToast("哦噢~~，你啥也没说啊？！");
            return;
        }

        // 2.自己输入的内容也是一条记录，记录刷新
        ChatMessage sendChatMessage = new ChatMessage();
        sendChatMessage.setMessage(send_message);
        sendChatMessage.setData(new Date());
        sendChatMessage.setType(ChatMessage.Type.OUTCOUNT);
        list.add(sendChatMessage);
        chatAdapter.notifyDataSetChanged();
        chat_listview.setSelection(list.size());// 将ListView定位到最后一行
        chat_input.setText("");

        // 3.发送你的消息，去服务器端，返回数据
        new Thread() {
            public void run() {
                ChatMessage chat = HttpUtils.sendMessage(send_message);
                Message message = new Message();
                message.what = 0x1;
                message.obj = chat;
                handler.sendMessage(message);
            };
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x1) {
                if (msg.obj != null) {
                    chatMessage = (ChatMessage) msg.obj;
                }
                // 添加数据到list中，更新数据
                list.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
                chat_listview.setSelection(list.size());// 将ListView定位到最后一行
            }
        };
    };

    // 点击事件监听
    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chat_send:
                    chat();
                    break;
            }
        }
    };
}
