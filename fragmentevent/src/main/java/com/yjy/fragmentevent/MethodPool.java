package com.yjy.fragmentevent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/23
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class MethodPool {
    final Map<Class, Map<Class, Object>> anyMethodByClass = new HashMap<>();


    private MethodPool(){

    }

    private static class Holder{
        private static MethodPool sPool = new MethodPool();
    }

    public static MethodPool get(){
        return Holder.sPool;
    }




    public boolean checkAdd(Class parent,Method method, Class<?> eventType) {
        // 2 level check: 1st level with event type only (fast), 2nd level with complete signature when required.
        // Usually a subscriber doesn't have methods listening to the same event type.
        Map<Class,Object> anyMethodByEventType =  anyMethodByClass.get(parent);
        if(anyMethodByEventType == null||anyMethodByEventType.size() <= 0){
            anyMethodByEventType = new HashMap<>();
        }

        if(anyMethodByEventType.get(eventType) == null){
            anyMethodByClass.put(parent,anyMethodByEventType);
            return true;
        }else {
            return false;
        }


    }

}
