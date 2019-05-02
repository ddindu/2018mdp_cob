package com.example.administrator.cob_3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
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

public class Login extends AppCompatActivity {

    private EditText etLogin,etPass;
    private Button btLogin,help;
    private TextView tvJoin, tvFind;
    public static String strId;

    String stLogin = "";
    String stPass = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        etLogin = (EditText)findViewById(R.id.login);
        etPass = (EditText)findViewById(R.id.password);
        etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        btLogin = (Button)findViewById(R.id.btlogin);
        tvJoin = (TextView) findViewById(R.id.join);
        tvFind = (TextView)findViewById(R.id.idpassfind);
        help = (Button)findViewById(R.id.help);

        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Join.class);
                startActivity(intent);
            }
        });

        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FindIdpw.class);
                startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stLogin = etLogin.getText().toString();
                stPass = etPass.getText().toString();
                new login().execute("http://192.168.0.213:8317/login");
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,HelpActivity.class);
                startActivity(intent);
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



    public class login extends AsyncTask<String, String, String> {

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
                strId = etLogin.getText().toString();
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();
            } else if(result.equals("fail"))Toast.makeText(getApplicationContext(), "다시입력", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
        }
    }
    public static String getStrId() {
        return strId;
    }
}