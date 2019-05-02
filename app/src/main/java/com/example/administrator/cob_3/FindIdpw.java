package com.example.administrator.cob_3;

import android.content.Intent;
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

public class FindIdpw extends AppCompatActivity {

    Button btIdFind, btPassFind;
    EditText etIdFindN, etIdFindP, etPassFindI, etPassFindN, etPassFindP;
    String stIdFindN, stIdFindP, stPassFindI, stPassFindN, stPassFindP;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_idpw);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btIdFind = (Button) findViewById(R.id.idfind);
        btPassFind = (Button) findViewById(R.id.passfind);
        etIdFindN = (EditText) findViewById(R.id.idfindname);
        etIdFindP = (EditText) findViewById(R.id.idfindphone);
        etPassFindI = (EditText) findViewById(R.id.passfindid);
        etPassFindN = (EditText) findViewById(R.id.passfindname);
        etPassFindP = (EditText) findViewById(R.id.passfindphone);
        back = (TextView) findViewById(R.id.findback);

        btIdFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stIdFindN = etIdFindN.getText().toString();
                stIdFindP = etIdFindP.getText().toString();
                new idfind().execute("http://192.168.0.213:8317/idfind");
            }
        });

        btPassFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stPassFindI = etPassFindI.getText().toString();
                stPassFindN = etPassFindN.getText().toString();
                stPassFindP = etPassFindP.getText().toString();
                new passfind().execute("http://192.168.0.213:8317/passfind");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindIdpw.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public class idfind extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("user_id", "androidTest");
                //jsonObject.accumulate("name", "jinsu");
                //jsonObject.accumulate("age", "41");
                jsonObject.accumulate("name",stIdFindN);
                jsonObject.accumulate("phone",stIdFindP);

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

            if(result.equals("fail")) {
                Toast.makeText(getApplicationContext(), "아이디를 찾지 못하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(result.length() > 0) {
                Toast.makeText(getApplicationContext(), stIdFindN + " 님의 ID : " + result, Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
        }
    }

    public class passfind extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("user_id", "androidTest");
                //jsonObject.accumulate("name", "jinsu");
                //jsonObject.accumulate("age", "41");
                jsonObject.accumulate("id",stPassFindI);
                jsonObject.accumulate("name",stPassFindN);
                jsonObject.accumulate("phone",stPassFindP);

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

            if(result.equals("fail")) {
                Toast.makeText(getApplicationContext(), "비밀번호를 찾지 못하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(result.length() > 0) {
                Toast.makeText(getApplicationContext(), "ID : " + stPassFindI + " 의 비밀번호 : " + result, Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
        }
    }
}
