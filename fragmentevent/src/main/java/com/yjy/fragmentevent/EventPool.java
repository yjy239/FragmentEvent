package com.yjy.fragmentevent;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/24
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class EventPool {
    final ConcurrentHashMap<Object, BaseEventFragment> mEventPool = new ConcurrentHashMap<>();


    private EventPool(){

    }

    private static class Holder{
        private static EventPool sPool = new EventPool();
    }


    public static EventPool get(){
        return Holder.sPool;
    }


    public void put(Context context,BaseEventFragment eventFragment){
        mEventPool.put(context,eventFragment);
    }

    public void put(Fragment fragment, BaseEventFragment eventFragment){
        mEventPool.put(fragment,eventFragment);
    }
}
