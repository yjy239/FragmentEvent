# FragmentEvent
A EventBus Tools:it can constrain event in a Fragment（一个EventBus工具，能够约束信号在一个片层中）

# How To Use It
we must init FragmentEvent at First(Or before use EventBus)

# Two Step for FragmentEvent

## 1.Create Two character for FragmentEvent

### 1.EventObject

When we want to create a new Event，we must extends EventObject.
At the same time, we must specifies the generic which extends BaseEventFragment
```
public class WxEvent extends EventObject<TestFragment> {
    public int code;

    public WxEvent() {
        this(CODE_SUCCESS);
    }

    public WxEvent(int code) {
        this.code = code;
    }
}
```

new class of Event must have a no parameter constructor.


### 2.BaseEventFragment
this fragment is a Event Handler.

we must specifies the generic which extends EventObject.
```
public class TestFragment extends BaseEventFragment<WxEvent> {
    public TestFragment(Context context,Lifecycle lifecycle) {
        super(context,lifecycle);
    }

    @Override
    public void onHandle(WxEvent event) {
    // the handle method
        Log.e("WxEvent",event.code+"");
    }

    @Override
    public boolean isInterceptor(WxEvent event) {
    //control whether go to "onHandle" method
        return false;
    }

}
```

## 2.register where it want to handle

we can register eventbus:
```
FragmentEvent.getDefault().register(this, WxEvent.class);
```

Pay a Attention! the LifeCyle of Event Handler will be bound to Context(Application/Activity/Fragment)

Instead of:
```
EventBus.getDefault().register(this);
```

Additionally,we can use those register methods at the same time.(but register method of FragmentEvent must be used firstly in the whole App)




