package com.example.administrator.cob_3;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.ArrayList;

public class UserF extends Fragment {

    Button btjoin, btLogout, btTrans;
    String stname,staddress,stphone,stcompany,stjob, strtrans ,strId = Login.getStrId();
    SwipeRefreshLayout user_refresh;
    ListView user_list;
    ArrayList<UserListitem> user_listitem=new ArrayList<UserListitem>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_userf, container, false);

        btjoin = (Button) view.findViewById(R.id.btjoin);
        btLogout = (Button) view.findViewById(R.id.btlogout);
        btTrans = (Button) view.findViewById(R.id.bttrans);
        user_refresh = (SwipeRefreshLayout) view.findViewById(R.id.user_refresh);
        user_list = (ListView) view.findViewById(R.id.userlistview);

        new users().execute("http://192.168.0.213:8317/users");

        Handler delayHandler = new Handler();

        delayHandler.postDelayed(new Runnable() {
            public void run() {

                user_listitem.add(new UserListitem("이름",stname,0));
                user_listitem.add(new UserListitem("주소",staddress,R.drawable.address));
                user_listitem.add(new UserListitem("전화번호",stphone,R.drawable.call));
                user_listitem.add(new UserListitem("회사",stcompany,R.drawable.company));
                user_listitem.add(new UserListitem("직책",stjob,R.drawable.job_1));
                UserListAdpater adapter=new UserListAdpater(getActivity(),R.layout.list_item,user_listitem);
                user_list.setAdapter(adapter);
            }
        }, 200);

        btjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Userjoin.class);
                startActivity(intent);
            }
        });

        btTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("사용자 정보 전송");
                builder.setMessage("전송하실 아이디를 입력해주세요.");
                final EditText et = new EditText(getActivity());
                builder.setView(et);
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        strtrans = et.getText().toString();
                        new trans().execute("http://192.168.0.213:8317/trans");

                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                alert_confirm.setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), Login.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        user_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new users().execute("http://192.168.0.213:8317/users");

                Handler delayHandler = new Handler();

                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        user_listitem.clear();
                        user_listitem.add(new UserListitem("이름",stname,0));
                        user_listitem.add(new UserListitem("주소",staddress,R.drawable.address));
                        user_listitem.add(new UserListitem("전화번호",stphone,R.drawable.call));
                        user_listitem.add(new UserListitem("회사",stcompany,R.drawable.company));
                        user_listitem.add(new UserListitem("직책",stjob,R.drawable.job_1));
                        UserListAdpater adapter=new UserListAdpater(getActivity(),R.layout.list_item,user_listitem);
                        user_list.setAdapter(adapter);

                        user_refresh.setRefreshing(false);
                    }
                }, 200);
            }
        });

        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 : {
                        break;
                    }
                    case 1 : {
                        Intent mintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/maps/search/" + user_listitem.get(position).getContents() + "/"));
                        startActivity(mintent);
                        break;
                    }
                    case 2 : {
                        Intent mintent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/" + user_listitem.get(position).getContents()));
                        startActivity(mintent);
                        break;
                    }
                    case 3 : {
                        Intent mintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/maps/search/" + user_listitem.get(position).getContents() + "/"));
                        startActivity(mintent);
                        break;
                    }
                }
            }
        });

        return view;
    }

    public class users extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("user_id", "androidTest");
                //jsonObject.accumulate("name", "jinsu");
                //jsonObject.accumulate("age", "41");
                jsonObject.accumulate("id",strId);

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

            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    stname = jsonObject.optString("name");
                    staddress = jsonObject.optString("address");
                    stphone = jsonObject.optString("phone");
                    stcompany = jsonObject.optString("company");
                    stjob = jsonObject.optString("job");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class trans extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("user_id", "androidTest");
                //jsonObject.accumulate("name", "jinsu");
                //jsonObject.accumulate("age", "41");
                jsonObject.accumulate("id",strId);
                jsonObject.accumulate("trans", strtrans);

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

            if (result.equals("success")) Toast.makeText(getActivity(), "전송 성공", Toast.LENGTH_SHORT).show();
            else if (result.equals("fail")) Toast.makeText(getActivity(), "전송 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
class UserListAdpater extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<UserListitem> data;
    private int layout;
    public UserListAdpater(Context context, int layout, ArrayList<UserListitem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getFrame();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        UserListitem userListitem=data.get(position);
        TextView frame=(TextView)convertView.findViewById(R.id.userlistframe);
        frame.setText(userListitem.getFrame());
        TextView contents=(TextView)convertView.findViewById(R.id.userlistcontents);
        contents.setText(userListitem.getContents());
        ImageView icon=(ImageView)convertView.findViewById(R.id.userlistimageview);
        icon.setImageResource(userListitem.getIcon());
        return convertView;
    }
}

class UserListitem {
    private int icon;
    private String frame, contents;
    public int getIcon(){return icon;}
    public String getFrame(){return frame;}
    public String getContents(){return contents;}

    public UserListitem(String frame, String contents, int icon){
        this.frame = frame;
        this.contents = contents;
        this.icon=icon;
    }
}






