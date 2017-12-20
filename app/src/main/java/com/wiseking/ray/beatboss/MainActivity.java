package com.wiseking.ray.beatboss;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wiseking.ray.beatboss.util.DotComBush;
import com.wiseking.ray.beatboss.util.SoundPlayUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DotComBush game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText text1= (EditText) findViewById(R.id.boss1);
        text1.setSelection(text1.getText().length());      //光标移动到编辑框的行末
        Button button_startgame = (Button) findViewById(R.id.startGame);
        Button button_checkhistory = (Button) findViewById(R.id.checkHistory);
        button_startgame.setOnClickListener(this);
        button_checkhistory.setOnClickListener(this);

        //初始化音效类
        SoundPlayUtils.init(this);
    }
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case  R.id.startGame :
                        SoundPlayUtils.play(1);  //播放按键音效
                        EditText text1= (EditText) findViewById(R.id.boss1);
                        text1.setSelection(text1.getText().length());
                        EditText text2= (EditText) findViewById(R.id.boss2);
                        EditText text3= (EditText) findViewById(R.id.boss3);
                        String name1=text1.getText().toString();
                        String name2=text2.getText().toString();
                        String name3=text3.getText().toString();

                        if(name1.length()!=3 || name2.length()!=3 || name3.length()!=3 ){
                            showAlertDialog();
                            return;
                        }
                        //创建DotComBush类实例
                        game= new DotComBush();
                        game.SetUpGame(name1,name2,name3);
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,StartGame.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("game",game);
                    intent.putExtras(bundle);
                    this.startActivity(intent);
                    break;
                    case  R.id.checkHistory :
                        SoundPlayUtils.play(1);  //播放按键音效
                        Intent intent1=new Intent(MainActivity.this,CheckHistory.class);
                        startActivity(intent1);
                        break;
                        default:
                            break;
                }

            }

    public void showAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("温馨提示").setMessage("请输入三个中文或三个英文字母，两个字的中文名请中间加空格！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}

