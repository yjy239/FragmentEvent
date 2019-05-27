package com.yjy.fragmentevent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.yjy.fragmentevent.Fragment.TestInner;
import com.yjy.fragmentevent.InnerFragment.InInner;
import com.yjy.fragmentevent.InnerFragment.InInnerFragment;

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
public class TestInnerFragment extends Fragment {

    public TestInnerFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentEvent.getDefault().register(this, TestInner.class);
        EventBus.getDefault().post(new TestInner());


        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Fragment fragment =  manager.findFragmentByTag("child");
        if(fragment == null){
            fragment = new InInnerFragment();
            fragmentTransaction.add(fragment,"child");
            fragmentTransaction.commitNow();
        }

        EventBus.getDefault().post(new InInner());
    }
}
