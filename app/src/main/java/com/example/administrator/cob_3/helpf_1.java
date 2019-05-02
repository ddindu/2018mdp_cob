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

public class helpf_1 extends Fragment {

    ImageView helpimg;
    Button next,back;
    TextView help_text;
    String str_help,first_text,second_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_helpf_1, container, false);

        next = (Button)view.findViewById(R.id.next);
        back = (Button)view.findViewById(R.id.back);
        help_text = (TextView)view.findViewById(R.id.help_text);
        helpimg = (ImageView) view.findViewById(R.id.helpimg);
        first_text = "1.하단의 탭중 왼쪽에 위치한 명함집입니다.\n2.상단의 탭으로 3개의 카테고리로 이동이 가능합니다.\n3.NEXT 버튼을 눌러주세요";
        second_text = "1.nfc기능을 키고 nfc스티커를 핸드폰에 태그하면 nfc스티커에 저장되어있던 정보가 출력됩니다.\n2.저장하기를 누르면 명함집에 저장이됩니다.\n3.닫기를 누르면 저장하지 않고 전 화면으로 되돌아갑니다";
        helpimg.setImageResource(R.drawable.readpage_3);

        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View v) {
                str_help = help_text.getText().toString();
                if (str_help.equals(first_text)){
                    help_text.setText(second_text);
                    helpimg.setImageResource(R.drawable.readpage_4);
                    helpimg.invalidate();
                }else if (str_help.equals(second_text)){
                    help_text.setText(first_text);
                    helpimg.setImageResource(R.drawable.readpage_3);
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
                    helpimg.setImageResource(R.drawable.readpage_4);
                    helpimg.invalidate();
                }else if (str_help.equals(second_text)){
                    help_text.setText(first_text);
                    helpimg.setImageResource(R.drawable.readpage_3);
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
