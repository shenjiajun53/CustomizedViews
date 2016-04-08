package com.shenjiajun.customizeviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shenjiajun.customizeviewdemo.views.WaveView;

import java.util.Timer;
import java.util.TimerTask;

public class WaveActivity extends AppCompatActivity {


    private TimerTask timerTask;
    private Timer timer;
    WaveView waveView;
    private float percent = 0;
    private boolean percentAdd;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 123:

                    waveView.setmPercent(percent);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        waveView = (WaveView) findViewById(R.id.wave_view);
        initTask();
    }

    private void initTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (percent >= 1) {
                    percentAdd = false;
                } else if (percent <= 0) {
                    percentAdd = true;
                }
                percent = (float) (percentAdd ? percent + 0.1 : percent - 0.1);
                mHandler.sendEmptyMessage(123);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }
}
