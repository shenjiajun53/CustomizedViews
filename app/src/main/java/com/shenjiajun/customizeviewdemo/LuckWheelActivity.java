package com.shenjiajun.customizeviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.shenjiajun.customizeviewdemo.models.AwardModel;
import com.shenjiajun.customizeviewdemo.views.LuckyWheelView;

import java.util.ArrayList;

public class LuckWheelActivity extends AppCompatActivity {

    private ArrayList<String> contentStringList = new ArrayList<>();
    private ArrayList<AwardModel> awardModelArrayList = new ArrayList<>();
    private String[] viewNames =
            {"00000", "111111", "222222", "333333", "4444444", "5555555"};
    private String[] viewNames2 =
            {"00000", "111111", "222222", "333333", "4444444", "5555555"};
    private double[] percents = {0.1, 0.1, 0.3, 0.05, 0, 0.45};

    private LuckyWheelView luckyWheelView;

    private Button control1, control2, control3, control4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_wheel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });

        for (int i = 0; i < viewNames.length; i++) {
            AwardModel awardModel = new AwardModel();
            awardModel.setTitle(viewNames[i]);
            awardModel.setContent(viewNames2[i]);
            awardModel.setIndex(i);
            awardModel.setPercent(percents[i]);
            awardModelArrayList.add(awardModel);
            contentStringList.add(viewNames[i]);
        }

        initView();
    }

    private void initView() {
        luckyWheelView = (LuckyWheelView) findViewById(R.id.lucky_wheel);
        luckyWheelView.setAwardsList(awardModelArrayList);

        control1 = (Button) findViewById(R.id.controler1);
        control1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luckyWheelView.startRotation(1);
            }
        });

        control2 = (Button) findViewById(R.id.controler2);
        control2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luckyWheelView.startRotation(5);
            }
        });

        control3 = (Button) findViewById(R.id.controler3);
        control3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luckyWheelView.startRotation(-1);
            }
        });

        control4 = (Button) findViewById(R.id.controler4);
        control4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luckyWheelView.startRotationByProbability();
            }
        });
    }

}
