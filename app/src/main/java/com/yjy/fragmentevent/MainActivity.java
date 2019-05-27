package com.yjy.fragmentevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yjy.fragmentevent.Activity.WxEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //FragmentEvent.builder().addIndex(new index()).installDefaultEventBus();
        FragmentEvent.getDefault().register(this, WxEvent.class);
        EventBus.getDefault().register(this);


        Button tv = findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });

        EventBus.getDefault().post(new WxEvent(10));

        EventBus.getDefault().post(new Event(3));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handle(Event index){
        Log.e("index",index.code+"");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
