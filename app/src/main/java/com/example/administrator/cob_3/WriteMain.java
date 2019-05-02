package com.example.administrator.cob_3;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.id.message;

public class WriteMain extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    Intent intent;
    EditText et_1,et_2,et_3,et_4,et_5;
    boolean mode = false;
    String str_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        et_1 = (EditText)findViewById(R.id.tag_1);
        et_2 = (EditText)findViewById(R.id.tag_2);
        et_3 = (EditText)findViewById(R.id.tag_3);
        et_4 = (EditText)findViewById(R.id.tag_4);
        et_5 = (EditText)findViewById(R.id.tag_5);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this,0, intent, 0);
    }
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        String str_1 = et_1.getText().toString();
        String str_2 = et_2.getText().toString();
        String str_3 = et_3.getText().toString();
        String str_4 = et_4.getText().toString();
        String str_5 = et_5.getText().toString();
        Tag tagFromIntent= intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        str_long = str_1 + "/" + str_2 + "/" + str_3 + "/" + str_4 + "/" + str_5;
        NdefMessage message = getNdefMessage(str_long);
        write_1(message, tagFromIntent);
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 500);
    }
    private NdefMessage getNdefMessage(String text){
        byte[] textBytes = text.getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,"text/plain".getBytes(),
                new byte[]{},textBytes);
        NdefMessage message = new NdefMessage(textRecord);
        return message;

    }


    private  boolean write_1(NdefMessage message, Tag tagFromIntent){
        try{
            Ndef ndef = Ndef.get(tagFromIntent);
            if (ndef != null){
                ndef.connect();
                ndef.writeNdefMessage(message);
                ndef.close();
                Toast.makeText(this,"태그에 기록했습니다", Toast.LENGTH_LONG).show();

                return true;
            }
            return false;
        }catch (Exception e){
            Toast.makeText(this, "태그에 쓰기 실패했습니다",Toast.LENGTH_LONG).show();
        }
        return false;
    }
    public void onResume(){
        super.onResume();
        if(nfcAdapter != null){
            nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
        }
    }
    protected void onPause(){
        super.onPause();
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

}



