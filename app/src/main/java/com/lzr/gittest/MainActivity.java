package com.lzr.gittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SpeedSeekBar mMainSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDatas();

    }



    private void initViews() {
        mMainSeekBar=findViewById(R.id.main_seekbar);


    }

    private void initDatas() {
        List<String> datas=new ArrayList<>();
        datas.add("0X");
        datas.add("1X");
        datas.add("2X");
        datas.add("3X");
        datas.add("4X");
        mMainSeekBar.setDatas(datas);
    }
}
