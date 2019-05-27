package com.yjy.fragmentevent;

import android.annotation.SuppressLint;

import com.yjy.fragmentevent.lifemanager.EventFragment;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/24
 *     desc   : 注意要保证一个Activity/Fragment/App一种组件只存在一个
 *     github:yjy239@gitub.com
 * </pre>
 */
public class EventPool<T extends EventFragment,K extends Class<EventObject>>{
    final ConcurrentHashMap<Object, HashMap<K,T>> mEventPool = new ConcurrentHashMap<>();


    private EventPool(){

    }

    private static class Holder{
        private static EventPool sPool = new EventPool();
    }


    public static EventPool getInstance(){
        return Holder.sPool;
    }

    public void put(Object o,K event, T eventFragment){
        HashMap<K,T> map = getMap(o,event);
        map.put(event,eventFragment);
        mEventPool.put(o,map);
    }

    private HashMap<K,T> getMap(Object o,K event){
        HashMap<K,T> map = mEventPool.get(o);
        if(map == null){
            map = new HashMap<>();
        }
        return map;
    }


   public T get(Object o,K event){
       HashMap<K,T> map = getMap(o,event);
       return map.get(event);
   }


   public void recycle(Object o,K event){
       HashMap<K,T> map = getMap(o,event);
       map.remove(event);
   }

    public void recycle(Object o){
        HashMap<K,T> map = mEventPool.get(o);
        for(K fragment : map.keySet()){
            map.remove(fragment);
        }

        mEventPool.remove(o);
    }
}
