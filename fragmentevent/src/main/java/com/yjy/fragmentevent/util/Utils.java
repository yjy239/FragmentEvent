package com.yjy.fragmentevent.util;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;


import com.yjy.fragmentevent.EventObject;
import com.yjy.fragmentevent.lifemanager.EventFragment;
import com.yjy.fragmentevent.lifemanager.Lifecycle;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/24
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class Utils {

    public static   boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static   boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

    @NonNull
    @SuppressWarnings("UseBulkOperation")
    public static <T> List<T> getSnapshot(@NonNull Collection<T> other) {
        // toArray creates a new ArrayList internally and does not guarantee that the values it contains
        // are non-null. Collections.addAll in ArrayList uses toArray internally and therefore also
        // doesn't guarantee that entries are non-null. WeakHashMap's iterator does avoid returning null
        // and is therefore safe to use. See #322, #2262.
        List<T> result = new ArrayList<>(other.size());
        for (T item : other) {
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    public  static  <T extends EventFragment,K extends EventObject> Class<T> getEventHandlerClass(Class<K> event){
        Type result = null;
        try {
            K object = event.getDeclaredConstructor().newInstance();
            Type type = object.getClass().getGenericSuperclass();
            if(type instanceof ParameterizedType){
                ParameterizedType p = (ParameterizedType)type;
                result = p.getActualTypeArguments()[0];
            }

            return (Class<T>) result;

        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException(event.getName()+"实例异常,继承EventObject必须包含无参构造函数");

        }


    }

    public static <T extends EventFragment> T getEventFragment(Class<T> eventHandlerClass, Context context,Lifecycle lifecycle){
        if(eventHandlerClass == null){
            throw new IllegalArgumentException("EventObject实例异常,必须包含无参构造函数");
        }

        T fragment = null;
        try {
            Constructor constructor = eventHandlerClass.getConstructor(Context.class,Lifecycle.class);
            fragment = (T) constructor.newInstance(context,lifecycle);
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException(eventHandlerClass.getName()+"必须为EventObject指定泛型以及为BaseEventFragment指定泛型");
        }

        return fragment;
    }
}
