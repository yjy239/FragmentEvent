package com.yjy.fragmentevent;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/21
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class EventObject<T extends BaseEventFragment> {
    int mCode;
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAIL = -1;

    public EventObject() {
        this(CODE_SUCCESS);
    }

    public EventObject(int code) {
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    public boolean isSuccess(){
        return mCode == CODE_SUCCESS;
    }


}
