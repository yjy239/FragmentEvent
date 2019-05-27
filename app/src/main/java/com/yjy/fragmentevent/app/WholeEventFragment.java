package com.yjy.fragmentevent.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yjy.fragmentevent.BaseEventFragment;
import com.yjy.fragmentevent.lifemanager.Lifecycle;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/27
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class WholeEventFragment extends BaseEventFragment<WholeAppEvent> {
    public WholeEventFragment(Context context, @NonNull Lifecycle lifecycle) {
        super(context, lifecycle);
    }

    @Override
    public void onHandle(WholeAppEvent event) {
        Log.e("whole",event.getCode()+"");
    }

    @Override
    public boolean isInterceptor(WholeAppEvent event) {
        return false;
    }
}
