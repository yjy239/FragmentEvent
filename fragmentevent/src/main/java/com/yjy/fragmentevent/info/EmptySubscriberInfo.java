package com.yjy.fragmentevent.info;

import org.greenrobot.eventbus.SubscriberMethod;
import org.greenrobot.eventbus.meta.SubscriberInfo;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/23
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class EmptySubscriberInfo implements SubscriberInfo {
    @Override
    public Class<?> getSubscriberClass() {
        return null;
    }

    @Override
    public SubscriberMethod[] getSubscriberMethods() {
        return new SubscriberMethod[0];
    }

    @Override
    public SubscriberInfo getSuperSubscriberInfo() {
        return null;
    }

    @Override
    public boolean shouldCheckSuperclass() {
        return false;
    }
}
