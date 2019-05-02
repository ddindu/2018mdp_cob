package com.example.administrator.cob_3;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    ImageView phone,nfc,cob;
    Animation phone_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        phone = (ImageView)findViewById(R.id.phone);
        nfc = (ImageView)findViewById(R.id.nfc);
        cob = (ImageView)findViewById(R.id.cob);

        phone_move = AnimationUtils.loadAnimation(this, R.anim.phone_move);

        Handler delayHandler = new Handler();

        delayHandler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

        phone.startAnimation(phone_move);
        delayHandler.postDelayed(new Runnable() {
            public void run() {
                cob.setImageResource(R.drawable.cob);
            }
        }, 1500);



    }
}
