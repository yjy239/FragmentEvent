package com.yjy.fragmentevent.Fragment;

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
public class TestEventInner extends BaseEventFragment<TestInner> {
    public TestEventInner(Context context, Lifecycle lifecycle) {
        super(context,lifecycle);
    }

    @Override
    public void onHandle(TestInner event) {
        Log.e("TestEventInner","event:"+event.getCode());
    }

    @Override
    public boolean isInterceptor(TestInner event) {
        return false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TestEventInner","onDestroy");
    }

}
