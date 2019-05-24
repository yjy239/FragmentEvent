package com.yjy.fragmentevent.info;

import android.util.Log;


import com.yjy.fragmentevent.MethodPool;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberMethod;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.meta.SubscriberInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/22
 *     desc   :只用来解析BaseEventFragment的数据
 *     github:yjy239@gitub.com
 * </pre>
 */
public class GenericSubscriberInfo implements SubscriberInfo {

    private static final int BRIDGE = 0x40;
    private static final int SYNTHETIC = 0x1000;
    private static final String TAG = GenericSubscriberInfo.class.getName();
    private final Class<?> subscriberClass;
    private final Class<? extends SubscriberInfo> superSubscriberInfoClass;
    private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT | Modifier.STATIC | BRIDGE | SYNTHETIC;

    public GenericSubscriberInfo(Class subscriberClass, Class<? extends SubscriberInfo> superSubscriberInfoClass) {
        this.superSubscriberInfoClass = superSubscriberInfoClass;
        this.subscriberClass = subscriberClass;
    }

    @Override
    public Class<?> getSubscriberClass() {
        return subscriberClass;
    }

    @Override
    public SubscriberMethod[] getSubscriberMethods() {
        SubscriberMethod[] subscriberMethods = null;
        try {
            //抽象类我就不去找了
            //只有这个情况我需要挂在缓存判断是否已经注册过了
            boolean isAbstract = Modifier.isAbstract(subscriberClass.getModifiers());
            if (isAbstract) {
                return new SubscriberMethod[0];
            }
            subscriberMethods = getMethods(subscriberClass.getMethods(),true);
        }catch (Exception e){
            e.printStackTrace();
        }

        return subscriberMethods;
    }



    private SubscriberMethod[] getMethods(Method[] methods, boolean isFragment){
        if(methods.length <= 0){
             return new SubscriberMethod[0];
        }
        List<SubscriberMethod> SubscriberMethods = new ArrayList<>();
        try {

            methods = subscriberClass.getMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                int modifiers = method.getModifiers();

                Class<?>[] parameterTypes = method.getParameterTypes();
                //只去找这个一个参数的方法
                if (((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0)
                        && parameterTypes.length == 1) {
                    Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                    if(subscribeAnnotation != null){
                        Class[] params = method.getParameterTypes();
                        ThreadMode threadMode = subscribeAnnotation.threadMode();
                        Class<?> eventType = params[0];

                        //判断此时是不是BaseEventFragment的onEvent方法
                        if(isFragment&&"onEvent".equals(method.getName())){
                            //eventType 切换为BaseEventFragment的泛型
                            ParameterizedType type =((ParameterizedType)subscriberClass.getGenericSuperclass());
                            if(type != null){
                                Log.d("type",type.getActualTypeArguments()[0]+"");
                                Type generic = type.getActualTypeArguments()[0];
                                eventType = (Class<?>)generic;
                            }

                        }

                        //不是则说明是新注入的,同时缓存不存在
                        if(MethodPool.get().checkAdd(subscriberClass,method,eventType)){
                            SubscriberMethod m = new SubscriberMethod(method, eventType, threadMode,
                                    subscribeAnnotation.priority(), subscribeAnnotation.sticky());
                            SubscriberMethods.add(m);
                        }

                    }

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return SubscriberMethods.toArray(new SubscriberMethod[SubscriberMethods.size()]);
    }

    @Override
    public SubscriberInfo getSuperSubscriberInfo() {
        if(superSubscriberInfoClass == null) {
            return null;
        }
        try {
            return superSubscriberInfoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shouldCheckSuperclass() {
        return false;
    }
}
