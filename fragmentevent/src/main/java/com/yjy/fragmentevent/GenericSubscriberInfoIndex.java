package com.yjy.fragmentevent;



import com.yjy.fragmentevent.info.EmptySubscriberInfo;
import com.yjy.fragmentevent.info.GenericSubscriberInfo;

import org.greenrobot.eventbus.meta.SubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/22
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class GenericSubscriberInfoIndex implements SubscriberInfoIndex {


    public GenericSubscriberInfoIndex() {
    }

    @Override
    public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {

        if(BaseEventFragment.class.isAssignableFrom(subscriberClass)&&subscriberClass != BaseEventFragment.class){
            return new GenericSubscriberInfo(subscriberClass,null);
        }else if(subscriberClass == BaseEventFragment.class){
            return new EmptySubscriberInfo();
        }
        return null;
    }


}
