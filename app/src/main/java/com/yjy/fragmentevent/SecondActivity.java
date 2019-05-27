package com.yjy.fragmentevent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.yjy.fragmentevent.Activity.AppEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/24
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentEvent.getDefault().register(this, AppEvent.class);
        FragmentEvent.getDefault().register(this, AppEvent.class);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Fragment fragment =  manager.findFragmentByTag("ss");
        if(fragment == null){
            fragment = new TestInnerFragment();
            fragmentTransaction.add(fragment,"ss");
            fragmentTransaction.commitNow();
        }

        EventBus.getDefault().post(new AppEvent());

        Button b = findViewById(R.id.tv);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                Fragment fragment =  manager.findFragmentByTag("ss");
                if(fragment != null){
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commitNow();
                }
            }
        });

    }
}
