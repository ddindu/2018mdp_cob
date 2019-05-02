package com.example.administrator.cob_3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class Join extends AppCompatActivity {

    private EditText etId,etPass,chpass;
    private Button Join;
    TextView back;

    String stLogin = "";
    String stPass = "";
    String stchPass = "";

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        etId = (EditText)findViewById(R.id.etid);
        etPass = (EditText)findViewById(R.id.etpassword_1);
        chpass = (EditText)findViewById(R.id.etpassword_2);
        etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        chpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Join = (Button)findViewById(R.id.join);
        back = (TextView) findViewById(R.id.joinback);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Join.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        Join.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                stLogin = etId.getText().toString();
                stPass = etPass.getText().toString();
                stchPass = chpass.getText().toString();
                if (stPass.equals(stchPass)){
                    new join().execute("http://192.168.0.213:8317/join");
                }else Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않음", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //회원가입 메소드
    public class join extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("user_id", "androidTest");
                //jsonObject.accumulate("name", "jinsu");
                //jsonObject.accumulate("age", "41");
                jsonObject.accumulate("id",stLogin);
                jsonObject.accumulate("password",stPass);

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
                Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(result.equals("fail")){
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("overlap")){
                Toast.makeText(getApplicationContext(), "중복", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
        }
    }
}

