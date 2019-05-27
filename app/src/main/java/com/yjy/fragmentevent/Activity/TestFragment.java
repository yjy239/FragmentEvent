package com.yjy.fragmentevent.Activity;

import android.content.Context;
import android.util.Log;

import com.yjy.fragmentevent.BaseEventFragment;
import com.yjy.fragmentevent.lifemanager.Lifecycle;


/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/23
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class TestFragment extends BaseEventFragment<WxEvent> {
    public TestFragment(Context context,Lifecycle lifecycle) {
        super(context,lifecycle);
    }

    @Override
    public void onHandle(WxEvent event) {
        Log.e("WxEvent",event.code+"");
    }

    @Override
    public boolean isInterceptor(WxEvent event) {
        return false;
    }




}
