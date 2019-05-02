package com.example.administrator.cob_3;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LinearLayout write_main = (LinearLayout) findViewById(R.id.write_main);
        LinearLayout collection_main = (LinearLayout) findViewById(R.id.collection_main);
        LinearLayout users_main = (LinearLayout) findViewById(R.id.users_main);

        getFragmentManager().beginTransaction().replace(R.id.frame_main, new ViewF()).commit();

        write_main.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().replace(R.id.frame_main, new WriteF()).commit();
            }
        });
        collection_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame_main, new ViewF()).commit();

            }
        });
        users_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame_main, new UserF()).commit();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("종료");
                builder.setMessage("앱을 종료 하시겠습니까?");
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                builder.show();
                break;
            }
            default:
                break;
        }
        return false;
    }

}









