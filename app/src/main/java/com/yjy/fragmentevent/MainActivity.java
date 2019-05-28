package com.yjy.fragmentevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yjy.fragmentevent.Activity.AppEvent;
import com.yjy.fragmentevent.Activity.TestFragment;
import com.yjy.fragmentevent.Activity.WxEvent;
import com.yjy.fragmentevent.app.WholeAppEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //FragmentEvent.builder().addIndex(new index()).installDefaultEventBus();
        TestFragment handler = FragmentEvent.getDefault().register(this, WxEvent.class);
        FragmentEvent.getDefault().register(this, WxEvent.class);
        EventBus.getDefault().register(this);

        final WholeAppEvent e = new WholeAppEvent();
        e.setCode(5);


        Button tv = findViewById(R.id.tv);
        Button tv2 = findViewById(R.id.tv1);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(e);
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AppEvent());
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
