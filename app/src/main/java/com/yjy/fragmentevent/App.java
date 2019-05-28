package com.yjy.fragmentevent;

import android.app.Application;

import com.yjy.fragmentevent.Activity.AppEvent;
import com.yjy.fragmentevent.app.WholeAppEvent;
import com.yjy.fragmentevent.app.WholeEventFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/24
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //       FragmentEvent.builder().addIndex(new index())
//               .eventInheritance(false)
//               .ignoreGeneratedIndex(true)
//               .installDefaultEventBus();
        FragmentEvent.getDefault().register(this, WholeAppEvent.class);
        WholeAppEvent e = new WholeAppEvent();
        e.setCode(3);
        EventBus.getDefault().post(e);

    }
}
