package com.yjy.fragmentevent.lifemanager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.yjy.fragmentevent.EventPool;
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
    private boolean isStop = false;
    protected Context mContext;
    private boolean isDestroy = false;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Runnable addSelfToLifecycle = new Runnable() {
        @Override
        public void run() {
            mLifecycle.addListener(EventFragment.this);
        }
    };
    public EventFragment(Context context, @NonNull Lifecycle lifecycle){
        this.mLifecycle = lifecycle;
        this.mContext = context;

        if (Utils.isOnBackgroundThread()) {
            mainHandler.post(addSelfToLifecycle);
        } else {
            lifecycle.addListener(this);
        }
    }

    @Override
    public void onStart() {
        isStop = false;
    }

    @Override
    public void onStop() {
        isStop = true;
    }

    public boolean isStop(){
        return isStop;
    }

    public boolean isDestroy() {
        return isDestroy;
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        mContext = null;
    }
}
