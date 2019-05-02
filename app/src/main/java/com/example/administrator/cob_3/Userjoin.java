package com.example.administrator.cob_3;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Administrator on 2018-11-13.
 */

public class Userjoin extends AppCompatActivity {

    EditText etName, etAddress, etPhone, etCompany, etJob;
    Button btUserjoin;
    String stName, stAddress, stPhone, stCompany, stJob;
    TextView tvUsers;
    String strId = Login.getStrId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userjoin);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etName = (EditText) findViewById(R.id.username);
        etAddress = (EditText) findViewById(R.id.useraddress);
        etPhone = (EditText) findViewById(R.id.userphone);
        etCompany = (EditText) findViewById(R.id.usercompany);
        etJob = (EditText) findViewById(R.id.userjob);
        btUserjoin = (Button) findViewById(R.id.userjoin);

        btUserjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stName = etName.getText().toString();
                stAddress = etAddress.getText().toString();
                stPhone = etPhone.getText().toString();
                stCompany = etCompany.getText().toString();
                stJob = etJob.getText().toString();
                new userjoin().execute("http://192.168.0.213:8317/userjoin");
            }
        });

    }

    public class userjoin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("user_id", "androidTest");
                //jsonObject.accumulate("name", "jinsu");
                //jsonObject.accumulate("age", "41");
                jsonObject.accumulate("id", strId);
                jsonObject.accumulate("name",stName);
                jsonObject.accumulate("address",stAddress);
                jsonObject.accumulate("phone",stPhone);
                jsonObject.accumulate("company",stCompany);
                jsonObject.accumulate("job",stJob);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
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
                Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(result.equals("fail")){
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("overlap")){
                Toast.makeText(getApplicationContext(), "사용자 수정함", Toast.LENGTH_SHORT).show();
                finish();
            }
            else Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
        }
    }
}
