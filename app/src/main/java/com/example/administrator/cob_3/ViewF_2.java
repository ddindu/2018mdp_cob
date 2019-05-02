package com.example.administrator.cob_3;

        import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

public class ViewF_2 extends Fragment {

    ListView data_list;
    ArrayList<DataListitem3> data_listitem=new ArrayList<DataListitem3>();
    SwipeRefreshLayout data_refresh;
    String stname, staddress, stphone, stcompany, stjob, strId = Login.getStrId();
    static String setName, setPhone;
    static int count;
    int itcount;
    String[] name, address, phone, company, job;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_viewf_2, container, false);

        Button pluscob = (Button) view.findViewById(R.id.pluscob2);
        data_list = (ListView) view.findViewById(R.id.datalistview2);
        data_refresh = (SwipeRefreshLayout) view.findViewById(R.id.data_refresh2);

        new cards2().execute("http://192.168.0.213:8317/cards2");

        Handler delayHandler = new Handler();

        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

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

                for (int i=0; i < itcount; i++) {
                    data_listitem.add(new DataListitem3(name[i],phone[i],0));
                    DataListAdpater3 adapter=new DataListAdpater3(getActivity(),R.layout.list_item,data_listitem);
                    data_list.setAdapter(adapter);
                }

            }
        }, 200);

        data_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new cards2().execute("http://192.168.0.213:8317/cards2");

                Handler delayHandler = new Handler();

                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data_listitem.clear();

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

                        for (int i=0; i < itcount; i++) {
                            data_listitem.add(new DataListitem3(name[i],phone[i],0));
                            DataListAdpater3 adapter=new DataListAdpater3(getActivity(),R.layout.list_item,data_listitem);
                            data_list.setAdapter(adapter);
                        }
                    }
                }, 200);

                data_refresh.setRefreshing(false);
            }
        });

        data_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                count = position;
                setNamephone(name[position], phone[position]);
                Intent intent = new Intent(getActivity(),InCard2.class);
                startActivity(intent);
            }
        });


        pluscob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReadMain2.class);
                startActivity(intent);
            }
        });

        return view;
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
                    itcount = jsonObject.optInt("count");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void setNamephone(String name, String phone) {
        this.setName = name;
        this.setPhone = phone;
    }

    public static String getName() {
        return setName;
    }
    public static String getPhone() {
        return setPhone;
    }
    public static int getCount() {
        return count;
    }
}

class DataListAdpater2 extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<DataListitem3> data;
    private int layout;
    public DataListAdpater2(Context context, int layout, ArrayList<DataListitem3> data){
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
        DataListitem3 dataListitem3 =data.get(position);
        TextView frame=(TextView)convertView.findViewById(R.id.userlistframe);
        frame.setText(dataListitem3.getFrame());
        TextView contents=(TextView)convertView.findViewById(R.id.userlistcontents);
        contents.setText(dataListitem3.getContents());
        ImageView icon=(ImageView)convertView.findViewById(R.id.userlistimageview);
        icon.setImageResource(dataListitem3.getIcon());
        return convertView;
    }
}

class DataListitem2 {
    private int icon;
    private String frame, contents;
    public int getIcon(){return icon;}
    public String getFrame(){return frame;}
    public String getContents(){return contents;}

    public DataListitem2(String frame, String contents, int icon){
        this.frame = frame;
        this.contents = contents;
        this.icon=icon;
    }
}

