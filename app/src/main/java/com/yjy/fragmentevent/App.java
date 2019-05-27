package com.yjy.fragmentevent;

import android.app.Application;

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
        FragmentEvent.getDefault();
    }
}
