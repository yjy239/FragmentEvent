package com.yjy.fragmentevent.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yjy.fragmentevent.BaseEventFragment;
import com.yjy.fragmentevent.lifemanager.Lifecycle;


/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/24
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class AppTestFragment extends BaseEventFragment<AppEvent> {
    public AppTestFragment(Context context,Lifecycle lifecycle) {
        super(context,lifecycle);
    }

    @Override
    public void onHandle(AppEvent event) {
        Log.e("appevent",event.getCode()+"");
    }

    @Override
    public boolean isInterceptor(AppEvent event) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("AppTestFragment","onDestroy");
    }
}
