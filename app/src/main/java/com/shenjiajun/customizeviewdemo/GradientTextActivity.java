package com.shenjiajun.customizeviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.shenjiajun.customizeviewdemo.views.GradientTextView;

public class GradientTextActivity extends AppCompatActivity implements View.OnClickListener {

    private GradientTextView gradientTextView;

    private Button resetBtn, doneBtn, toggleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                gradientTextView.startAnimation();
            }
        });

        gradientTextView = (GradientTextView) findViewById(R.id.gradient_tv);

        resetBtn = (Button) findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(this);
        doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(this);
        toggleBtn = (Button) findViewById(R.id.toggle_btn);
        toggleBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_btn:
                gradientTextView.setInit();
                break;
            case R.id.done_btn:
                gradientTextView.setDone();
                break;
            case R.id.toggle_btn:
                gradientTextView.startAnimation();
                break;
            default:
                break;
        }
    }
}
