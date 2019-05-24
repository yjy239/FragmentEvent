package com.yjy.fragmentevent.lifemanager;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.yjy.fragmentevent.util.Utils;


/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/24
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class EventFragment implements LifecycleListener{
    @SuppressWarnings("WeakerAccess")
    final Lifecycle mLifecycle;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Runnable addSelfToLifecycle = new Runnable() {
        @Override
        public void run() {
            mLifecycle.addListener(EventFragment.this);
        }
    };
    public EventFragment(@NonNull Lifecycle lifecycle){
        this.mLifecycle = lifecycle;

        if (Utils.isOnBackgroundThread()) {
            mainHandler.post(addSelfToLifecycle);
        } else {
            lifecycle.addListener(this);
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
