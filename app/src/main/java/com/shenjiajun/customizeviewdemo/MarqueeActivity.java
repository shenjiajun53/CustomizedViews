package com.shenjiajun.customizeviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shenjiajun.customizeviewdemo.views.MarqueeTextView;

import java.util.ArrayList;

public class MarqueeActivity extends AppCompatActivity {

    private MarqueeTextView marqueeTextView1, marqueeTextView2;
    private ArrayList<String> contentStringList = new ArrayList<>();
    private String[] viewNames =
            {"WaveView达到撒旦法反对反对撒", "CircleImageView", "ScratchView"
                    , "自定义ViewGroup", "DragHelperView方大苏打撒旦发射的的覆盖辅导费", "自定义Drawer"
                    , "自定义下拉", "GradientTextView股份过户费工夫呼庚呼癸规范化", "自定义双向跑马灯"

                    , "to be continue"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);
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

        for (int i = 0; i < viewNames.length; i++) {
            contentStringList.add(viewNames[i]);
        }
        initView();
    }

    private void initView() {
        marqueeTextView1 = (MarqueeTextView) findViewById(R.id.marquee_1);
        marqueeTextView1.setContentList(contentStringList);

//        marqueeTextView2 = (MarqueeTextView) findViewById(R.id.marquee_2);
//        marqueeTextView2.setContentList(contentStringList);
    }

}
