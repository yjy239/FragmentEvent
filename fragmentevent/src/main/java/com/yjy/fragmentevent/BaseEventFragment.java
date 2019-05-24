package com.yjy.fragmentevent;

import android.support.annotation.NonNull;

import com.yjy.fragmentevent.lifemanager.EventFragment;
import com.yjy.fragmentevent.lifemanager.Lifecycle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/21
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public abstract class BaseEventFragment<T> extends EventFragment {


    public BaseEventFragment(@NonNull Lifecycle lifecycle){
        super(lifecycle);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(T event){
        if(!isInterceptor(event)){
            onHandle(event);
        }
    }



    public abstract void onHandle(T event);

    public abstract boolean isInterceptor(T event);

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
