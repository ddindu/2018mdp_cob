package com.example.administrator.cob_3;

        import android.app.PendingIntent;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.nfc.NdefMessage;
        import android.nfc.NdefRecord;
        import android.nfc.NfcAdapter;
        import android.nfc.Tag;
        import android.nfc.tech.Ndef;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.EditText;
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

        import static android.R.id.message;

public class WriteUser extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    Intent intent;
    boolean mode = false;
    String str_long, strId = Login.getStrId(), stname, staddress, stphone, stcompany, stjob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_user);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this,0, intent, 0);

        new users().execute("http://192.168.0.213:8317/users");
    }

    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        Tag tagFromIntent= intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        str_long = stname + "/" + staddress + "/" + stphone + "/" + stcompany + "/" + stjob;
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

}



