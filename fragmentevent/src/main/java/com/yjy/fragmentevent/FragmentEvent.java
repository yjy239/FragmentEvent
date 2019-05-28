package com.yjy.fragmentevent;

import android.content.Context;
import android.support.v4.app.Fragment;


import com.yjy.fragmentevent.lifemanager.EventFragment;
import com.yjy.fragmentevent.lifemanager.EventLifeManager;

import org.greenrobot.eventbus.EventBus;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/21
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class FragmentEvent {


    private  EventLifeManager mManager;


    private volatile static FragmentEventBuilder BUILDER = builder().addIndex(new GenericSubscriberInfoIndex());
    volatile static FragmentEvent sInstance;


    FragmentEvent(){
        if(mManager == null){
            mManager = new EventLifeManager();
        }
    }


    FragmentEvent(FragmentEventBuilder builder){
        if(mManager == null){
            mManager = new EventLifeManager();
        }

        builder.installDefaultEventBus();

    }

    public static FragmentEventBuilder builder() {
        return new FragmentEventBuilder(EventBus.builder().addIndex(new GenericSubscriberInfoIndex()));
    }


    public static FragmentEvent getDefault(){
        if(sInstance == null){
            synchronized (FragmentEvent.class){
                if(sInstance == null){
                    sInstance = new FragmentEvent(BUILDER);
                }
            }
        }
        return sInstance;
    }


    public EventLifeManager getLifeManager(){
        return mManager;
    }


    public <T extends EventFragment,K extends EventObject> T register(Context context, Class<K> event){
        return mManager.get(context,event);
    }

    public <T extends EventFragment,K extends EventObject> T register(Fragment fragment, Class<K> event){
        return mManager.get(fragment,event);
    }

    public <T extends EventFragment,K extends EventObject> T register(android.app.Fragment fragment, Class<K> event){
        return mManager.get(fragment,event);
    }





}
