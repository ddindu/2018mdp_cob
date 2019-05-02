package com.example.administrator.cob_3;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ViewF extends Fragment{



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_viewf, container, false);

        LinearLayout btnviewf_1 = (LinearLayout)view.findViewById(R.id.btnviewf_1);
        LinearLayout btnviewf_2 = (LinearLayout)view.findViewById(R.id.btnviewf_2);
        LinearLayout btnviewf_3 = (LinearLayout)view.findViewById(R.id.btnviewf_3);
        getChildFragmentManager().beginTransaction().replace(R.id.frame_viewf, new ViewF_1()).commit();

        btnviewf_1.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View v){
                getChildFragmentManager().beginTransaction().replace(R.id.frame_viewf, new ViewF_1()).commit();
            }
        });
        btnviewf_2.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View v){
                getChildFragmentManager().beginTransaction().replace(R.id.frame_viewf, new ViewF_2()).commit();
            }
        });
        btnviewf_3.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View v){
                getChildFragmentManager().beginTransaction().replace(R.id.frame_viewf, new ViewF_3()).commit();
            }
        });

        return view;
    }

}
