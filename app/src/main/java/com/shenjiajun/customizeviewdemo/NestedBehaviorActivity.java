package com.shenjiajun.customizeviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NestedBehaviorActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String[] viewNames =
            {"aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb",
                    "aaaaaaaa", "bbbbbbbb"};
    private MyAdapter myAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_behavior);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("title");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.nested_recycler);
        myAdapter = new MyAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NestedBehaviorActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(myAdapter);
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_main_recycler, parent, false);
            MyHolder myHolder = new MyHolder(linearLayout);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            holder.textView.setText(viewNames[position]);
        }

        @Override
        public int getItemCount() {
            return viewNames.length;
        }

        class MyHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public MyHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_tv);
            }
        }
    }

}
