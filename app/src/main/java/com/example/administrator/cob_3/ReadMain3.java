package com.example.administrator.cob_3;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReadMain3 extends AppCompatActivity {

    private NfcAdapter nfcAdapter_1;
    private PendingIntent pendingIntent_1;
    TextView tv_1;
    Button readsave,readexit;
    String str_long, stname, staddress, stphone, stcompany, stjob, strId = Login.getStrId();
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_main3);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tv_1=(TextView)findViewById(R.id.tag_3);
        readsave = (Button)findViewById(R.id.readsave3);
        readexit = (Button)findViewById(R.id.readexit3);

        nfcAdapter_1 = nfcAdapter_1.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent_1 = PendingIntent.getActivity(this, 0, intent, 0);

        readsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = str_long.split("/");
                new madecard3().execute("http://192.168.0.213:8317/madecard3");
                finish();
            }
        });
        readexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    protected  void onResume(){
        super.onResume();
        if(nfcAdapter_1 != null){
            nfcAdapter_1.enableForegroundDispatch(this, pendingIntent_1,null,null);
        }
    }
    protected  void onPause(){
        super.onPause();
        if(nfcAdapter_1 !=null){
            nfcAdapter_1.disableForegroundDispatch(this);
        }
    }
    protected  void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Parcelable[] rawMsgs_1 = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (rawMsgs_1 != null) {
            NdefMessage msgs_1 = (NdefMessage) rawMsgs_1[0];
            NdefRecord[] rec = msgs_1.getRecords();
            byte[] bt_1 = rec[0].getPayload();
            String text_1 = new String(bt_1);
            str_long = text_1;
            tv_1.setText(text_1);
        }
    }

    //회원가입 메소드
    public class madecard3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("user_id", "androidTest");
                //jsonObject.accumulate("name", "jinsu");
                //jsonObject.accumulate("age", "41");
                jsonObject.accumulate("id", strId);
                jsonObject.accumulate("name",data[0]);
                jsonObject.accumulate("address",data[1]);
                jsonObject.accumulate("phone",data[2]);
                jsonObject.accumulate("company",data[3]);
                jsonObject.accumulate("job",data[4]);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    buffer.append(reader.readLine());

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임


                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("success")) {
                Toast.makeText(getApplicationContext(), "추가됨", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(result.equals("fail")){
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("update")){
                Toast.makeText(getApplicationContext(), "추가됨", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
        }
    }
}
