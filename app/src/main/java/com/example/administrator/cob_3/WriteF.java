package com.example.administrator.cob_3;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WriteF extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_writef,container, false);


        Button write = (Button)view.findViewById(R.id.write);
        Button userwrite = (Button)view.findViewById(R.id.userwrite);

        write.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), WriteMain.class);
                startActivity(intent);
            }
        });

        userwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteUser.class);
                startActivity(intent);
            }
        });

        return view;
    }

}

