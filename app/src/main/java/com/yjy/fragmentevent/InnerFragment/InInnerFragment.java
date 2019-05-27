package com.yjy.fragmentevent.InnerFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import com.yjy.fragmentevent.FragmentEvent;

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
public class InInnerFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentEvent.getDefault().register(this,InInner.class);
        EventBus.getDefault().post(new InInner());
    }
}
