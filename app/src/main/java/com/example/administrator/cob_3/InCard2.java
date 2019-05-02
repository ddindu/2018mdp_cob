package com.example.administrator.cob_3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class InCard2 extends AppCompatActivity {

    String stname,staddress,stphone,stcompany,stjob, strId = Login.getStrId();
    String getName, getPhone;
    int cardcount = ViewF_2.getCount();
    SwipeRefreshLayout card_refresh;
    ListView card_list;
    Button back;
    ArrayList<CardListitem3> card_listitem=new ArrayList<CardListitem3>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_card2);

        card_list = (ListView) findViewById(R.id.cardlistview2);
        card_refresh = (SwipeRefreshLayout) findViewById(R.id.card_refresh2);
        back = (Button)findViewById(R.id.back) ;

        getName = ViewF_2.getName();
        getPhone = ViewF_2.getPhone();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new cards2().execute("http://192.168.0.213:8317/cards2");

        Handler delayHandler = new Handler();

        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] name, address, phone, company, job;

                if(stname == null) {
                    name = new String[0];
                    address = new String[0];
                    phone = new String[0];
                    company = new String[0];
                    job = new String[0];
                } else {
                    name = stname.split("/");
                    address = staddress.split("/");
                    phone = stphone.split("/");
                    company = stcompany.split("/");
                    job = stjob.split("/");
                }

                card_listitem.add(new CardListitem3("이름",name[cardcount],0));
                card_listitem.add(new CardListitem3("주소",address[cardcount],R.drawable.address));
                card_listitem.add(new CardListitem3("전화번호",phone[cardcount],R.drawable.call));
                card_listitem.add(new CardListitem3("회사",company[cardcount],R.drawable.company));
                card_listitem.add(new CardListitem3("직책",job[cardcount],R.drawable.job_1));
                CardListAdpater3 adapter=new CardListAdpater3(InCard2.this ,R.layout.list_item,card_listitem);
                card_list.setAdapter(adapter);
            }
        }, 250);

        card_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new cards2().execute("http://192.168.0.213:8317/cards2");

                Handler delayHandler = new Handler();

                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String[] name, address, phone, company, job;

                        if(stname == null) {
                            name = new String[0];
                            address = new String[0];
                            phone = new String[0];
                            company = new String[0];
                            job = new String[0];
                        } else {
                            name = stname.split("/");
                            address = staddress.split("/");
                            phone = stphone.split("/");
                            company = stcompany.split("/");
                            job = stjob.split("/");
                        }

                        card_listitem.clear();

                        card_listitem.add(new CardListitem3("이름",name[cardcount],0));
                        card_listitem.add(new CardListitem3("주소",address[cardcount],R.drawable.address));
                        card_listitem.add(new CardListitem3("전화번호",phone[cardcount],R.drawable.call));
                        card_listitem.add(new CardListitem3("회사",company[cardcount],R.drawable.company));
                        card_listitem.add(new CardListitem3("직책",job[cardcount],R.drawable.job_1));
                        CardListAdpater3 adapter=new CardListAdpater3(InCard2.this ,R.layout.list_item,card_listitem);
                        card_list.setAdapter(adapter);
                    }
                }, 250);

                card_refresh.setRefreshing(false);
            }
        });

        card_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 : {
                        break;
                    }
                    case 1 : {
                        Intent mintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/maps/search/" + card_listitem.get(position).getContents() + "/"));
                        startActivity(mintent);
                        break;
                    }
                    case 2 : {
                        Intent mintent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/" + card_listitem.get(position).getContents()));
                        startActivity(mintent);
                        break;
                    }
                    case 3 : {
                        Intent mintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/maps/search/" + card_listitem.get(position).getContents() + "/"));
                        startActivity(mintent);
                        break;
                    }
                }
            }
        });
    }

    // 카드 정보 받아오기
    public class cards2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
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
}

class CardListAdpater2 extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<CardListitem3> data;
    private int layout;
    public CardListAdpater2(Context context, int layout, ArrayList<CardListitem3> data){
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
        CardListitem3 cardListitem3 =data.get(position);
        TextView frame=(TextView)convertView.findViewById(R.id.userlistframe);
        frame.setText(cardListitem3.getFrame());
        TextView contents=(TextView)convertView.findViewById(R.id.userlistcontents);
        contents.setText(cardListitem3.getContents());
        ImageView icon=(ImageView)convertView.findViewById(R.id.userlistimageview);
        icon.setImageResource(cardListitem3.getIcon());
        return convertView;
    }
}

class CardListitem2 {
    private int icon;
    private String frame, contents;
    public int getIcon(){return icon;}
    public String getFrame(){return frame;}
    public String getContents(){return contents;}

    public CardListitem2(String frame, String contents, int icon){
        this.frame = frame;
        this.contents = contents;
        this.icon=icon;
    }
}




