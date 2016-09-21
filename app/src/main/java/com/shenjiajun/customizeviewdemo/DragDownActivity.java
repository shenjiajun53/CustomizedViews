package com.shenjiajun.customizeviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.shenjiajun.customizeviewdemo.views.DragDownLayout;

public class DragDownActivity extends AppCompatActivity {

    private DragDownLayout dragDownLayout;
    private RelativeLayout dragAnchor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_down);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dragDownLayout = (DragDownLayout) findViewById(R.id.drag_down_layout);
        dragAnchor = (RelativeLayout) findViewById(R.id.drag_anchor_layout);
        dragAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dragDownLayout.isDroped()) {
                    dragDownLayout.slideUp();
                } else {
                    dragDownLayout.dropDown();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dragDownLayout.isDroped()) {
                    dragDownLayout.slideUp();
                } else {
                    dragDownLayout.dropDown();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
