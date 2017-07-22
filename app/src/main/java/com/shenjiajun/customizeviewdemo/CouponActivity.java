package com.shenjiajun.customizeviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shenjiajun.customizeviewdemo.views.CouponView;

public class CouponActivity extends AppCompatActivity {

    CouponView couponView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        couponView = (CouponView) findViewById(R.id.coupon_1);
        couponView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponView.setChecked(!couponView.isChecked());
            }
        });
    }

}
