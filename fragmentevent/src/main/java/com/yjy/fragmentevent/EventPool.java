package com.yjy.fragmentevent;

import com.yjy.fragmentevent.lifemanager.EventFragment;

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

    private final ConcurrentHashMap<Object, ConcurrentHashMap<K,T>> mEventPool = new ConcurrentHashMap<>();


    private EventPool(){

    }

    private static class Holder{
        private static EventPool sPool = new EventPool();
    }


    public static EventPool getInstance(){
        return Holder.sPool;
    }

    public void put(Object o,K event, T eventFragment){
        ConcurrentHashMap<K,T> map = getMap(o,event);
        map.put(event,eventFragment);
        mEventPool.put(o,map);
    }

    private ConcurrentHashMap<K,T> getMap(Object o,K event){
        ConcurrentHashMap<K,T> map = mEventPool.get(o);
        if(map == null){
            map = new ConcurrentHashMap<>();
        }
        return map;
    }


   public T get(Object o,K event){
       ConcurrentHashMap<K,T> map = getMap(o,event);
       return map.get(event);
   }


   public void recycle(Object o,K event){
       ConcurrentHashMap<K,T> map = getMap(o,event);
       map.remove(event);
   }


    public void recycle(Object o){
        ConcurrentHashMap<K,T> map = mEventPool.get(o);
        if(map != null){
            for(K fragment : map.keySet()){
                if(fragment != null){
                    T f = map.remove(fragment);
                    f = null;
                }

            }
            mEventPool.remove(o);
            map = null;
        }


    }
}
