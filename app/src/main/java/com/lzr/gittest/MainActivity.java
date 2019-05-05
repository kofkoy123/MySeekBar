package com.lzr.gittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        mMainSeekBar.setOnSeekBarChangedListener(new SpeedSeekBar.OnPointSeekBarChangedListener() {
            @Override
            public void onProgressChanging(SpeedSeekBar speedSeekBar, float progress) {

            }

            @Override
            public void onChanged(float progress) {
                Log.e("lzr","进度："+progress);
            }
        });
    }

    private void initDatas() {
        List<String> datas=new ArrayList<>();
        datas.add("1X");
        datas.add("2X");
        datas.add("3X");
        datas.add("4X");
        datas.add("5X");
        mMainSeekBar.setDatas(datas);
    }
}
