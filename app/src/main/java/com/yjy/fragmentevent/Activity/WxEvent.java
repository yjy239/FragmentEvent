package com.yjy.fragmentevent.Activity;


import com.yjy.fragmentevent.EventObject;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/21
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class WxEvent extends EventObject<TestFragment> {
    public int code;

    public WxEvent() {

    }

    public WxEvent(int code) {
        this.code = code;
    }
}
