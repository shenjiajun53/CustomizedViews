package com.shenjiajun.customizeviewdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.shenjiajun.customizeviewdemo.databinding.ContentLuckWheelBinding;
import com.shenjiajun.customizeviewdemo.models.AwardModel;
import com.shenjiajun.customizeviewdemo.viewModels.LuckyWheelViewModel;
import com.shenjiajun.customizeviewdemo.views.LuckyWheelView;

import java.util.ArrayList;

public class LuckWheelActivity extends AppCompatActivity {


//    private LuckyWheelView luckyWheelView;
//
//    private Button control1, control2, control3, control4;

    private RelativeLayout contentlayout;

    private LuckyWheelViewModel luckyWheelViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_wheel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//            }
//        });
        contentlayout = (RelativeLayout) findViewById(R.id.content_luck_wheel);
        ContentLuckWheelBinding contentLuckWheelBinding = DataBindingUtil.bind(contentlayout);
        luckyWheelViewModel = new LuckyWheelViewModel();
        luckyWheelViewModel.setContentLuckWheelBinding(contentLuckWheelBinding);
        contentLuckWheelBinding.setViewModel(luckyWheelViewModel);

//        luckyWheelView = (LuckyWheelView) findViewById(R.id.lucky_wheel);
//        initView();
    }

//    private void initView() {
//
//
//        control1 = (Button) findViewById(R.id.controler1);
//        control1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                luckyWheelView.startRotation(1);
//            }
//        });
//
//        control2 = (Button) findViewById(R.id.controler2);
//        control2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                luckyWheelView.startRotation(5);
//            }
//        });
//
//        control3 = (Button) findViewById(R.id.controler3);
//        control3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                luckyWheelView.startRotation(-1);
//            }
//        });
//
//        control4 = (Button) findViewById(R.id.controler4);
//        control4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                luckyWheelView.startPreSetRotation();
//            }
//        });
//    }

}
