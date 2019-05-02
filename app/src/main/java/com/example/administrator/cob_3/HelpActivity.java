package com.example.administrator.cob_3;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private Button forward,back;
    TextView help_text;
    String firsttap,secondtap,thirdtap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        forward = (Button)findViewById(R.id.forward);
        back = (Button)findViewById(R.id.back);
        help_text = (TextView)findViewById(R.id.help_text);
        firsttap = "1.명함집";
        secondtap = "2.명함쓰기";
        thirdtap = "3.사용자정보";

        getFragmentManager().beginTransaction().replace(R.id.frame_main_1, new helpf_1()).commit();

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_help = help_text.getText().toString();
                if (str_help.equals(firsttap)){
                    help_text.setText(secondtap);
                    getFragmentManager().beginTransaction().replace(R.id.frame_main_1, new helpf_2()).commit();
                }else if (str_help.equals(secondtap)){
                    help_text.setText(thirdtap);
                    getFragmentManager().beginTransaction().replace(R.id.frame_main_1, new helpf_3()).commit();
                }else if(str_help.equals(thirdtap)){
                    help_text.setText(firsttap);
                    getFragmentManager().beginTransaction().replace(R.id.frame_main_1, new helpf_1()).commit();
                }else {
                    finish();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_help = help_text.getText().toString();
                if (str_help.equals(firsttap)){
                    help_text.setText(thirdtap);
                    getFragmentManager().beginTransaction().replace(R.id.frame_main_1, new helpf_3()).commit();
                }else if (str_help.equals(secondtap)){
                    help_text.setText(firsttap);
                    getFragmentManager().beginTransaction().replace(R.id.frame_main_1, new helpf_1()).commit();
                }else if(str_help.equals(thirdtap)){
                    help_text.setText(secondtap);
                    getFragmentManager().beginTransaction().replace(R.id.frame_main_1, new helpf_2()).commit();
                }else {
                    finish();
                }
            }
        });
    }
}
