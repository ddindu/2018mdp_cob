package com.example.administrator.cob_3;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class helpf_3 extends Fragment {

    ImageView helpimg;
    Button next,back;
    TextView help_text;
    String str_help,first_text,second_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_helpf_3, container, false);

        next = (Button)view.findViewById(R.id.next);
        back = (Button)view.findViewById(R.id.back);
        help_text = (TextView)view.findViewById(R.id.help_text);
        helpimg = (ImageView) view.findViewById(R.id.helpimg);
        first_text="1.하단의 탭중 우측에 위치한 사용자 정보입니다.\n2.좌측 하단에 위치한 사용자등록/수정 버튼을 클릭하면 앱에 사용자정보를 기록할 수 있습니다.\n3.로그아웃버튼을 누르면 다른 사용자에게 자신의 사 용자 정보를 전송할 수 있습니다.\n4.NEXT버튼을 눌러주세요.";
        second_text = "1.명함을 보내고 싶은 사용자의 ID를 입력한 후 전송을 누르면 나의 사용자 정보를 전송할 수 있습니다. ";
        helpimg.setImageResource(R.drawable.userpage_3);

        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View v) {
                str_help = help_text.getText().toString();
                if (str_help.equals(first_text)){
                    help_text.setText(second_text);
                    helpimg.setImageResource(R.drawable.userpage_4);
                    helpimg.invalidate();
                }else if (str_help.equals(second_text)){
                    help_text.setText(first_text);
                    helpimg.setImageResource(R.drawable.userpage_3);
                    helpimg.invalidate();
                }
                else{
                    Toast.makeText(getActivity(), "에러", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_help = help_text.getText().toString();
                if (str_help.equals(first_text)){
                    help_text.setText(second_text);
                    helpimg.setImageResource(R.drawable.userpage_3);
                    helpimg.invalidate();
                }else if (str_help.equals(second_text)){
                    help_text.setText(first_text);
                    helpimg.setImageResource(R.drawable.userpage_4);
                    helpimg.invalidate();
                }
                else{
                    Toast.makeText(getActivity(), "에러", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
