package com.yjy.fragmentevent.InnerFragment;

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
public class Innerfragment extends BaseEventFragment<InInner> {
    public Innerfragment(@NonNull Context context, @NonNull Lifecycle lifecycle) {
        super(context,lifecycle);
    }

    @Override
    public void onHandle(InInner event) {
        Log.e("InInner",event.getCode()+"");
    }

    @Override
    public boolean isInterceptor(InInner event) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Innerfragment","onDestroy");
    }
}
